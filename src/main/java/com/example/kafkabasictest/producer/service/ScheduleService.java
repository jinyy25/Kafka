package com.example.kafkabasictest.producer.service;
import org.quartz.SchedulerException;


public interface ScheduleService {
    void runOnce(String name, String group) throws SchedulerException;

    void pause(String name, String group) throws SchedulerException;

    void resume(String name, String group) throws SchedulerException;

    void delete(String name, String group) throws SchedulerException;
}
