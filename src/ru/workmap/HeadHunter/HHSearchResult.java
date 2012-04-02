package ru.workmap.HeadHunter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 01.04.12
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class HHSearchResult {
    private List<Vacancy> vacancyList;
    private int foundTotal;
    private int foundForMap;

    public List<Vacancy> getVacancyList() {
        return vacancyList;
    }

    public void setVacancyList(List<Vacancy> vacancyList) {
        this.vacancyList = vacancyList;
    }

    public int getFoundTotal() {
        return foundTotal;
    }

    public void setFoundTotal(int foundTotal) {
        this.foundTotal = foundTotal;
    }

    public int getFoundForMap() {
        return foundForMap;
    }

    public void setFoundForMap(int foundForMap) {
        this.foundForMap = foundForMap;
    }
}
