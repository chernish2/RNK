package ru.workmap.cache;

import ru.workmap.HeadHunter.Vacancy;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 23.10.11
 * Time: 20:17
 * To change this template use File | Settings | File Templates.
 */
public class CacheEntity {
    private List<Vacancy> vacancies;
    private Date creationDate;

    public CacheEntity(List<Vacancy> vacancies) {
        this.vacancies = vacancies;
        creationDate = new Date();
    }

    public List<Vacancy> getVacancies() {
        return vacancies;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
