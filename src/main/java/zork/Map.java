package zork;

import javax.swing.*;

public class Map extends Window{

    private int width, height;
    public Map(int width, int height, String titleOfWindow) {
        super(width, height, titleOfWindow);
        setAlwaysOnTop(true);
        this.width = width;
        this.height = height;
        init();
    }

    private void init(){
        JLabel map = new JLabel();
        map.setIcon(new ImageIcon(Constants.MAP));
        JPanel panel = new JPanel();
        panel.add(map);
        panel.setBounds(0,0,width,height);
        this.add(panel);
        revalidate();
    }
}
