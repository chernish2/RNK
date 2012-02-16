package ru.workmap.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 05.02.12
 * Time: 17:09
 * To change this template use File | Settings | File Templates.
 */
public class DBAccessor {
    private static DBAccessor instance;
    private Connection connection;

    private DBAccessor() {

    }

    public static DBAccessor getInstance() {
        if (instance == null) {
            instance = new DBAccessor();
        }
        return instance;
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                String urlStr = "jdbc:mysql://" + Settings.getProperty(Settings.SERVER_URL);
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(urlStr, Settings.getProperty(Settings.DB_USERNAME), Settings.getProperty(Settings.DB_PASSWORD));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return connection;
    }
}
