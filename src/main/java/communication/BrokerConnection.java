package communication;

import controller.CommunicationController;
import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.*;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import zork.Constants;
import zork.Zork;

/**
 * class to create and manage a client for the MQTT-connection
 * @author Patrick Mayer
 * @version 03.02.2022
 */
public final class BrokerConnection implements MqttCallback {

    /**
     * Variable of the instance of the BrokerConnection
     */
    private static BrokerConnection INSTANCE;

    /**
     * Method to generate Instance of the class
     * @return the instance of BrokerConnection
     */
    public static BrokerConnection getInstance(){
        if(INSTANCE == null){
            INSTANCE = new BrokerConnection();
        }
        return INSTANCE;
    }
    /**
     * MQTT-client
     */
    private MqttClient client;

    /**
     * URL to the broker
     */
    final String broker = "tcp://broker.hivemq.com:1883";

    /**
     * Options for the MQTT Connection
     */
    private MqttConnectionOptions mqttConnectionOptions;

    /**
     * last message received from this callback
     */
    String receivedMessage ="";

    MqttSubscription[] initialTopics = {
            new MqttSubscription("zork4/chat/global",2),
            new MqttSubscription("zork4/chat/trading",2)
    };

    /**
     * Constructor to initialize the client and options. Furthermore the client is connected to the broker
     * the callback(the class, where the actions to certain events is handled) is set to the given class
     * and the client subscribes the array of given topics.
     * @author Patrick Mayer
     */
    private BrokerConnection(){
        try {
            client = new MqttClient(broker, "", new MemoryPersistence());
            mqttConnectionOptions = new MqttConnectionOptions();

            mqttConnectionOptions.setAutomaticReconnect(true);  // enable reconnect
            mqttConnectionOptions.setKeepAliveInterval(2);      // 2 seconds to notice offline
            mqttConnectionOptions.setMaxReconnectDelay(3000);   // wait max 3 sec for reconnection

            client.setCallback(this);
            client.connect(mqttConnectionOptions);

        }catch (Exception e){
            //swallow error
        }
    }

    /**
     * whether the client is connected or not
     * @return true if the client is connected
     * @author Jonas Pröll
     */
    public boolean isConnected(){
        return client.isConnected();
    }

    /**
     * Method to let the client subscribe to multiple or one topic
     * @param topic the array with the list of topics
     * @author Patrick Mayer
     */
    public void subscribeTopic(MqttSubscription[] topic){
        try {
            client.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to let the client subscribe to one topic
     * @param topic String of topic name
     * @param qos Quality of Service level
     * @author Yvonne Rahnfeld
     */
    public void subscribeTopic(String topic, int qos){
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to let the client unsubscribe from one topic
     * @param topic the name of the topic
     * @author Patrick Mayer
     */
    public void unsubscribeFromTopic(String topic){
        try{
            client.unsubscribe(topic);
        }catch(MqttException e){
            e.printStackTrace();
        }
    }

    /**
     * Method to let the client connect to the broker if it is not connected
     * @author Patrick Mayer
     */
    public void connectToClient(){
        if(!client.isConnected()){
            try {
                client.connect(mqttConnectionOptions);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to publish a message to a topic
     * @param message the message, which should be published
     * @param topic the topic in which the message should be published
     * @author Patrick Mayer
     */
    public void publish(MqttMessage message, String topic){
        try {
            client.publish(topic,message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to disconnect the client from the broker
     * @author Patrick Mayer
     */
    public void disconnect(){
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to grab Client ID
     * @author Christian Litke
     * @return This player's client id
     */
    public String getClientID(){
        return client.getClientId();
    }

    /**
     * is called when the client is disconnected
     * @param disconnectResponse
     */
    @Override
    public void disconnected(MqttDisconnectResponse disconnectResponse) {
        if (TradingHandler.getInstance().getCurrentTrade() != null) {
            TradingHandler.getInstance().abortCurrentTrade("You are disconnected from broker! Your current trade will be aborted.");
        }
        CommunicationController.getInstance().setConnectionStatus(Constants.STATE_NOT_CONNECTED);
    }

    /**
     * NOT IMPLEMENTED YET
     * @param exception
     */
    @Override
    public void mqttErrorOccurred(MqttException exception) {
    }

    /**
     * Method which is called when a message arrives on a subscribed topic
     * @param topic The topic the message arrived to
     * @param message The message which has arrived
     */
    @Override
    public void messageArrived(String topic, MqttMessage message){
        receivedMessage = message.toString();
        CommunicationController.getInstance().receiveMessage(topic, receivedMessage);
    }

    /**
     *NOT IMPLEMENTED YET
     * @param token
     */
    @Override
    public void deliveryComplete(IMqttToken token) {
    }

    /**
     * is called when the connection is complete
     * @param reconnect whether the connection was reconnected
     * @param serverURI
     */
    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        subscribeTopic(initialTopics);
        if (Zork.game.getRoom("traders hut").getVisited() > 0) {
            subscribeTopic(Constants.TRADING_TOPIC + Zork.game.getPlayer().getName(), 2);
        }
        CommunicationController.getInstance().setConnectionStatus(Constants.STATE_CONNECTED);
    }

    /**
     *NOT IMPLEMENTED YET
     * @param reasonCode
     * @param properties
     */
    @Override
    public void authPacketArrived(int reasonCode, MqttProperties properties) {
    }

}