package com.example.kafkabasictest.producer.schedule;

import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;

import com.example.kafkabasictest.producer.schedule.vo.ProducerSchedule;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduleManage {

    @Autowired
    private QuartzWork quartzWork;

    @Autowired
    private Scheduler scheduler;


    @PostConstruct
    public void createTrigger() throws SchedulerException {
        List<ProducerSchedule> list = this.quartzWork.getList();
        Iterator var3 = list.iterator();

        while(var3.hasNext()) {
            ProducerSchedule ps = (ProducerSchedule)var3.next();
            String jobId = ps.getScheduleName();
            String jobGroup = this.quartzWork.getWork();
            TriggerKey.triggerKey(jobId, jobGroup);
            JobDetail job = JobBuilder.newJob(ScheduleJob.class).withIdentity(jobId, jobGroup).withDescription(ps.getScheduleDesc()).build();
            CronTrigger cronTrigger = (CronTrigger)TriggerBuilder.newTrigger().withIdentity(ps.getTriggerName(), jobGroup).withDescription(ps.getTriggerDesc()).withSchedule(CronScheduleBuilder.cronSchedule(ps.getCron()).withMisfireHandlingInstructionDoNothing()).build();
            if (this.scheduler.checkExists(job.getKey())) {
                this.scheduler.rescheduleJob(TriggerKey.triggerKey(jobId, jobGroup), cronTrigger);
            } else {
                this.scheduler.scheduleJob(job, cronTrigger);
            }
        }

    }
}
