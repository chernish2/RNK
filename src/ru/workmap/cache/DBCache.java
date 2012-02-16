package ru.workmap.cache;



import org.apache.log4j.Logger;
import ru.workmap.HeadHunter.Vacancy;
import ru.workmap.util.Settings;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 05.02.12
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */
public class DBCache implements ICache {
//    @PersistenceContext(name = "persistenceUnit")
    private static EntityManager entityManager;
    private static DBCache instance = new DBCache();
    private static final Logger log = Logger.getLogger(DBCache.class);
    private static final long cacheExpireMins = 1;
    private static final long cacheExpirePeriod = cacheExpireMins * 60 * 1000;

    static {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
        entityManager = factory.createEntityManager();
    }

//    private DBCache(){
//        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
//        entityManager = factory.createEntityManager();
//        factory.close();
//    }

    public static DBCache getInstance(){
        return instance;
    }


    @Override
    public boolean contains(String request) {
        Query query = entityManager.createNamedQuery("getQueryEntityCountByStr");
        query.setParameter("queryStr", request);
        List<Long> resultList = query.getResultList();
        return resultList.get(0) > 0;
    }

    @Override
    public List<Vacancy> get(String request) {
        Query query = entityManager.createNamedQuery("getQueryEntityByQueryStr");
        query.setParameter("queryStr", request);
        Object o = query.getResultList();
        QueryEntity queryEntity = (QueryEntity) query.getResultList().get(0);
        List<Vacancy> vacancyList = new ArrayList<Vacancy>();
        for(QueryResultEntity resultEntity: queryEntity.getResultEntity()){
            vacancyList.add(resultEntity.getVacancy());
        }
        log.debug("got " + vacancyList.size() + " vacancies from cache");
        return vacancyList;
    }

    @Override
    public void put(String request, List<Vacancy> vacancies) {
        log.debug("put " + vacancies.size() + " vacancies to cache");
        entityManager.getTransaction().begin();
        QueryEntity queryEntity = new QueryEntity();
        queryEntity.setQueryStr(request);
        queryEntity.setQueryLastUpdated(new Timestamp(new Date().getTime()));
        entityManager.persist(queryEntity);
        for(Vacancy vacancy:vacancies){
            QueryResultEntity queryResultEntity = new QueryResultEntity();
            queryResultEntity.setVacancy(vacancy);
            queryResultEntity.setQueryEntity(queryEntity);
//            entityManager.persist(queryResultEntity);
            entityManager.merge(queryResultEntity);
        }
//        entityManager.flush();
        entityManager.getTransaction().commit();
        log.debug("done putting to cache");
    }

    @Override
    public CacheStat getStat() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clear() {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("delete from Salary");
        query.executeUpdate();
        query = entityManager.createQuery("delete from Address");
        query.executeUpdate();
        query = entityManager.createQuery("delete from Vacancy");
        query.executeUpdate();
        query = entityManager.createQuery("delete from ru.workmap.cache.QueryEntity");
        query.executeUpdate();
        query = entityManager.createQuery("delete from ru.workmap.cache.QueryResultEntity");
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Override
    public void update() {
        Query query = entityManager.createNamedQuery("getQueryEntityByDate");
//        String s =Settings.getProperty(Settings.CACHE_UPDATE_TIME);
//        long cacheExpirePeriod = Long.parseLong(Settings.getProperty(Settings.CACHE_UPDATE_TIME)) * 60 * 60 * 1000;
        Long goodTimeStart = new Date().getTime() - cacheExpirePeriod;
        Timestamp timestamp = new Timestamp(goodTimeStart);
        query.setParameter("date", timestamp);
        Object o = query.getResultList();
        QueryEntity queryEntity = (QueryEntity) query.getResultList().get(0);
        List<Vacancy> vacancyList = new ArrayList<Vacancy>();


    }

//    protected void finalize(){
//        entityManager.close();
//    }

}
