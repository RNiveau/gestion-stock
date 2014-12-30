/**
 *
 */
package net.blog.dev.gestion.stocks.middle.beans;

import net.blog.dev.gestion.stocks.back.StockId;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kiva
 */
public class DetailStockBean {

    private StockId id;

    private Float taxes;

    private Float taxesClose;

    private Float taxesPercentage;

    private String strategy;

    private String direction;

    private String type;

    private String account;
    private List<StopGain> stopGain;

    private Float estimateSrd;

    /**
     * Gain par dividende
     */
    private Float dividends;

    public static Integer GAIN_ATR_1 = 0;
    public static Integer GAIN_ATR_15 = 1;
    public static Integer GAIN_ATR_2 = 2;
    public static Integer STOP_ATR_1 = 3;
    public static Integer STOP_ATR_15 = 4;
    public static Integer STOP_ATR_2 = 5;

    public void addStopGain(Float price, Float percentage, Integer position) {
        if (stopGain == null)
            stopGain = new ArrayList<>();
        stopGain.add(position, new StopGain(price, percentage));
    }

    /**
     * @return the taxes
     */
    public Float getTaxes() {
        return taxes;
    }

    /**
     * @param taxes the taxes to set
     */
    public void setTaxes(Float taxes) {
        this.taxes = taxes;
    }

    public Float getDividends() {
        return dividends;
    }

    public void setDividends(Float dividends) {
        this.dividends = dividends;
    }

    public class StopGain {
        private Float price;

        private Float percentage;

        public StopGain(Float price, Float percentage) {
            super();
            this.price = price;
            this.percentage = percentage;
        }

        /**
         * @return the price
         */
        public Float getPrice() {
            return price;
        }

        /**
         * @param price the price to set
         */
        public void setPrice(Float price) {
            this.price = price;
        }

        /**
         * @return the percentage
         */
        public Float getPercentage() {
            return percentage;
        }

        /**
         * @param percentage the percentage to set
         */
        public void setPercentage(Float percentage) {
            this.percentage = percentage;
        }
    }

    /**
     * @return the strategy
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * @param strategy the strategy to set
     */
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    /**
     * @return the stopGain
     */
    public List<StopGain> getStopGain() {
        return stopGain;
    }

    /**
     * @return the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return the id
     */
    public StockId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(StockId id) {
        this.id = id;
    }

    /**
     * @return the taxesClose
     */
    public Float getTaxesClose() {
        return taxesClose;
    }

    /**
     * @param taxesClose the taxesClose to set
     */
    public void setTaxesClose(Float taxesClose) {
        this.taxesClose = taxesClose;
    }

    /**
     * @return the estimateSrd
     */
    public Float getEstimateSrd() {
        return estimateSrd;
    }

    /**
     * @param estimateSrd the estimateSrd to set
     */
    public void setEstimateSrd(Float estimateSrd) {
        this.estimateSrd = estimateSrd;
    }

    public Float getTaxesPercentage() {
        return taxesPercentage;
    }

    public void setTaxesPercentage(Float taxesPercentage) {
        this.taxesPercentage = taxesPercentage;
    }
}
