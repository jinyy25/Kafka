package com.example.kafkabasictest.producer.service;


import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.quartz.SchedulerException;
import org.quartz.JobKey;
import org.quartz.TriggerKey;


@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private Scheduler scheduler;


    public void runOnce(String name, String group) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(name, group);
        this.scheduler.triggerJob(jobKey);
    }

    public void pause(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
        this.scheduler.pauseTrigger(triggerKey);
    }

    public void resume(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
        this.scheduler.resumeTrigger(triggerKey);
    }

    public void delete(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
        this.scheduler.unscheduleJob(triggerKey);
    }
}
