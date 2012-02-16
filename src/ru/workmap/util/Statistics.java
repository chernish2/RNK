package ru.workmap.util;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: a.chernysh
 * Date: 31.01.12
 * Time: 10:53
 * To change this template use File | Settings | File Templates.
 */
public class Statistics {
    private static long hits;
    private static long searches;
    private static Date startTime = new Date();

    public static void hit(){
        hits++;
    }

    public static void search(){
        searches++;
    }

    public static long getHits() {
        return hits;
    }

    public static long getSearches() {
        return searches;
    }

    public static Date getStartTime() {
        return startTime;
    }
}
