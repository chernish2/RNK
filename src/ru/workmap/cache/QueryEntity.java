package ru.workmap.cache;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 08.02.12
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Table(name = "query_table", schema = "", catalog = "workmap")
@Entity(name = "ru.workmap.cache.QueryEntity")
@NamedQueries({
        @NamedQuery(name = "getQueryEntityByQueryStr", query = "FROM ru.workmap.cache.QueryEntity q WHERE q.queryStr = :queryStr"),
        @NamedQuery(name = "getQueryEntityCountByStr", query = "SELECT COUNT(*) FROM ru.workmap.cache.QueryEntity q WHERE q.queryStr = :queryStr"),
        @NamedQuery(name = "getQueryEntityByDate", query = "SELECT q FROM ru.workmap.cache.QueryEntity q WHERE q.queryLastUpdated < :date"),
        @NamedQuery(name = "getStat", query = "SELECT q.queryStr, q.queryCount FROM ru.workmap.cache.QueryEntity q")
})
public class QueryEntity implements Serializable {
    private int queryId;
    private List<QueryResultEntity> resultEntity = new ArrayList<QueryResultEntity>();
    private String queryStr;

    @javax.persistence.Column(name = "query_id")
    @Id
    @GeneratedValue
    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "query_id")
    public List<QueryResultEntity> getResultEntity() {
        return resultEntity;
    }

    public void setResultEntity(List<QueryResultEntity> resultEntity) {
        this.resultEntity = resultEntity;
    }

    @javax.persistence.Column(name = "query_str")
    @Basic
    public String getQueryStr() {
        return queryStr;
    }

    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr;
    }

    private int queryCount;

    @javax.persistence.Column(name = "query_count")
    @Basic
    public int getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(int queryCount) {
        this.queryCount = queryCount;
    }

    private Timestamp queryLastUpdated;

    @javax.persistence.Column(name = "query_last_updated")
    @Basic
    public Timestamp getQueryLastUpdated() {
        return queryLastUpdated;
    }

    public void setQueryLastUpdated(Timestamp queryLastUpdated) {
        this.queryLastUpdated = queryLastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryEntity that = (QueryEntity) o;

        if (queryCount != that.queryCount) return false;
        if (queryId != that.queryId) return false;
        if (queryLastUpdated != null ? !queryLastUpdated.equals(that.queryLastUpdated) : that.queryLastUpdated != null)
            return false;
        if (queryStr != null ? !queryStr.equals(that.queryStr) : that.queryStr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = queryId;
        result = 31 * result + (queryStr != null ? queryStr.hashCode() : 0);
        result = 31 * result + queryCount;
        result = 31 * result + (queryLastUpdated != null ? queryLastUpdated.hashCode() : 0);
        return result;
    }
}
