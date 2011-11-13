package ru.workmap.HeadHunter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: workmap
 * Date: 15.10.11
 * Time: 22:09
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement (name = "address")
public class Address {

    private Double longitude;
    private Double latitude;
    private String city = "";
    private String street = "";
    private String building = "";

    public Double getLongitude() {
        return latitude;
    }

    @XmlElement(name = "lng")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return longitude;
    }

    @XmlElement(name = "lat")
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    @XmlElement(name = "city")
    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    @XmlElement(name = "street")
    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    @XmlElement(name = "building")
    public void setBuilding(String building) {
        this.building = building;
    }

    @Override
    public String toString(){
        return longitude + ":" + latitude + ":" + city + ":" + street + ":" + building + "\n";
    }

    public boolean hasCoordinates(){
        return (latitude != null && longitude != null);
    }

    public boolean hasCityAddress(){
        return (city.length() > 0 || street.length() > 0);
    }

    public String getAddress(){
        return city + " " + street + " " + building;
    }
}
