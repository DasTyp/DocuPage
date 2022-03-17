package communication;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.MqttSubscription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zork.Constants;
import zork.Game;
import zork.Zork;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * class to test basic function of class BrokerConnection
 * @author Patrick Mayer
 * @version 03.02.2022
 */
class BrokerConnectionTest{

    Game game;
    private static final MqttSubscription testSubscription = new MqttSubscription("ZorkGroupFourTest",1);
    public static final MqttSubscription[] testTopic = {testSubscription};
    BrokerConnection test =BrokerConnection.getInstance();
    MqttMessage message = new MqttMessage("TestMessage".getBytes(StandardCharsets.UTF_8));

    /**
     * Set up test data
     */
    public BrokerConnectionTest() {
        InputStream instream = Zork.class.getResourceAsStream(Constants.NEW_GAME);
        game = Zork.parseData(instream);
        Zork.game = game;
    }

    /**
     * Assert message is sent and received correctly.
     * @author Patrick Mayer
     */
    @Test
    @DisplayName("Message will be sent and received successfully")
    public void testDeliveryCompleteAndReceivedMessage() throws InterruptedException {
        test.subscribeTopic(testTopic);
        test.publish(message,testTopic[0].getTopic());

        TimeUnit.SECONDS.sleep(1);

        assertEquals("TestMessage", test.receivedMessage);
    }

}
