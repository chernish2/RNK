package ru.workmap.HeadHunter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: workmap
 * Date: 15.10.11
 * Time: 20:18
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement(name = "result"/*, namespace = "http://hh.ru/api"*/)
public class Result extends IHH{

    @XmlElement(name = "found")
    public int found;

    @XmlElement(name = "vacancies")
    public Vacancies vacancies;

    @Override
    public Class getType() {
        return Result.class;
    }
}
