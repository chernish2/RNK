package ru.workmap.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 14.02.12
 * Time: 17:10
 * To change this template use File | Settings | File Templates.
 */
public class ServerListener implements ServletContextListener {
    private static QuartzUtil quartzUtil;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        quartzUtil = QuartzUtil.getInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        quartzUtil.destroy();
    }
}
