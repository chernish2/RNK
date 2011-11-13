package ru.workmap.yandex;

import ru.workmap.HeadHunter.IHH;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 05.11.11
 * Time: 20:27
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "Point")
public class Point extends IHH{


    @XmlElement
    public String pos;

    @Override
    public Class getType() {
        return Point.class;
    }
}
