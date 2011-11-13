package ru.workmap.HeadHunter;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 22.10.11
 * Time: 23:10
 * To change this template use File | Settings | File Templates.
 */
public class Salary {
    private int from;
    private int to;
    private String currency;

    public int getFrom() {
        return from;
    }

    @XmlElement
    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    @XmlElement
    public void setTo(int to) {
        this.to = to;
    }

    public String getCurrency() {
        return currency;
    }

    @XmlElement
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
