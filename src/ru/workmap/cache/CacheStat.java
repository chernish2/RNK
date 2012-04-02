package ru.workmap.cache;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 20.11.11
 * Time: 18:13
 * To change this template use File | Settings | File Templates.
 */
public class CacheStat {
    private String statInfo;
    private Map<String, Integer> statMap = new HashMap<String, Integer>();
    private ValueComparator vc = new ValueComparator(statMap);

    public void addQuery(String query, int count) {
        statMap.put(query, count);
    }

    private Map<String, Integer> getSortedMap() {
        Map<String, Integer> sortedMap = new TreeMap<String, Integer>(vc);
        sortedMap.putAll(statMap);
        return sortedMap;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Map<String, Integer> map = getSortedMap();
        for (String key : map.keySet()) {
            sb.append(key)
                    .append(" : ")
                    .append(map.get(key))
                    .append("<br>");
        }
        return sb.toString();
    }

    public String getStatInfo() {
        return statInfo;
    }

    public void setStatInfo(String statInfo) {
        this.statInfo = statInfo;
    }
}

class ValueComparator implements Comparator {

    private Map base;

    public ValueComparator(Map base) {
        this.base = base;
    }

    public int compare(Object a, Object b) {
        int x = (Integer) base.get(a);
        int y = (Integer) base.get(b);
        if (x < y) {
            return 1;
        } else if (x == y) {
            return 0;
        } else {
            return -1;
        }
    }
}
