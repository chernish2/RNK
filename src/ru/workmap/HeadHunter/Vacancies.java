package ru.workmap.HeadHunter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: workmap
 * Date: 15.10.11
 * Time: 20:22
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement (name = "vacancies")
public class Vacancies {

    @XmlElement(name = "vacancy")
    public List<Vacancy> vacancyList = new ArrayList<Vacancy>();
}
