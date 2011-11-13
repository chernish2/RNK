package ru.workmap.yandex;

import ru.workmap.HeadHunter.IHH;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 05.11.11
 * Time: 20:51
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "ymaps")
public class YMaps extends IHH{
    @XmlElement(name = "GeoObjectCollection")
    public GeoObjectCollection geoObjectCollection;

    @Override
    public Class getType() {
        return YMaps.class;
    }

    public Double getLongitude(){
        if (geoObjectCollection.featureMemberList.size() > 0){
            String longitude = geoObjectCollection.featureMemberList.get(0).geoObject.point.pos.split(" ")[1];
            return Double.parseDouble(longitude);
        }
        else{
            return 0d;
        }
    }

    public Double getLatitude(){
        if(geoObjectCollection.featureMemberList.size() > 0){
            String latitude = geoObjectCollection.featureMemberList.get(0).geoObject.point.pos.split(" ")[0];
            return Double.parseDouble(latitude);
        }
        else{
            return 0d;
        }
    }
}
