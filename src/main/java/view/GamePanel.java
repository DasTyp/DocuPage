package view;

import controller.GameController;
import zork.Constants;
import zork.Game;
import zork.Zork;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel {
    /**
     * JTextArea for the output of the game
     */
    private JTextPane gameLog = new JTextPane();

    /**
     * JTextField for the userinput
     */
    private JTextField inputField = new JTextField();

    /**
     * JScrollPane for the output of the game
     */
    private JScrollPane gameScroll = new JScrollPane(gameLog);

    /**
     * String to handle userinput
     */
    private String userInput = "";

    /**
     * String in which the hole output of the game is stored and handled to display in the JTextArea
     */
    private String gameOutput = "";

    /**
     * Instance of the class (Singleton)
     */
    private static GamePanel INSTANCE;

    /**
     * Object of the gameController
     */
    private GameController gameController;

    /**
     * Method to get an Instance of the class (Singleton)
     * @return Instance of the class
     */
    public static GamePanel getInstance(){
        if(INSTANCE == null){
            INSTANCE = new GamePanel();
        }
        return INSTANCE;
    }

    /**
     * Reset GamePanel Instance and controller after loading new or saved game
     */
    public GamePanel reset() {
        INSTANCE = null;
        this.gameController = new GameController();
        INSTANCE = new GamePanel();
        return INSTANCE;
    }

    /**
     * Constructor
     * sets gameController and initializes the input and output areas
     */
    private GamePanel(){
        this.gameController = new GameController();
        this.setName("gamePanel");
        initOutputField();
        initInputField();
    }

    /**
     * Method to initialize the output TextArea
     */
    private void initOutputField(){
        gameScroll.setName("outputScroll");
        gameScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gameScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gameScroll.setPreferredSize(new Dimension(0, 0)); // prevent resize of gamepanel (workaround)

        gameLog.setEditable(false);
        gameLog.setName("outputTextArea");
        gameLog.setContentType("text/html");

        //implement always scroll to bottom
        DefaultCaret caret = (DefaultCaret) gameLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        this.add(gameScroll);
    }

    /**
     * Method to initialize the input TextArea
     */
    private void initInputField(){

        inputField.setName("gameInput");
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
                userInput = inputField.getText();
                inputField.setText("");
                inputField.update(inputField.getGraphics());

                String feedback = "<p color=\"green\"><b>> " + userInput + "</b></p><br>";
                addToOutput(feedback);

                String output = gameController.processInput(userInput);

                if (Game.state == Constants.STATE_BLOCK || Game.state == Constants.STATE_CRITICAL_STRIKE) {
                    countDown(5, output);
                } else {
                    addToOutput(output);
                }
                addToOutput(gameController.checkAndProcessPlayersEnergy());
                Zork.game.getPlayer().updateStatsPanel();
            }
        });
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, inputField.getMinimumSize().height));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(inputField);
    }

    /**
     * getter for the userInput
     * @return the userInput
     */
    public String getUserInput() {
        return userInput;
    }

    /**
     * Method to add text to the output TextArea
     * @param output the String which should be added
     */
    public void addToOutput(String output) {
        output = output.replaceAll("\n", "<br>");

        gameOutput = gameOutput + output;
        gameLog.setText(gameOutput);
    }

    /**
     * Set (temporary) output
     * @param output new output string
     */
    public void setOutput(String output) {
        gameLog.setText(output);
        gameLog.update(gameLog.getGraphics());
        gameScroll.getVerticalScrollBar().setValue(0);
    }

    /**
     * Countdown and output for fight
     * @param length length of countdown
     * @param output String
     */
    private void countDown(int length, String output) {
        gameLog.setText("");
        setOutput(output);
        try {
            for(int i =length; i>=0;i--){
                output += "\n\n" + i;
                setOutput(output);
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {}
        addToOutput(output);
        addToOutput("\nGO!");
        Zork.game.setTimeBeforeAction(System.currentTimeMillis());
    }

    public GameController getGameController(){
        return gameController;
    }
}

