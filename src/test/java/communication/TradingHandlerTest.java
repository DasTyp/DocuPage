package communication;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import view.mocks.MockMessageDialog;
import view.mocks.MockOptionPaneNo;
import view.mocks.MockOptionPaneYes;
import zork.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Trading Feture
 *
 * @version 17.03.2022
 * @author Yvonne Rahnfeld
 */
public class TradingHandlerTest {

    Game game;
    BrokerConnection brokerConnection1 = BrokerConnection.getInstance();
    BrokerConnection brokerConnection2 = BrokerConnection.getInstance();
    TradingHandler tradingHandler = TradingHandler.getInstance();
    Item offeredItem = new Item("machete", "", "", 0, "");
    String requestedItemName = "fish";
    String playerName = "player";
    String tradingPartner = "tradingPartner";
    String topicPlayer = Constants.TRADING_TOPIC + playerName;
    String topicTradingPartner = Constants.TRADING_TOPIC + tradingPartner;
    Trade testTrade = new Trade(offeredItem, requestedItemName, tradingPartner, true);
    long testTradeTime = testTrade.getTimeStamp();

    /**
     * Set up test data
     */
    public TradingHandlerTest() {
        InputStream instream = BeachActionsTest.class.getResourceAsStream(Constants.NEW_GAME);
        game = Zork.parseData(instream);
        Zork.game = game;
        Zork.game.getPlayer().inventory.addItem(offeredItem);
        Zork.game.getPlayer().setName(playerName);
        testTrade.setMyName(playerName);
        Zork.game.getRoom("traders hut").incrementVisited();
        brokerConnection1.subscribeTopic(topicPlayer, 2);
        brokerConnection2.subscribeTopic(topicTradingPartner, 2);
        tradingHandler.setCurrentTrade(null);
        tradingHandler.getTradeHashMap().clear();
    }

    @Test
    @DisplayName("Test initTradeOffer, no current trade was set before -> new offer created and sent")
    public void testInitTradeOffer_NoCurrentTradeSetBefore_NewOfferWillBeCreated()
    {
        assertNull(tradingHandler.getCurrentTrade());
        assertTrue(tradingHandler.getTradeHashMap().isEmpty());
        tradingHandler.initTradeOffer(offeredItem, requestedItemName, tradingPartner);
        assertEquals(tradingHandler.getCurrentTrade().getOfferedItem().getName(), offeredItem.getName());
        assertNotNull(tradingHandler.getTradeHashMap().get(tradingHandler.getCurrentTrade().getTimeStamp()));
        assertEquals(tradingHandler.getLastTradingPartner(), tradingPartner);
        assertTrue(tradingHandler.getCurrentTrade().isAlive());
        assertEquals(tradingHandler.buildTradeOffer(), brokerConnection2.receivedMessage);
    }

    @Test
    @DisplayName("Test initTradeOffer, current trade was set before -> player choose abort")
    public void testInitTradeOffer_CurrentTradeSetBefore_PlayerChooseAbort()
    {
        tradingHandler.setOptionPane(new MockOptionPaneNo());
        brokerConnection1.publish(new MqttMessage("test".getBytes(StandardCharsets.UTF_8)), topicTradingPartner);
        assertTrue(tradingHandler.getTradeHashMap().isEmpty());
        tradingHandler.setCurrentTrade(testTrade);
        tradingHandler.getCurrentTrade().start();
        tradingHandler.getTradeHashMap().put(testTradeTime, testTrade);
        tradingHandler.initTradeOffer(offeredItem, "item", "partner");
        assertEquals(tradingHandler.getCurrentTrade().getRequestedItem(), requestedItemName);
        assertNotNull(tradingHandler.getTradeHashMap().get(tradingHandler.getCurrentTrade().getTimeStamp()));
        assertEquals(1, tradingHandler.getTradeHashMap().size());
        assertTrue(tradingHandler.getCurrentTrade().isAlive());
        assertEquals("test", brokerConnection2.receivedMessage);
    }

    @Test
    @DisplayName("Test initTradeOffer, current trade was set before -> player choose new offer")
    public void testInitTradeOffer_CurrentTradeSetBefore_PlayerChooseNewOffer()
    {
        tradingHandler.setOptionPane(new MockOptionPaneYes());
        tradingHandler.setMessagePane(new MockMessageDialog());
        assertTrue(tradingHandler.getTradeHashMap().isEmpty());
        tradingHandler.setCurrentTrade(testTrade);
        tradingHandler.getCurrentTrade().start();
        tradingHandler.getTradeHashMap().put(testTradeTime, testTrade);
        tradingHandler.initTradeOffer(new Item("offeredItem", "", "", 10, ""), "requestedItem", tradingPartner);
        tradingHandler.setOptionPane(new MockMessageDialog());
        assertEquals(tradingHandler.getCurrentTrade().getRequestedItem(), "requestedItem");
        assertNotNull(tradingHandler.getTradeHashMap().get(tradingHandler.getCurrentTrade().getTimeStamp()));
        assertEquals(1, tradingHandler.getTradeHashMap().size());
        assertTrue(tradingHandler.getCurrentTrade().isAlive());
        String message = tradingHandler.buildTradeOffer();
        assertEquals(message, brokerConnection2.receivedMessage);
    }

    @Test
    @DisplayName("Test sendTradeMessage, message not empty -> message sent")
    public void testSendTradeMessage_MessageNotEmpty_Sent() throws InterruptedException {
        tradingHandler.setCurrentTrade(testTrade);
        String message = tradingHandler.buildTradeOffer();
        tradingHandler.sendTradeMessage(message, topicTradingPartner);
        // time span to short without sleep...
        TimeUnit.SECONDS.sleep(1);
        assertEquals(message, brokerConnection2.receivedMessage);
    }

    @Test
    @DisplayName("Test sendTradeMessage, message empty -> not sent")
    public void testSendTradeMessage_MessageEmpty_NotSent()
    {
        brokerConnection2.receivedMessage = "test";
        tradingHandler.sendTradeMessage("", topicTradingPartner);
        assertEquals("test", brokerConnection2.receivedMessage);
    }

    @Test
    @DisplayName("Test receiveOffer, player agrees and has item -> trade executed")
    public void testReceiveOffer_AgreeAndItemInInventory_TradeExecuted()
    {
        tradingHandler.setOptionPane(new MockOptionPaneYes());
        String[] input = {"", offeredItem.getName(), requestedItemName, playerName, tradingPartner, testTradeTime+""};
        Zork.game.getPlayer().inventory.addItem(new Item(requestedItemName, "", "", 10, ""));
        assertNull(tradingHandler.getCurrentTrade());
        assertTrue(tradingHandler.getTradeHashMap().isEmpty());
        tradingHandler.receiveOffer(input);
        assertEquals(tradingPartner, tradingHandler.getLastTradingPartner());
        assertEquals(requestedItemName, tradingHandler.getCurrentTrade().getOfferedItem().getName());
        assertEquals(1, tradingHandler.getTradeHashMap().size());
        assertTrue(tradingHandler.getCurrentTrade().isAlive());
        assertEquals(Constants.TRADE_ACCEPTED, brokerConnection2.receivedMessage);
    }

    @Test
    @DisplayName("Test receiveOffer, player agrees and doesn't own item -> trade aborted")
    public void testReceiveOffer_AgreeAndItemNotInInventory_TradeAborted()
    {
        tradingHandler.setOptionPane(new MockOptionPaneYes());
        tradingHandler.setMessagePane(new MockMessageDialog());
        String[] input = {"", offeredItem.getName(), requestedItemName, playerName, tradingPartner, testTradeTime+""};
        Zork.game.getPlayer().inventory.removeItem(requestedItemName);
        assertNull(tradingHandler.getCurrentTrade());
        assertTrue(tradingHandler.getTradeHashMap().isEmpty());
        tradingHandler.receiveOffer(input);
        assertNull(tradingHandler.getCurrentTrade());
        assertTrue(tradingHandler.getTradeHashMap().isEmpty());
        String message = tradingHandler.buildAbortedMessage(requestedItemName, offeredItem.getName(), "The trading partner " + input[3] +
                " does not own the requested item " + input[2] + ".");
        assertEquals(message, brokerConnection2.receivedMessage);
    }

    @Test
    @DisplayName("Test receiveOffer, player declines -> trade aborted")
    public void testReceiveOffer_Disagree_TradeAborted()
    {
        tradingHandler.setOptionPane(new MockOptionPaneNo());
        tradingHandler.setMessagePane(new MockMessageDialog());
        String[] input = {"", offeredItem.getName(), requestedItemName, playerName, tradingPartner, testTradeTime+""};
        Zork.game.getPlayer().inventory.addItem(new Item(requestedItemName, "", "", 10, ""));
        assertNull(tradingHandler.getCurrentTrade());
        assertTrue(tradingHandler.getTradeHashMap().isEmpty());
        tradingHandler.receiveOffer(input);
        assertNull(tradingHandler.getCurrentTrade());
        assertTrue(tradingHandler.getTradeHashMap().isEmpty());
        String message = tradingHandler.buildAbortedMessage(requestedItemName, offeredItem.getName(),
                "The trading partner " + input[3] + " declined your offer.");
        assertEquals(message, brokerConnection2.receivedMessage);
    }

    @Test
    @DisplayName("Test acceptCurrentTrade and executeCurrentTrade, item transfer executed")
    public void testAcceptCurrentTradeAndExecuteCurrentTrade_ItemInInventory_TradeExecuted()
    {
        assertEquals(Zork.game.getRoom("traders hut").getVisited(),1);
        Zork.game.getPlayer().inventory.addItem(testTrade.getOfferedItem());
        tradingHandler.setCurrentTrade(testTrade);
        tradingHandler.getTradeHashMap().put(testTradeTime, testTrade);
        tradingHandler.acceptCurrentTrade();
        assertNull(tradingHandler.getCurrentTrade());
        assertEquals(Constants.TRADE_ITEM_TRANSFER + ";machete;;;0;0;", brokerConnection2.receivedMessage);
    }

    @Test
    @DisplayName("Test acceptCurrentTrade, item not in inventory -> trade aborted")
    public void testAcceptCurrentTrade_ItemNotInInventory_TradeAborted()
    {
        tradingHandler.setMessagePane(new MockMessageDialog());
        assertEquals(Zork.game.getRoom("traders hut").getVisited(),1);
        Zork.game.getPlayer().inventory.removeItem(testTrade.getOfferedItem());
        assertFalse(Zork.game.getPlayer().inventory.hasItem(offeredItem));
        tradingHandler.setCurrentTrade(testTrade);
        tradingHandler.getTradeHashMap().put(testTradeTime, testTrade);
        tradingHandler.acceptCurrentTrade();
        assertNull(tradingHandler.getCurrentTrade());
        assertNull(tradingHandler.getCurrentTrade());
        assertTrue(tradingHandler.getTradeHashMap().isEmpty());
        String message = tradingHandler.buildAbortedMessage(testTrade.getOfferedItem().getName(), testTrade.getRequestedItem(),
                "The trading partner " + testTrade.getMyName() + " does not own the requested item " +
                        testTrade.getOfferedItem().getName() + ".");
        assertEquals(message, brokerConnection2.receivedMessage);

    }

    @Test
    @DisplayName("Test receiveItem")
    public void testReceiveItem()
    {
        tradingHandler.setMessagePane(new MockMessageDialog());
        Zork.game.getPlayer().inventory.removeItem(offeredItem);
        assertFalse(Zork.game.getPlayer().inventory.hasItem(offeredItem.getName()));
        String[] input = {"", offeredItem.getName(), "", "", "0", "0", ""};
        tradingHandler.receiveItem(input);
        assertTrue(Zork.game.getPlayer().inventory.hasItem(offeredItem.getName()));

    }

    @Test
    @DisplayName("Test buildTradeOffer")
    public void testBuildTradeOffer()
    {
        String expected = Constants.TRADE_OFFER + ";" + offeredItem.getName() + ";" + requestedItemName +
                ";" + tradingPartner + ";" + playerName + ";" + testTradeTime;
        tradingHandler.setCurrentTrade(testTrade);
        assertEquals(expected, tradingHandler.buildTradeOffer());
    }

    @Test
    @DisplayName("Test buildAbortedMessage")
    public void testBuildAbortedMessage()
    {
        String expected = Constants.TRADE_ABORTED + "; Trade offer ( " + requestedItemName + " for a " + offeredItem.getName() + " ) was aborted. " + "Weil halt!";
        assertEquals(expected, tradingHandler.buildAbortedMessage(requestedItemName, offeredItem.getName(), "Weil halt!"));
    }
}
