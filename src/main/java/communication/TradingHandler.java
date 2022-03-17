package communication;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import view.GuiMainFrame;
import view.OptionPane;
import zork.Constants;
import zork.Item;
import zork.Zork;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Executes trading statements
 *
 * @version 14.03.2022
 * @author Yvonne Rahnfeld
 */
public class TradingHandler {

    /**
     * TradingHandler instance
     */
    private static TradingHandler INSTANCE;

    /**
     * Contains history of successful trades
     */
    private HashMap<Long, Trade> tradeHashMap = new HashMap<>();

    /**
     * Current trade to keep data and process trade
     */
    private Trade currentTrade;

    /**
     * Necessary to send message to trading partner after connection was lost
     */
    private String lastTradingPartner;

    /**
     * Pane for options (must be settable for mocks)
     */
    private OptionPane optionPane = new OptionPane();

    /**
     * Pane for messages (must be settable for mocks)
     */
    private OptionPane messagePane = new OptionPane();

    public void setOptionPane(OptionPane optionPane) {
        this.optionPane = optionPane;
    }

    public void setMessagePane(OptionPane messagePane) {
        this.messagePane = messagePane;
    }

    /**
     * Private Constructor (for Singleton pattern)
     */
    private TradingHandler() {}

    /**
     * Get TradingHandler instance as singleton
     * @return TradingHandler
     */
    public static TradingHandler getInstance() {
        if (INSTANCE == null) INSTANCE = new TradingHandler();
        return INSTANCE;
    }

    public Trade getCurrentTrade() {
        return currentTrade;
    }

    public void setCurrentTrade(Trade trade) {
        currentTrade = trade;
    }

    public HashMap<Long, Trade> getTradeHashMap() {
        return tradeHashMap;
    }

    public String getLastTradingPartner() {
        return lastTradingPartner;
    }

    /**
     * Build trade and send offer to trading partner, if no offer is open or player wants to replace the offer
     * @param offeredItem The item the player wants to offer
     * @param requestedItem The item the player wants to receive
     * @param tradingPartner The trading partner for this offer
     */
    public void initTradeOffer(Item offeredItem, String requestedItem, String tradingPartner) {
        // player is asked to overwrite existing offer
        if (currentTrade != null) {
            int dialogResult = optionPane.showConfirmDialog(GuiMainFrame.getInstance(),
                    "If you're starting a new trade offer your old offer will be aborted.\n" +
                            "Do you still want to start a new offer?", "New Trading Offer", JOptionPane.YES_NO_OPTION);
            if (dialogResult == 0) {
                sendTradeMessage(buildAbortedMessage(currentTrade.getOfferedItem().getName(), currentTrade.getRequestedItem(), "The trading partner " + currentTrade.getTradingPartner() +
                        " has aborted it to start a new trade."), Constants.TRADING_TOPIC + currentTrade.getTradingPartner());
                abortCurrentTrade("A new trade offer was sent!");
            } else {
                return;
            }
        }
        lastTradingPartner = tradingPartner;
        Trade tradeOffer = new Trade(offeredItem, requestedItem, tradingPartner, true);
        tradeOffer.setMyName(Zork.game.getPlayer().getName());
        setCurrentTrade(tradeOffer);
        tradeHashMap.put(tradeOffer.getTimeStamp(), tradeOffer);
        sendTradeMessage(buildTradeOffer(), Constants.TRADING_TOPIC + currentTrade.getTradingPartner());
        // start expiring timer thread
        currentTrade.start();
    }

    /**
     * Method to send a trade message to a topic
     * @param message the message
     * @param topic   the topic
     */
    public void sendTradeMessage(String message, String topic) {
        if (message.length() < 1) return;
        MqttMessage mqttMessage = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
        BrokerConnection.getInstance().publish(mqttMessage, topic);
    }

    /**
     * Player gets asked to accept trade offer. If he accepts the offer and owns the requested item the trade will be
     * continued. If he declines, the trade will be aborted.
     * @param input Offer Content: [0] messageCode, [1] offeredItem, [2] requestedItem, [3] playerName, [4] tradingPartner, [5] timestamp
     */
    public void receiveOffer(String[] input) {
        int dialogResult = optionPane.showConfirmDialog(GuiMainFrame.getInstance(), input[4] + " offers you a " + input[1] + " for a " +
                input[2] + ". Do you agree?", "Tradingoffer from " + input[4], JOptionPane.YES_NO_OPTION);
        if (dialogResult == 0) {
            if (Zork.game.getPlayer().inventory.hasItem(input[2])) {
                lastTradingPartner = input[4];
                Trade trade = new Trade(Zork.game.getPlayer().inventory.getItem(input[2]), input[1], input[4], false);
                trade.setTimeStamp(Long.parseLong(input[5]));
                trade.setMyName(input[3]);
                setCurrentTrade(trade);
                tradeHashMap.put(Long.parseLong(input[5]), trade);
                sendTradeMessage(Constants.TRADE_ACCEPTED, Constants.TRADING_TOPIC + input[4]);
                // start expiring timer thread
                currentTrade.start();
            } else {
                sendTradeMessage(buildAbortedMessage(input[2], input[1], "The trading partner " + input[3] +
                        " does not own the requested item " + input[2] + "."), Constants.TRADING_TOPIC + input[4]);
                abortCurrentTrade("You do not have the item. The trade will be declined!");
            }
        } else {
            sendTradeMessage(buildAbortedMessage(input[2], input[1], "The trading partner " + input[3] +
                    " declined your offer."), Constants.TRADING_TOPIC + input[4]);
            abortCurrentTrade("You declined the trade! Your trading partner will be notified about that.");
        }
    }

    /**
     * Start execution of accepted trade if player owns still his offered item, otherwise abort trade
     */
    public void acceptCurrentTrade() {
        if (Zork.game.getPlayer().inventory.hasItem(currentTrade.getOfferedItem().getName())) {
            sendTradeMessage(Constants.TRADE_EXECUTE, Constants.TRADING_TOPIC + currentTrade.getTradingPartner());
            executeCurrentTrade();
        } else {
            sendTradeMessage(buildAbortedMessage(currentTrade.getOfferedItem().getName(), currentTrade.getRequestedItem(),
                    "The trading partner " + currentTrade.getMyName() + " does not own the requested item " +
                            currentTrade.getOfferedItem().getName() + "."),Constants.TRADING_TOPIC + currentTrade.getTradingPartner());
            abortCurrentTrade("You do not have the item you offered! The trade will be aborted!");
        }
    }

    /**
     * Execute current trade: send item to trading partner, remove from inventory and set current trade to null
     */
    public void executeCurrentTrade() {
        Zork.game.getPlayer().inventory.removeItem(currentTrade.getOfferedItem());
        String transferItem = Constants.TRADE_ITEM_TRANSFER + ";" + currentTrade.getOfferedItem().getName() + ";" +
                currentTrade.getOfferedItem().getDescription() + ";" + currentTrade.getOfferedItem().getState() + ";" +
                currentTrade.getOfferedItem().getStrength() + ";" + currentTrade.getOfferedItem().getMaxStrength() + ";" +
                currentTrade.getOfferedItem().getWhere();
        sendTradeMessage(transferItem, Constants.TRADING_TOPIC + currentTrade.getTradingPartner());
        currentTrade = null;
    }

    /**
     * Add received item to inventory and show confirmation
     * @param itemData Content: [0] messageCode, [1] name, [2] description, [3] state, [4] strength, [5] maxStrength, [6] where
     */
    public void receiveItem(String[] itemData) {
        Item newItem = new Item(itemData[1], itemData[2], itemData[3], Integer.parseInt(itemData[4]), itemData[6]);
        newItem.setMaxStrength(Integer.parseInt(itemData[5]));
        Zork.game.getPlayer().inventory.addItem(newItem);
        messagePane.showMessageDialog(GuiMainFrame.getInstance(), "Item exchange was successful!\n\nItem <" + newItem.getName() +
                "> was added to your inventory.");
    }

    /**
     * Abort current trade: show message, remove from hash map and set current trade to null
     * @param dialogMessage Message to show in dialog
     */
    public void abortCurrentTrade(String dialogMessage) {
        messagePane.showMessageDialog(GuiMainFrame.getInstance(), dialogMessage);
        if (currentTrade != null) {
            tradeHashMap.remove(currentTrade.getTimeStamp());
        }
        currentTrade = null;
    }

    /**
     * Method to build an offer to send
     * structure: 4x chars type of message; name of offered item, name of requested item, name of tradingPartner,
     * my name; timestamp of the trade
     * @return the offer
     */
    public String buildTradeOffer(){
        return Constants.TRADE_OFFER + ";" + currentTrade.getOfferedItem().getName() + ";" + currentTrade.getRequestedItem() +
                ";" + currentTrade.getTradingPartner() + ";" + currentTrade.getMyName() + ";" + currentTrade.getTimeStamp();
    }

    /**
     * Build message for aborting trade
     * @param requestedItem Name of requested item
     * @param offeredItem Name of offered item
     * @param cause Cause of abortion (message)
     * @return Abortion message string
     */
    public String buildAbortedMessage(String requestedItem, String offeredItem, String cause) {
        return Constants.TRADE_ABORTED + "; Trade offer ( " + requestedItem + " for a " + offeredItem + " ) was aborted. " + cause;
    }
}
