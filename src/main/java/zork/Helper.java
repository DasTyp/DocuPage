package zork;

/**
 * Helper Class for various additional methods
 */
public class Helper {

    /**
     * return a html color by string
     * @param str the string to get a color by
     * @return String representing a html color
     */
    public static String getColorByString(String str) {
        return Constants.HTML_COLORS[Math.abs(str.hashCode()% Constants.HTML_COLORS.length)];
    }

    /**
     * Calculates a progress bar based on a value and a maximum
     * @author Jonas Proell
     * @param value current value
     * @param max max value
     * @return a progress bar as a string
     */
    public String getProgressBar(int value, int max){
        int percent = getPercentage(value, max);
        String bar = "[";
        for (int i = 0; i < 100; i+= 10) {
            if(i < percent){
                bar += "&#9632;";
            }else{
                bar += "&#9633;";
            }
        }
        bar += "]("+value+"/"+max+")";
        return bar;
    }

    /**
     * Calculates a percentage based on a value and a maximum
     * @author Jonas Proell
     * @param value value
     * @param max max value
     * @return percentage from 0-100
     */
    public int getPercentage(int value, int max){
        return (int)((value * 100.0f) / max);
    }

}
