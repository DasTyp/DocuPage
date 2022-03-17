package view;

import controller.CommunicationController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;

public class ChatPanel extends JPanel {

    /**
     * Variable of the instance of the class
     */
    private static ChatPanel INSTANCE;

    /**
     * ScrollPane for the inputfield
     */
    private JScrollPane mymessageScroll;
    /**
     * TextArea for the inputfield
     */
    private JTextArea mymessage;
    /**
     * Scrollpane for the received messages
     */
    private JScrollPane receivedScroll;
    /**
     * TextArea for the received messages
     */
    private JTextPane received;
    /**
     * Panel for the buttons to change MQTT-Topic
     */
    private JPanel selection;
    /**
     * Button to change to global Topic
     */
    private JButton globalButton;
    /**
     * Button to change to Trade Topic
     */
    private JButton tradeButton;
    /**
     * Helpvariable to determin if a new line has started
     */
    private int myMessageHeight;
    /**
     * Maximum Lines of the input before scrollable
     */
    private final int maximumLinesInput = 4;
    /**
     * the starting height of the myMessage Text Area
     */
    private int myMessageStartingHeight;
    /**
     * difference of the height of the ScrollPane and the TextArea (MyMessage)
     */
    private int differenceAreaScrollPanel;
    /**
     * Helpvariable to set StartingHeigth
     */
    private boolean firstChar = true;

    /**
     * Number of chars which are currently in the JTextArea mymessage
     */
    private int charCounter = 0;

    /**
     * number of lines which are currently displayed in the JTextArea mymessage
     */
    private int lineCounter =0;

    /**
     * Array with number of chars at which a line wrap is
     */
    private int[] wrapCounter = new int[5];

    /**
     * Method to return the Instance of the ChatPanel
     *
     * @return The Instance of the class ChatPanel
     * @author Patrick Mayer
     */
    public static ChatPanel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatPanel();
        }
        return INSTANCE;
    }

    /**
     * Constructor of class ChatPanel to initialize the JPanel and the contents
     */
    private ChatPanel() {
        this.setName("chatPanel");
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        initButtons();
        initReceivedChatPanel();
        initMyMessagePanel();
    }

    /**
     * Method to update this JPanel
     */
    public void update() {
        this.updateUI();
    }

    /**
     * Method to initialize the Received Messages Panel
     */
    private void initReceivedChatPanel(){
        received = new JTextPane();
        received.setName("receivedPanel");
        received.setContentType("text/html");
        receivedScroll = new JScrollPane(received);
        receivedScroll.setName("receivedScroll");
        receivedScroll.setPreferredSize(new Dimension(0, 0));

        receivedScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        receivedScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        received.setEditable(false);

        //workaround for missing methods setLineWrap and setWrapStyleWord
        received.setMaximumSize(received.getSize());
        received.setMinimumSize(received.getSize());

        //Scroll always to bottom
        DefaultCaret caret = (DefaultCaret) received.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.add(receivedScroll);
    }

    /**
     * Method to initialize the mymessage Panel
     */
    private void initMyMessagePanel() {
        mymessage = new JTextArea();
        mymessage.setName("myMessageArea");
        mymessageScroll = new JScrollPane(mymessage);
        mymessageScroll.setName("myMessageScroll");
        mymessage.setBorder(new JTextField().getBorder());
        mymessageScroll.setBorder(BorderFactory.createEmptyBorder());
        mymessageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mymessageScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //Implement Line Wrap
        mymessage.setLineWrap(true);
        mymessage.setWrapStyleWord(true);

        mymessageScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, mymessage.getSize().height));

        mymessage.getDocument().addDocumentListener(
                new DocumentListener() {
                    /**
                     * Method is called when something is deleted
                     * @param e The removal which was done
                     */
                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        //make JTextArea smaller when chars are deleted
                        charCounter--;
                        if(charCounter==wrapCounter[lineCounter]-2 && lineCounter>0){
                            lineCounter--;
                            mymessage.setRows(mymessage.getRows()-1);
                            mymessageScroll.setMinimumSize(new Dimension(Integer.MIN_VALUE,myMessageHeight+differenceAreaScrollPanel));
                            myMessageHeight= myMessageStartingHeight*mymessage.getRows();
                            update();
                        }
                    }

                    /**
                     * Method is called when something is inserted to the textArea mymessage
                     *
                     * @param e The Update, which was done
                     */
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        charCounter++;
                        //Initialize Starting Sizes at first written char
                        if(firstChar){
                            firstChar = false;
                            myMessageStartingHeight = mymessage.getSize().height;
                            differenceAreaScrollPanel = mymessageScroll.getSize().height-myMessageStartingHeight;
                        }
                        //check if new line has started
                        if (myMessageHeight != mymessage.getHeight() && mymessage.getRows() < maximumLinesInput) {
                            mymessage.setRows(mymessage.getRows()+1);
                            myMessageHeight = mymessage.getHeight();
                            mymessageScroll.setMinimumSize(new Dimension(Integer.MIN_VALUE,myMessageHeight+differenceAreaScrollPanel));
                            update();
                            lineCounter++;
                            wrapCounter[lineCounter]=charCounter;
                        }
                    }
                    //Not Implemented
                    @Override
                    public void changedUpdate(DocumentEvent arg0) {

                    }
                });

        mymessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    //Send Message
                    CommunicationController.getInstance().publishToCurrentTopic(mymessage.getText());
                    mymessage.setText("");
                    //Reset Graphics
                    mymessage.setText("");
                    mymessage.setRows(1);
                    mymessage.setMaximumSize(new Dimension(Integer.MIN_VALUE,myMessageStartingHeight));
                    mymessageScroll.setMinimumSize(new Dimension(Integer.MIN_VALUE,myMessageStartingHeight+differenceAreaScrollPanel));
                    update();
                    myMessageHeight = myMessageStartingHeight;
                    lineCounter = 0;
                    charCounter = 0;
                }
            }
        });
        this.add(mymessageScroll);
    }

    /**
     * Method to initialize the Buttons for the MQTT-Topic
     */
    private void initButtons() {
        selection = new JPanel();
        selection.setName("selection");
        selection.setLayout(new BoxLayout(selection, BoxLayout.LINE_AXIS));

        globalButton = new JButton("global");
        globalButton.setName("global");
        tradeButton = new JButton("trading");
        tradeButton.setName("trading");
        //disable global Button because Broker is in this topic
        tradeButton.setEnabled(true);
        globalButton.setEnabled(false);

        globalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                globalButton.setEnabled(false);
                tradeButton.setEnabled(true);
                CommunicationController.getInstance().setCurrentTopic("zork4/chat/global");
            }
        });
        tradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                globalButton.setEnabled(true);
                tradeButton.setEnabled(false);
                CommunicationController.getInstance().setCurrentTopic("zork4/chat/trading");
            }
        });
        selection.add(globalButton);
        selection.add(tradeButton);
        this.add(selection);
    }

    /**
     * Method to set the receivedTextArea to the messages
     * @param messages the messages, which should be displayed
     */
    public void setMessages(String messages){
        received.setText(messages);
    }

    /**
     * Method to get the current char-number of the myMessage(input Area)
     * @return the number of chars
     */
    public int getCharCounter(){
      return charCounter;
    }


    /**
     * Method to return the current lineCounter
     * @return current lineCounter
     */
    public int getLineCounter(){
        return lineCounter;
    }

    /**
     * toggles offline mode
     * @param offline whether offline mode should be enabled or disabled
     */
    public void setOfflineMode(boolean offline) {
        if(offline){
            mymessage.setEnabled(false);
            mymessage.setBackground(Color.lightGray);
            mymessage.setText("offline :(");
        }
        else{
            mymessage.setEnabled(true);
            mymessage.setBackground(Color.white);
            mymessage.setText("");
        }

        update();
    }
}
