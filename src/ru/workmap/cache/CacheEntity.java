package ru.workmap.cache;

import ru.workmap.HeadHunter.HHResult;
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
    private HHResult hhResult;
    private Date creationDate;

    public CacheEntity(HHResult hhResult) {
        this.hhResult = hhResult;
        creationDate = new Date();
    }

    public HHResult getHhResult() {
        return hhResult;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
