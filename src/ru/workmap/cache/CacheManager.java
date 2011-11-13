package ru.workmap.cache;

import org.apache.log4j.Logger;
import ru.workmap.HeadHunter.HHResult;
import ru.workmap.HeadHunter.Vacancy;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 23.10.11
 * Time: 20:15
 * To change this template use File | Settings | File Templates.
 */
public class CacheManager {
    private Map<String, CacheEntity> cache = new HashMap<String, CacheEntity>();
    private static final long CACHED_TIME = 5 * 60 * 1000;
    private static final Logger log = Logger.getLogger(CacheManager.class);

    public boolean containsKey(Object key) {
        if (cache.containsKey(key)) {
            if (isEntityValid(cache.get(key))) {
                return true;
            } else {
                cache.remove(key);
            }
        }
        return false;
    }

    public HHResult get(Object key) {
        HHResult result = cache.get(key).getHhResult();
        return result;
    }

    public void put(String key, HHResult hhResult) {
        cache.put(key, new CacheEntity(hhResult));
    }

    private boolean isEntityValid(CacheEntity cacheEntity) {
        if (cacheEntity.getCreationDate().getTime() + CACHED_TIME > new Date().getTime()) {
            return true;
        }
        return false;
    }
}
