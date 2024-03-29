package ru.workmap.HeadHunter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: workmap
 * Date: 15.10.11
 * Time: 19:56
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement(name = "vacancy")

@Table(name = "vacancy", schema = "", catalog = "workmap")
@Entity
public class Vacancy implements Serializable {

    private int id;
    private int xmlId;
    private String name = "";
    private Region region = new Region();
    private Address address = new Address();
    private Employer employer = new Employer();
    private Salary salary = new Salary();

    public static final int TYPE_EXACT = 0;
    public static final int TYPE_OVERALL = 1;

    private int type;

    @Id
    @Column(name = "vacancy_id")
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "vacancy_xmlid")
    public int getXmlId() {
        return xmlId;
    }

    @XmlAttribute(name = "id")
    public void setXmlId(int xmlId) {
        this.xmlId = xmlId;
    }

    @javax.persistence.Column(name = "vacancy_name")
    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public Region getRegion() {
        return region;
    }

    @XmlElement
    public void setRegion(Region region) {
        this.region = region;
    }

    @OneToOne(cascade = {CascadeType.ALL}/*, orphanRemoval = true*/)
    @JoinColumn(name = "vacancy_address_id")
    public Address getAddress() {
        return address;
    }

    @XmlElement
    public void setAddress(Address address) {
        this.address = address;
    }

    @Transient
    public Employer getEmployer() {
        return employer;
    }

    @XmlElement
    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "vacancy_salary_id")
    public Salary getSalary() {
        return salary;
    }

    @XmlElement
    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "id=" + xmlId + "\nname=" + name + "\nregion" + region + "\naddress=" + address;
    }

    @Transient
    public String getVacancyUrl() {
        if (xmlId != 0) {
            return "<br><a href=\"http://hh.ru/vacancy/" + xmlId + "\" target=\"_blank\">Вакансия целиком</a>";
        } else {
            return "";
        }
    }

    @Transient
    public String getNameUrl() {
        if (xmlId == 0) {
            return name;
        } else {
            return "<br><a href=\"http://hh.ru/vacancy/" + xmlId + "\" target=\"_blank\">" + name + "</a>";
        }
    }

    @Transient
    public String getDescription() {
        boolean hasSalary = false;
        StringBuilder description = new StringBuilder();
        if (employer.getName() != null) {
            description.append(employer.getName() + "<br>");
        }
        if (salary.getFrom() > 0) {
            description.append(salary.getFrom() + "-");
            hasSalary = true;
        }
        if (salary.getTo() > 0) {
            description.append(salary.getTo());
            hasSalary = true;
        }
        if (hasSalary && salary.getCurrency() != null) {
            description.append(" " + salary.getCurrency() + "<br>");
        }
        if (address.getStreet().length() > 0) {
            description.append(address.getStreet());
        }
        if (address.getBuilding().length() > 0) {
            description.append(", " + address.getBuilding());
        }
//        description.append(getVacancyUrl());
        return description.toString();
    }

    @Transient
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vacancy vacancy = (Vacancy) o;

        if (id != vacancy.id) return false;
        if (type != vacancy.type) return false;
        if (xmlId != vacancy.xmlId) return false;
        if (address != null ? !address.equals(vacancy.address) : vacancy.address != null) return false;
        if (employer != null ? !employer.equals(vacancy.employer) : vacancy.employer != null) return false;
        if (name != null ? !name.equals(vacancy.name) : vacancy.name != null) return false;
        if (region != null ? !region.equals(vacancy.region) : vacancy.region != null) return false;
        if (salary != null ? !salary.equals(vacancy.salary) : vacancy.salary != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + xmlId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (employer != null ? employer.hashCode() : 0);
        result = 31 * result + (salary != null ? salary.hashCode() : 0);
        result = 31 * result + type;
        return result;
    }
}
