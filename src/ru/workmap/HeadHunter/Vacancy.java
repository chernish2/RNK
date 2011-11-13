package ru.workmap.HeadHunter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: workmap
 * Date: 15.10.11
 * Time: 19:56
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement(name = "vacancy")
public class Vacancy {

    private int id;
    private String name = "";
    private Region region = new Region();
    private Address address = new Address();
    private Employer employer = new Employer();
    private Salary salary = new Salary();

    public int getId() {
        return id;
    }

    @XmlAttribute()
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public Region getRegion() {
        return region;
    }

    @XmlElement
    public void setRegion(Region region) {
        this.region = region;
    }

    public Address getAddress() {
        return address;
    }

    @XmlElement
    public void setAddress(Address address) {
        this.address = address;
    }

    public Employer getEmployer() {
        return employer;
    }

    @XmlElement
    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Salary getSalary() {
        return salary;
    }

    @XmlElement
    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "id=" + id + "\nname=" + name + "\nregion" + region + "\naddress=" + address;
    }

    public String getDescription() {
        boolean hasSalary = false;
        String resStr = employer.getName() + "<br>";
        if (salary.getFrom() > 0) {
            resStr += salary.getFrom() + "-";
            hasSalary = true;
        }
        if (salary.getTo() > 0) {
            resStr += salary.getTo();
            hasSalary = true;
        }
        if (hasSalary) {
            resStr += " " + salary.getCurrency() + "<br>";
        }
        resStr += address.getStreet() + ", " + address.getBuilding() + "<br><a href=\"http://hh.ru/vacancy/" + id + "\" target=\"_blank\">Вакансия целиком</a>";
        return resStr;
    }

}
