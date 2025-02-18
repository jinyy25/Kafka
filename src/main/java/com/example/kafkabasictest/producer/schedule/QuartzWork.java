package com.example.kafkabasictest.producer.schedule;

import com.example.kafkabasictest.producer.schedule.vo.ProducerSchedule;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(
        prefix = "quartz-info"
)
public class QuartzWork {
    private final List<ProducerSchedule> list = new ArrayList();
    private String work;

    public QuartzWork() {
    }

    public List<ProducerSchedule> getList() {
        return this.list;
    }

    public String getWork() {
        return this.work;
    }

    public void setWork(String work) {
        this.work = work;
    }
}
