package ru.workmap.HeadHunter;

import ru.workmap.HHSearcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 01.04.12
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
public class HHSearchResult {
    private List<Vacancy> vacancyList = Collections.EMPTY_LIST;
    private int foundTotal;
    private int foundForMap;
    private int averageSalary;

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

    public String getDetails() {
        StringBuilder sb = new StringBuilder();
        if (foundTotal > 0){
            sb.append(HHSearcher.RESOURCE_BUNDLE.getString("overall_vacancies")).append(" ").append(HHSearcher.makeVacanciesString(foundTotal));
            sb.append(", ").append(HHSearcher.RESOURCE_BUNDLE.getString("vacancies_for_map")).append(" ").append(foundForMap);
            if (averageSalary> 0) {
                sb.append(", " + HHSearcher.RESOURCE_BUNDLE.getString("average_salary")).append(" ").append(averageSalary);
            }
        }
        else{
            sb.append(HHSearcher.RESOURCE_BUNDLE.getString("zero_vacancies"));
        }
        return sb.toString();
    }

    public int getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(int averageSalary) {
        this.averageSalary = averageSalary;
    }
}
