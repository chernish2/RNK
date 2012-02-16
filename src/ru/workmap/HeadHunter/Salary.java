package ru.workmap.HeadHunter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 22.10.11
 * Time: 23:10
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "salary", schema = "", catalog = "workmap")
@Entity
public class Salary implements Serializable{
    private int salaryId;
    private int from;
    private int to;
    private String currency;

    @javax.persistence.Column(name = "salary_id")
    @Id @GeneratedValue
    public int getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(int salaryId) {
        this.salaryId = salaryId;
    }

    @javax.persistence.Column(name = "salary_from")
    public int getFrom() {
        return from;
    }

    @XmlElement
    public void setFrom(int from) {
        this.from = from;
    }

    @javax.persistence.Column(name = "salary_to")
    public int getTo() {
        return to;
    }

    @XmlElement
    public void setTo(int to) {
        this.to = to;
    }

    @javax.persistence.Column(name = "salary_currency")
    public String getCurrency() {
        return currency;
    }

    @XmlElement
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Salary salary = (Salary) o;

        if (from != salary.from) return false;
        if (to != salary.to) return false;
        if (currency != null ? !currency.equals(salary.currency) : salary.currency != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = salaryId;
        result = 31 * result + from;
        result = 31 * result + to;
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }
}
