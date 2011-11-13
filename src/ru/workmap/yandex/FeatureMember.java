package ru.workmap.yandex;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 05.11.11
 * Time: 21:00
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "featureMember")
public class FeatureMember {
    @XmlElement(name = "GeoObject")
    public GeoObject geoObject;
}
