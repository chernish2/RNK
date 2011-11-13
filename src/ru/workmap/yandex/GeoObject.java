package ru.workmap.yandex;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 05.11.11
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class GeoObject {
    @XmlElement(name = "Point")
    public Point point;
}
