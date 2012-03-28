package ru.workmap.cache;

import ru.workmap.HeadHunter.Vacancy;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 19.03.12
 * Time: 9:50
 * To change this template use File | Settings | File Templates.
 */
public class NullCache implements ICache{
    @Override
    public boolean contains(String request) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Vacancy> get(String request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void put(String request, List<Vacancy> vacancies) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CacheStat getStat() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clear() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void update() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
