package ru.workmap.HeadHunter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: workmap
 * Date: 15.10.11
 * Time: 22:09
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement(name = "address")
@javax.persistence.Table(name = "address", schema = "", catalog = "workmap")
@Entity
public class Address implements Serializable{

    private int addressId;
    private Double longitude;
    private Double latitude;
    private String city = "";
    private String street = "";
    private String building = "";

    @javax.persistence.Column(name = "address_id")
    @Id @GeneratedValue
    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    @javax.persistence.Column(name = "address_longitude")
    public Double getLongitude() {
        return longitude;
    }

    @XmlElement(name = "lng")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @javax.persistence.Column(name = "address_latitude")
    public Double getLatitude() {
        return latitude;
    }

    @XmlElement(name = "lat")
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @javax.persistence.Column(name = "address_city")
    public String getCity() {
        return city;
    }

    @XmlElement(name = "city")
    public void setCity(String city) {
        this.city = city;
    }

    @javax.persistence.Column(name = "address_street")
    public String getStreet() {
        return street;
    }

    @XmlElement(name = "street")
    public void setStreet(String street) {
        this.street = street;
    }

    @javax.persistence.Column(name = "address_building")
    public String getBuilding() {
        return building;
    }

    @XmlElement(name = "building")
    public void setBuilding(String building) {
        this.building = building;
    }

    @Override
    public String toString() {
        return longitude + ":" + latitude + ":" + city + ":" + street + ":" + building + "\n";
    }

    public boolean hasCoordinates() {
        return (latitude != null && longitude != null);
    }

    public boolean hasCityAddress() {
        return (city.length() > 0 || street.length() > 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (addressId != address.addressId) return false;
        if (building != null ? !building.equals(address.building) : address.building != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (latitude != null ? !latitude.equals(address.latitude) : address.latitude != null) return false;
        if (longitude != null ? !longitude.equals(address.longitude) : address.longitude != null) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = addressId;
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (building != null ? building.hashCode() : 0);
        return result;
    }
}
