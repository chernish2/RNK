package ru.workmap.HeadHunter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 13.11.11
 * Time: 19:10
 * To change this template use File | Settings | File Templates.
 */
public class HHResult {
    private List<Vacancy> coordinatesList = new ArrayList<Vacancy>();
    private List<Vacancy> streetList = new ArrayList<Vacancy>();
    public int otherVacancies;

    public List<Vacancy> getCoordinatesList() {
        return coordinatesList;
    }

    public void setCoordinatesList(List<Vacancy> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }

    public List<Vacancy> getStreetList() {
        return streetList;
    }

    public void setStreetList(List<Vacancy> streetList) {
        this.streetList = streetList;
    }

    public int getOtherVacancies() {
        return otherVacancies;
    }

    public void setOtherVacancies(int otherVacancies) {
        this.otherVacancies = otherVacancies;
    }
}
