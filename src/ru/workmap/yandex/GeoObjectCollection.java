package ru.workmap.yandex;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 05.11.11
 * Time: 21:02
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class GeoObjectCollection {
    @XmlElement(name = "featureMember")
    public List<FeatureMember> featureMemberList = new ArrayList<FeatureMember>();
}
