package net.blog.dev.gestion.stocks.middle.beans;

import java.time.Duration;

/**
 * Created by romainn on 31/10/2014.
 */
public class StrategyBean {

    private String strategy;

    private Float benefitsBuy = 0f;

    private Float benefitsAverageBuy = 0f;

    private Float benefitsSell = 0f;

    private Float benefitsAverageSell = 0f;

    private Duration durationBuy = Duration.ofNanos(1);

    private Duration durationSell = Duration.ofNanos(1);

    private Integer nbrBuy = 0;

    private Integer nbrSell = 0;

    private Integer positionRunning = 0;

    private Float moneyRunning = 0f;

    private Float dividends = 0f;

    public Float getDividends() {
        return dividends;
    }

    public void setDividends(Float dividends) {
        this.dividends = dividends;
    }

    public Integer getNbrSell() {
        return nbrSell;
    }

    public void setNbrSell(Integer nbrSell) {
        this.nbrSell = nbrSell;
    }

    public Integer getNbrBuy() {
        return nbrBuy;
    }

    public void setNbrBuy(Integer nbrBuy) {
        this.nbrBuy = nbrBuy;
    }

    public Duration getDurationBuy() {
        return durationBuy;
    }

    public void setDurationBuy(Duration durationBuy) {
        this.durationBuy = durationBuy;
    }

    public Float getBenefitsAverageSell() {
        return benefitsAverageSell;
    }

    public void setBenefitsAverageSell(Float benefitsAverageSell) {
        this.benefitsAverageSell = benefitsAverageSell;
    }

    public Float getBenefitsSell() {
        return benefitsSell;
    }

    public void setBenefitsSell(Float benefitsSell) {
        this.benefitsSell = benefitsSell;
    }

    public Float getBenefitsAverageBuy() {
        return benefitsAverageBuy;
    }

    public void setBenefitsAverageBuy(Float benefitsAverageBuy) {
        this.benefitsAverageBuy = benefitsAverageBuy;
    }

    public Float getBenefitsBuy() {
        return benefitsBuy;
    }

    public void setBenefitsBuy(Float benefitsBuy) {
        this.benefitsBuy = benefitsBuy;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public Integer getPositionRunning() {
        return positionRunning;
    }

    public void setPositionRunning(Integer positionRunning) {
        this.positionRunning = positionRunning;
    }

    public Float getMoneyRunning() {
        return moneyRunning;
    }

    public void setMoneyRunning(Float moneyRunning) {
        this.moneyRunning = moneyRunning;
    }

    public Duration getDurationSell() {
        return durationSell;
    }

    public void setDurationSell(Duration durationSell) {
        this.durationSell = durationSell;
    }
}
