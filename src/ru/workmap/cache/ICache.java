package ru.workmap.cache;

import ru.workmap.HeadHunter.Vacancy;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 20.11.11
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
public interface ICache {
    public boolean contains(String request);

    public List<Vacancy> get(String request);

    public void put(String request, List<Vacancy> vacancies);

    public CacheStat getStat();

    public void clear();

    public void update();
}
