package communication;

import zork.Constants;
import zork.Item;
import java.time.Instant;

/**
 * Data for single item trade
 */
public class Trade extends Thread {
    /**
     * Item the player offers to trading partner
     */
    private Item offeredItem;

    /**
     * Name of the item the player is requesting from trading partner
     */
    private String requestedItem;

    /**
     * Name of Trading Partner
     */
    private String tradingPartner;

    /**
     * My Name
     */
    private String myName;

    /**
     * Marks trade as offer
     */
    private boolean isOffer;

    /**
     * Unix time stamp of creation time
     */
    private long timeStamp;

    /**
     * Time to expire trade in milliseconds
     */
    private long expiringTime = 600000;

    public Trade(Item offeredItem, String requestedItem, String tradingPartner, boolean isOffer) {
        this.offeredItem = offeredItem;
        this.requestedItem = requestedItem;
        this.tradingPartner = tradingPartner;
        this.isOffer = isOffer;
        timeStamp = Instant.now().getEpochSecond();
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTradingPartner() {
        return tradingPartner;
    }

    public void setTradingPartner(String tradingPartner) {
        this.tradingPartner = tradingPartner;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getRequestedItem() {
        return requestedItem;
    }

    public void setRequestedItem(String requestedItem) {
        this.requestedItem = requestedItem;
    }

    public Item getOfferedItem() {
        return offeredItem;
    }

    public void setOfferedItem(Item offeredItem) {
        this.offeredItem = offeredItem;
    }

    /**
     * Start expiring timer and abort current trade if it isn't completed within expiring time
     */
    public void run() {
        try {
            sleep(expiringTime);
            TradingHandler tradingHandler = TradingHandler.getInstance();
            Trade currentTrade = tradingHandler.getCurrentTrade();
            tradingHandler.sendTradeMessage(tradingHandler.buildAbortedMessage(currentTrade.getOfferedItem().getName(), currentTrade.getRequestedItem(),
                            "Its time limit has expired!"),
                    Constants.TRADING_TOPIC + currentTrade.getTradingPartner());
            tradingHandler.abortCurrentTrade("The time limit of your trade offer has expired!\nThe offer was: "
                    + currentTrade.getOfferedItem().getName() + " for a " + currentTrade.getRequestedItem() + " with " +
                    currentTrade.getTradingPartner()+" as a trading partner.");
          } catch (InterruptedException e) {}
    }
}
