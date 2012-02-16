package ru.workmap.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 14.02.12
 * Time: 17:10
 * To change this template use File | Settings | File Templates.
 */
public class HibernateListener implements ServletContextListener{
    private static SessionFactory sessionFactory;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        sessionFactory.close();
    }
}
