package ru.workmap.cache;

import org.apache.log4j.Logger;
import ru.workmap.HHSearcher;
import ru.workmap.HeadHunter.Vacancy;
import ru.workmap.util.Settings;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
    private static DBCache instance = new DBCache();
    private static final Logger log = Logger.getLogger(DBCache.class);
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
    private static long cacheExpirePeriod;

    private DBCache() {
        cacheExpirePeriod = Settings.getPropertyAsInt(Settings.CACHE_UPDATE_TIME) * 60 * 1000;
    }

    public static DBCache getInstance() {
        return instance;
    }

    @Override
    public boolean contains(String request) {
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        Query query = entityManager.createNamedQuery("getQueryEntityCountByStr");
        query.setParameter("queryStr", request);
        List<Long> resultList = query.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return resultList.get(0) > 0;
    }

    @Override
    public List<Vacancy> get(String request) {
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        Query query = entityManager.createNamedQuery("getQueryEntityByQueryStr");
        query.setParameter("queryStr", request);
        QueryEntity queryEntity = (QueryEntity) query.getResultList().get(0);
        if (queryEntity.getResultEntity().size() == 0) {
            entityManager.refresh(queryEntity);
        }
//                                                        entityManager.getTransaction().commit();
        List<Vacancy> vacancyList = new ArrayList<Vacancy>();
        for (QueryResultEntity resultEntity : queryEntity.getResultEntity()) {
            vacancyList.add(resultEntity.getVacancy());
        }
        log.debug("got " + vacancyList.size() + " vacancies from cache");
        queryEntity.setQueryCount(queryEntity.getQueryCount() + 1);
        entityManager.merge(queryEntity);
        entityManager.getTransaction().commit();
        entityManager.close();
        return vacancyList;
    }

    @Override
    public void put(String request, List<Vacancy> vacancies) {
        EntityManager entityManager = factory.createEntityManager();
        log.debug("put " + vacancies.size() + " vacancies to cache");
        entityManager.getTransaction().begin();
        storeInDB(entityManager, request, vacancies, 1);
        entityManager.getTransaction().commit();
        entityManager.close();
        log.debug("done putting to cache");
    }

    private void storeInDB(EntityManager entityManager, String request, List<Vacancy> vacancies, int count) {
        QueryEntity queryEntity = new QueryEntity();
        queryEntity.setQueryCount(count);
        queryEntity.setQueryStr(request);
        queryEntity.setQueryLastUpdated(new Timestamp(new Date().getTime()));
        entityManager.persist(queryEntity);
        for (Vacancy vacancy : vacancies) {
            QueryResultEntity queryResultEntity = new QueryResultEntity();
            queryResultEntity.setVacancy(vacancy);
            queryResultEntity.setQueryEntity(queryEntity);
            entityManager.persist(queryResultEntity);
        }
    }

    @Override
    public CacheStat getStat() {
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        Query query = entityManager.createNamedQuery("getStat");
        List<Object> resultList = query.getResultList();
        CacheStat stat = new CacheStat();
        for (Object o : resultList) {
            Object[] array = (Object[]) o;
            String key = extractKey(array[0]);
            stat.addQuery(key, (Integer) array[1]);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        stat.setStatInfo("Database cache statistics");
        return stat;
    }

    private String extractKey(Object o) {
        String key = "";
        if (o instanceof String) {
            String s = (String) o;
            String startString = "text=";
            int firstChar = s.indexOf(startString);
            s = s.substring(firstChar + startString.length());
            int lastChar = s.indexOf("&");
            s = s.substring(0, lastChar);
            try {
                key = URLDecoder.decode(s, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return key;
    }

    @Override
    public void clear() {
        EntityManager entityManager = factory.createEntityManager();
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
        entityManager.close();
    }

    @Override
    public void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EntityManager entityManager = factory.createEntityManager();
                entityManager.getTransaction().begin();
                Query query = entityManager.createNamedQuery("getQueryEntityByDate");
                Long goodTimeStart = new Date().getTime() - cacheExpirePeriod;
                Timestamp timestamp = new Timestamp(goodTimeStart);
                query.setParameter("date", timestamp);
                List<QueryEntity> queryEntityList = query.getResultList();
                log.debug("updating " + queryEntityList.size() + " records...");
                for (QueryEntity queryEntity : queryEntityList) {
                    String key = queryEntity.getQueryStr();
                    List<Vacancy> vacancyList = HHSearcher.getVacancyList(key);
                    entityManager.remove(queryEntity);
                    storeInDB(entityManager, key, vacancyList, queryEntity.getQueryCount());
                }
                entityManager.getTransaction().commit();
                entityManager.close();
                log.debug("db cache update finished");
            }
        }
        ).start();
    }

    @Override
    public List<String> getSuggestions(String input) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
