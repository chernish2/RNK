package ru.workmap.HeadHunter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: workmap
 * Date: 15.10.11
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement()
public class Region {

    private int id;
    private int parentId;
    private String name = "";
    private Region parentRegion;
    private Double longitude;
    private Double latitude;

    public int getId() {
        return id;
    }

    @XmlAttribute
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    @XmlAttribute(name = "parent")
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        Deque<String> stringQueue = new LinkedList<String>();
        Region parent = this;
        while((parent = parent.getParentRegion()) != null){
            stringQueue.add(parent.getName());

        }
        String result = "";
        String currentName;
        while((currentName = stringQueue.pollLast()) != null){
            if (nameIsValid(currentName)){
                result += currentName + " ";
            }
        }
        result += getName();
        return result;
    }

    private boolean nameIsValid(String name) {
        if (!name.contains("Другие страны") &&
                !name.contains("Московская область - ") &&
                !name.contains(" округ")){
            return true;
        }
        return false;
    }

    public Double getLongitude() {
        return longitude;
    }

    @XmlElement
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    @XmlElement
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @XmlTransient
    public Region getParentRegion() {
        return parentRegion;
    }

    public void setParentRegion(Region parentRegion) {
        this.parentRegion = parentRegion;
    }

    @XmlElement(name = "fullname")
    public String getFullName(){
        return toString();
    }
}
