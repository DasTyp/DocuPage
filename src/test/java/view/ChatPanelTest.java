package view;
import controller.CommunicationController;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.*;
import org.junit.jupiter.api.*;

import java.awt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;

public class ChatPanelTest{

    private static FrameFixture window;
    private static GuiMainFrame frame;
    private static ChatPanel panel;

    /**
     * Method to build gui before testing
     */
    @BeforeAll
    public static void onSetUp(){
        frame = GuiActionRunner.execute(GuiMainFrame::getInstance);
        panel = GuiActionRunner.execute(ChatPanel::getInstance);
        frame.addRight(panel);
        window = new FrameFixture(frame);
        window.show();
    }

    /**
     * Method to reset the gui before each test
     */
    @BeforeEach
    public  void reset(){
        JButtonFixture globalButton = window.button("global");
        if(globalButton.isEnabled()){
            globalButton.click();
        }
        JTextComponentFixture myMessageTest = window.textBox("myMessageArea");
        myMessageTest.setText("");
        JTextComponentFixture receivedPanel = window.textBox("receivedPanel");
        receivedPanel.setText("");

    }

    /**
     * Method to clean up the gui after the tests
     */
    @AfterAll
    public static void afterAll(){
        window.cleanUp();
    }

    /**
     * Test to see if the ChatPanel is visible
     */
    @Test
    @DisplayName("Test if ChatPanel is visible")
    public void testChatPanel(){
        JPanelFixture chatPanel = window.panel("chatPanel");
        assertNotNull(chatPanel);
        chatPanel.requireVisible();
    }

    /**
     * Tests if the Buttons are visible and change the topic when pressed
     */
    @Test
    @DisplayName("Test if Buttons to change topic work properly")
    public void testButton(){
        JButtonFixture globalButton = window.button("global");
        JButtonFixture tradeButton = window.button("trading");
        JPanelFixture selection = window.panel("selection");

        assertNotNull(globalButton);
        assertNotNull(tradeButton);
        assertNotNull(selection);

        globalButton.requireVisible();
        tradeButton.requireVisible();
        selection.requireVisible();

        assertNotNull(globalButton);
        assertNotNull(tradeButton);

        assertFalse(globalButton.isEnabled());
        assertTrue(tradeButton.isEnabled());

        globalButton.click();

        assertFalse(globalButton.isEnabled());
        assertTrue(tradeButton.isEnabled());

        tradeButton.click();

        assertTrue(globalButton.isEnabled());
        assertFalse(tradeButton.isEnabled());
        assertEquals(CommunicationController.getInstance().getCurrentTopic(),"zork4/chat/trading");

        globalButton.click();
        assertFalse(globalButton.isEnabled());
        assertTrue(tradeButton.isEnabled());
        assertEquals(CommunicationController.getInstance().getCurrentTopic(),"zork4/chat/global");
    }

    /**
     * Tests if the TextArea myMessage is visible and works properly
     */
    @Test
    @DisplayName("Test if the input textarea works properly")
    public void testMyMessage(){
        JTextComponentFixture myMessageTest = window.textBox("myMessageArea");
        JScrollPaneFixture myMessageScroll = window.scrollPane("myMessageScroll");

        assertNotNull(myMessageTest);
        assertNotNull(myMessageScroll);

        myMessageTest.requireVisible();
        myMessageScroll.requireVisible();

        myMessageTest.requireEditable();
        myMessageTest.requireEmpty();

        myMessageTest.pressAndReleaseKeys(KeyEvent.VK_T, KeyEvent.VK_E, KeyEvent.VK_S, KeyEvent.VK_T);
        myMessageTest.requireText("test");
        myMessageTest.pressAndReleaseKeys(KeyEvent.VK_ENTER);
        myMessageTest.requireEmpty();
    }

    /**
     * Test if the line wraps in the input Textarea work
     * tests if the counters for lines and chars are increased and decreased
     */
    @Test
    @DisplayName("Test if the line wraps in the input Textarea work")
    public void testLineWrapMyMessage(){
        JTextComponentFixture myMessageTest = window.textBox("myMessageArea");
        assertNotNull(myMessageTest);

        assertEquals(0, panel.getCharCounter());
        assertEquals(0,panel.getLineCounter());

        for(int i =0; i<50; i++){
            myMessageTest.pressAndReleaseKeys(KeyEvent.VK_M);
        }

        assertEquals(50, panel.getCharCounter());
        assertTrue(panel.getLineCounter()>=1);

        for(int i = 0; i<49; i++){
            myMessageTest.pressAndReleaseKeys(KeyEvent.VK_BACK_SPACE);
        }

        assertEquals(1, panel.getCharCounter());
        assertEquals(1, panel.getLineCounter());

    }

    /**
     * Tests if TextArea receivedMessages is visible and works properly
     */
    @Test
    @DisplayName("Test if the received message textarea works properly")
    public void testReceivedMessage() {
        JTextComponentFixture receivedPanel = window.textBox("receivedPanel");

        panel.setMessages("TEST");
        String actual = receivedPanel.text();
        assertEquals(actual, "<html>\n" +
                "  <head>\n" +
                "    \n" +
                "  </head>\n" +
                "  <body>\n" +
                "    TEST\n" +
                "  </body>\n" +
                "</html>\n");
    }
}