package ru.workmap.cache;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;



/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 19.03.12
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class DBUpdateJob implements Job{
    private static final Logger log = Logger.getLogger(DBUpdateJob.class);
    private static ICache dbCache = DBCache.getInstance();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Executing quartz job");
        dbCache.update();
    }
}
