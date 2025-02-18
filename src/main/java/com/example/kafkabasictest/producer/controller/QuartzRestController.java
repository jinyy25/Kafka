package com.example.kafkabasictest.producer.controller;

import com.example.kafkabasictest.producer.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.quartz.SchedulerException;

@RestController
public class QuartzRestController {
    @Autowired
    private ScheduleService scheduleService;

    public QuartzRestController() {
    }

    @RequestMapping({"/runOnce"})
    public void runOnce(String name, String group) throws SchedulerException {
        this.scheduleService.runOnce(name, group);
    }

    @RequestMapping({"/pause"})
    public void pause(String name, String group) throws SchedulerException {
        this.scheduleService.pause(name, group);
    }

    @RequestMapping({"/resume"})
    public void resume(String name, String group) throws SchedulerException {
        this.scheduleService.resume(name, group);
    }

    @RequestMapping({"/delete"})
    public void delete(String name, String group) throws SchedulerException {
        this.scheduleService.delete(name, group);
    }
}
