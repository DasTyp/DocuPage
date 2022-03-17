package view;

import javax.swing.*;
import java.awt.*;

/**
 * JPanel containing the players stats
 * @author Jonas Pröll
 */
public class StatsPanel extends JPanel {

    /**
     * instance object (singleton pattern)
     */
    private static StatsPanel INSTANCE = null;

    /**
     * progressbar showing the players current health
     */
    private JProgressBar healthBar = new JProgressBar();

    /**
     * progressbar showing the players current hunger
     */
    private JProgressBar hungerBar = new JProgressBar();

    /**
     * progressbar showing the players current thirst
     */
    private JProgressBar thirstBar = new JProgressBar();

    /**
     * progressbar showing the players current space
     */
    private JProgressBar spaceBar = new JProgressBar();

    /**
     * constructor for the StatsPanel
     * @author Jonas Pröll
     */
    private StatsPanel(){

        // set layout
        GridLayout layout = new GridLayout(4, 2, 0, 10);
        this.setLayout(layout);
        this.setMaximumSize(new Dimension(200, 100));

        // add health bar
        this.add(new JLabel("HEALTH"));
        styleJProgressBar(this.healthBar);
        this.add(this.healthBar);

        // add hunger bar
        this.add(new JLabel("HUNGER"));
        styleJProgressBar(this.hungerBar);
        this.add(this.hungerBar);

        // add thirst bar
        this.add(new JLabel("THIRST"));
        styleJProgressBar(this.thirstBar);
        this.add(this.thirstBar);

        // add space bar
        this.add(new JLabel("SPACE"));
        styleJProgressBar(this.spaceBar);
        this.add(this.spaceBar);
    }

    /**
     * get the instance of the StatsPanel (singleton pattern)
     * @return instance of the StatsPanel
     * @author Jonas Pröll
     */
    public static StatsPanel getInstance(){
        if (INSTANCE == null) INSTANCE = new StatsPanel();
        return INSTANCE;
    }

    /**
     * set the progressbar to the current health
     * @param actual current health points
     * @param max max health points
     * @author Jonas Pröll
     */
    public void setHealth(int actual, int max){
        if(actual <= 20) this.healthBar.setForeground(Color.RED);
        else this.healthBar.setForeground(Color.BLACK);
        this.healthBar.setMaximum(max);
        this.healthBar.setValue(actual);
    }

    /**
     * set the progressbar to the current hunger
     * @param actual current hunger points
     * @param max max hunger points
     * @author Jonas Pröll
     */
    public void setHunger(int actual, int max){
        if(actual < 20) this.hungerBar.setForeground(Color.RED);
        else this.hungerBar.setForeground(Color.BLACK);
        this.hungerBar.setMaximum(max);
        this.hungerBar.setValue(actual);
    }

    /**
     * set the progressbar to the current thirst
     * @param actual current thirst points
     * @param max max thirst points
     * @author Jonas Pröll
     */
    public void setThirst(int actual, int max){
        if(actual < 20) this.thirstBar.setForeground(Color.RED);
        else this.thirstBar.setForeground(Color.BLACK);
        this.thirstBar.setMaximum(max);
        this.thirstBar.setValue(actual);
    }

    /**
     * set the progressbar to the current space
     * @param actual current space
     * @param max max space
     * @author Jonas Pröll
     */
    public void setSpace(int actual, int max){
        this.spaceBar.setMaximum(max);
        this.spaceBar.setValue(actual);
    }

    /**
     * set the styling of a progressbar
     * @param bar the progressbar you want to style
     * @author Jonas Pröll
     */
    private void styleJProgressBar(JProgressBar bar){
        bar.setStringPainted(true);
    }
}


