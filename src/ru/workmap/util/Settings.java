package ru.workmap.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 05.02.12
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */
public class Settings {
    private static Properties properties;
    public static final String CACHE_UPDATE_TIME = "cache_update_time"; //in hours
    public static final String MAX_THREAD_POOL_NUMBER = "max_thread_pool_number";
    public static final String LOCALIZATION_BUNDLE = "bundle";
    private static final Logger log = Logger.getLogger(Settings.class);

    static {
        properties = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("workmap.properties");
        try {
            properties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static int getPropertyAsInt(String key){
        return Integer.parseInt(getProperty(key));
    }


}
