package ru.workmap.HeadHunter;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 22.10.11
 * Time: 23:08
 * To change this template use File | Settings | File Templates.
 */

public class Employer {

    private String name;

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }
}
