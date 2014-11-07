package net.blog.dev.gestion.stocks.middle.beans;

import java.time.Duration;

/**
 * Created by romainn on 31/10/2014.
 */
public class StrategyBean {

    private String strategy;

    private Float benefitsBuy;

    private Float benefitsAverageBuy;

    private Float benefitsSell;

    private Float benefitsAverageSell;

    private Duration duration;

    private Integer nbrBuy;

    private Integer nbrSell;

    private Float dividends;


    public StrategyBean() {
        benefitsBuy = 0f;
        benefitsAverageBuy = 0f;
        benefitsAverageSell = 0f;
        benefitsSell = 0f;
        nbrBuy = 0;
        nbrSell = 0;
        dividends = 0f;
    }

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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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



}
