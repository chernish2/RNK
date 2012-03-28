package ru.workmap.util;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.workmap.cache.DBUpdateJob;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 19.03.12
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public class QuartzUtil {
    private static Scheduler scheduler;
    private static final Logger log = Logger.getLogger(QuartzUtil.class);
    private static QuartzUtil instance = new QuartzUtil();

    private QuartzUtil() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = JobBuilder.newJob(DBUpdateJob.class)
                    .withIdentity("job1", "group1")
                    .build();
            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(
                            simpleSchedule()
                                    .withIntervalInMinutes(Settings.getPropertyAsInt(Settings.CACHE_UPDATE_TIME))
                                    .repeatForever()
                    )
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error(e);
        }
    }

    public static QuartzUtil getInstance() {
        return instance;
    }

    public static void destroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            log.error(e);
        }
    }
}
