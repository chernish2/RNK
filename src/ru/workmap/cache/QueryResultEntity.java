package ru.workmap.cache;

import ru.workmap.HeadHunter.Vacancy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 08.02.12
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "query_result", schema = "", catalog = "workmap")
@Entity(name = "ru.workmap.cache.QueryResultEntity")
public class QueryResultEntity implements Serializable{
    private int idqueryResult;
    private Vacancy vacancy;
    private QueryEntity queryEntity;

    @javax.persistence.Column(name = "idquery_result", unique = true)
    @Id @GeneratedValue
    public int getIdqueryResult() {
        return idqueryResult;
    }

    public void setIdqueryResult(int idqueryResult) {
        this.idqueryResult = idqueryResult;
    }

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "vacancy_id")
    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "query_id")
    public QueryEntity getQueryEntity() {
        return queryEntity;
    }

    public void setQueryEntity(QueryEntity queryEntity) {
        this.queryEntity = queryEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryResultEntity that = (QueryResultEntity) o;

        if (idqueryResult != that.idqueryResult) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = idqueryResult;
//        result = 31 * result + vacancyId;
        return result;
    }
}
