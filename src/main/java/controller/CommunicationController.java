package controller;

import communication.BrokerConnection;
import communication.TradingHandler;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import view.ChatPanel;
import zork.Constants;
import zork.Helper;
import zork.Item;
import zork.Zork;
import zork.Player;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class CommunicationController {

    /**
     * CommunicationController instance
     */
    private static CommunicationController INSTANCE;

    /**
     * HashMap containing the messages (key = topic, value = ArrayList with messages)
     */
    private HashMap<String, ArrayList<String>> communication = new HashMap<>();

    /**
     * Current topic
     */
    private String currentTopic = "zork4/chat/global";

    /**
     * the Players name
     */
    private String playerName;

    /**
     * player object
     */
    private Player player;

    /**
     * Topic for item trading
     */
    private String tradingTopic;

    /**
     * Executes trading statements
     */
    private final TradingHandler tradingHandler = TradingHandler.getInstance();

    /**
     * CommunicationController constructor
     * @author Jonas Pröll
     */
    CommunicationController(){
        if(!BrokerConnection.getInstance().isConnected()){
            ChatPanel.getInstance().setOfflineMode(true);
        }
    }

    /**
     * get the CommunicationController instance (singleton pattern)
     * @return CommunicationController instance
     * @author Jonas Pröll
     */
    public static CommunicationController getInstance(){
        if(INSTANCE == null) INSTANCE = new CommunicationController();
        return INSTANCE;
    }

    /**
     * formats the message to the desired output
     * @param message the message to format
     * @return the formatted message
     * @author Jonas Pröll
     */
    private String formatMessage(String message){
        //return "[" + this.playerName + "] " + message; //TODO remove
        String color = Helper.getColorByString(this.playerName);
        return "<p>[<i style=\"color:" + color + "\">" + this.playerName + "</i>] " + message + "</p>";
    }

    /**
     * receive a message, check for trading actions
     * @param topic received topic
     * @param message received message
     * @author Jonas Pröll
     */
    public void receiveMessage(String topic, String message){

        if(topic.equals(tradingTopic))
        {
            String[] input = message.split(";");
            String messageCode = input[0];
            if (messageCode.equals(Constants.NAME_CHECK)){
                // if others check, send blocker
                if(!input[1].equals(BrokerConnection.getInstance().getClientID())){
                    MqttMessage mqttMessage = new MqttMessage((Constants.NAME_BLOCKED + ";" + BrokerConnection.getInstance().getClientID()).getBytes());
                    BrokerConnection.getInstance().publish(mqttMessage, (Constants.TRADING_TOPIC + playerName));
                }
            } else if (messageCode.equals(Constants.NAME_BLOCKED)) {
                // if receiving a blocker, change name
                if(!input[1].equals(BrokerConnection.getInstance().getClientID())){
                    BrokerConnection.getInstance().unsubscribeFromTopic(playerName);
                    playerName = playerName + "1";
                    player.setName(playerName);
                    setPlayerName(playerName);
                }
            }
            else{
                processTradingMessage(input);
            }
        } else {
            ArrayList<String> messages = communication.get(topic);

            //handle empty topics
            if (messages == null) messages = new ArrayList<>();

            //limit message amount
            if (messages.size() + 1 > Constants.MAX_MESSAGES) messages.remove(0);

            messages.add(message);
            communication.put(topic, messages);

            updateChatPanel();
        }
    }

    /**
     * updates the assigned ChatPanel
     * @author Jonas Pröll
     */
    private void updateChatPanel(){
        ChatPanel.getInstance().setMessages(getMessagesAsString(this.currentTopic));
    }

    /**
     * gets the messages of a specific topic as a String
     * @param topic the topic of the messages
     * @return the messages of a specific topic
     * @author Jonas Pröll
     */
    private String getMessagesAsString(String topic){
        ArrayList<String> messages = communication.get(topic);
        if(messages != null) return String.join("\n", messages);
        else return "";
    }

    /**
     * publish a message to a topic
     * @param messageText the message to publish
     * @author Jonas Pröll
     */
    public void publishToCurrentTopic(String messageText){
        if(messageText.length() <= 1) return;
        String formatted = formatMessage(messageText);
        MqttMessage message = new MqttMessage(formatted.getBytes(StandardCharsets.UTF_8));
        BrokerConnection.getInstance().publish(message, currentTopic);
    }

    /**
     * sets the current topic
     * @param topic the topic to set as current
     * @author Jonas Pröll
     */
    public void setCurrentTopic(String topic){
        this.currentTopic = topic;
        updateChatPanel();
    }

    /**
     * get the current topic
     * @return the current topic
     */
    public String getCurrentTopic(){
        return this.currentTopic;
    }

    /**
     * set the players name (required for formatted messages)
     * @param name the players name
     * @author Jonas Pröll
     */
    public void setPlayerName(String name){
        BrokerConnection.getInstance().unsubscribeFromTopic(Constants.TRADING_TOPIC+playerName);
        this.playerName = name;
        this.tradingTopic = Constants.TRADING_TOPIC+name;
        BrokerConnection.getInstance().subscribeTopic(tradingTopic,2);
        checkNameAvailability();
    }

    /**
     * set the player object (required to set player's name)
     * @param player
     * @author Christian Litke
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * returns the assigned players name
     */
    public String getPlayerName(){return this.playerName;}

    /**
     * Method to init a trade
     * @param offeredItem the Item the player offers
     * @param requestedItem the item the player requests
     * @param tradingPartner the name of the player, the player wants to trade with
     */
    public void initTradeOffer(Item offeredItem, String requestedItem, String tradingPartner) {
        tradingHandler.initTradeOffer(offeredItem, requestedItem, tradingPartner);
    }

    /**
     * set / handle the changed connection status
     * @param status the current connection status
     * @author Jonas Pröll
     */
    public void setConnectionStatus(String status) {
        switch (status) {
            case Constants.STATE_CONNECTED -> ChatPanel.getInstance().setOfflineMode(false);
            case Constants.STATE_NOT_CONNECTED -> ChatPanel.getInstance().setOfflineMode(true);
        }
    }

    public void checkNameAvailability(){
        MqttMessage mqttMessage = new MqttMessage((Constants.NAME_CHECK + ";" + BrokerConnection.getInstance().getClientID()).getBytes());
        BrokerConnection.getInstance().publish(mqttMessage, tradingTopic);
    }

    /**
     * Processes trading actions according to message code
     * @param input Trading message pieces
     */
    private void processTradingMessage(String input[]) {
        String messageCode = input[0];

        if (Zork.game.getRoom("traders hut").getVisited() < 1) {
            tradingHandler.sendTradeMessage(Constants.TRADE_ABORTED + "; Your trading partner " + input[1] + " has the feature trading not unlocked yet!", Constants.TRADING_TOPIC + input[4]);
        } else if (messageCode.equals(Constants.TRADE_OFFER)) {
            tradingHandler.receiveOffer(input);
        } else if (messageCode.equals(Constants.TRADE_ACCEPTED)) {
            tradingHandler.acceptCurrentTrade();
        } else if (messageCode.equals(Constants.TRADE_EXECUTE)) {
            tradingHandler.executeCurrentTrade();
        } else if (messageCode.equals(Constants.TRADE_ITEM_TRANSFER)) {
            tradingHandler.receiveItem(input);
        } else if (messageCode.equals(Constants.TRADE_ABORTED)) {
            tradingHandler.abortCurrentTrade(input[1]);
        }
    }
}