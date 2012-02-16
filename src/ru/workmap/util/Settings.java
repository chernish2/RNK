package ru.workmap.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 05.02.12
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */
public class Settings {
//    private static Settings instance;
    private static Properties properties;
    public static final String DB_USERNAME = "db_username";
    public static final String DB_PASSWORD = "db_password";
    public static final String SERVER_URL = "server_url";
    public static final String CACHE_UPDATE_TIME = "cache_update_time"; //in hours
    private static final Logger log = Logger.getLogger(Settings.class);

    static {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("..\\workmap.properties"));
        } catch (IOException e) {
            try{
            log.error(new File(".").getCanonicalPath() + "   ;  " + new File("..").getCanonicalPath());
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            log.error("Error load properties", e);
        }
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}
