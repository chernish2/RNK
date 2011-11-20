package ru.workmap.cache;

import org.apache.log4j.Logger;
import ru.workmap.HeadHunter.Vacancy;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 20.11.11
 * Time: 18:27
 * To change this template use File | Settings | File Templates.
 */
public class RamCache implements ICache {
    private Map<String, CacheEntity> cache = new HashMap<String, CacheEntity>();
    private static final long LIFETIME = 1000 * 60 * 6; //6 hours
    private static final long MIN_FREE_MEMORY = 2048;
    private static final Logger log = Logger.getLogger(RamCache.class);

    @Override
    public boolean contains(String request) {
        if (cache.containsKey(request)) {
            Date ts = cache.get(request).getCreationDate();
            if (new Date().getTime() - ts.getTime() < LIFETIME) {
                return true;
            } else {
                cache.remove(request);
            }
        }
        return false;
    }


    @Override
    public List<Vacancy> get(String request) {
        List<Vacancy> resultList = cache.get(request).getVacancies();
        log.debug("Got " + resultList.size() + " vacancies from cache");
        return resultList;
    }


    @Override
    public void put(String request, List<Vacancy> vacancies) {
        cache.put(request, new CacheEntity(vacancies));
        log.debug("Cache size=" + cache.size() + ", " + getFreeMemory() + " kb free");
        if (getFreeMemory() < MIN_FREE_MEMORY){
            log.debug("Low memory, wiping cache...");
            cache.clear();
            log.debug(getFreeMemory() + "kb free after wiping cache");
        }
    }

    @Override
    public CacheStat getStat() {
        return new CacheStat();
    }

    private long getFreeMemory() {
        return Runtime.getRuntime().freeMemory() / 1024;
    }
}
