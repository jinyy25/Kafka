package com.example.kafkabasictest.producer.route;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.kafkabasictest.producer.schedule.QuartzWork;
import com.example.kafkabasictest.producer.schedule.vo.ProducerSchedule;
import org.apache.camel.Exchange;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BTypeSendRoute extends RouteBuilder {

    @Autowired
    private QuartzWork quartzWork;  // Quartz 설정 주입


    public static final String ROUTE_ID = BTypeSendRoute.class.getCanonicalName();
    public static final String HISTORY_ROUTE = HistorySendRoute.class.getCanonicalName();


    @Override
    public void configure() throws Exception {
        // Completion handling
        onCompletion().to("direct:" + HISTORY_ROUTE);

        // Exception handling
        onException(Exception.class)
                .to("direct:" + HISTORY_ROUTE);

        // Route definition
        from("direct:" + ROUTE_ID)
                .routeId(ROUTE_ID)
                .process(this::follow)
                .marshal().json()
                .to("kafka:data-b")
                .log("Send data-B");
    }


    public void follow(Exchange exchange) {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // Quartz 설정에서 해당 route의 schedule 정보 가져오기
        ProducerSchedule schedule = quartzWork.getList().stream()
                .filter(s -> s.getScheduleName().equals(ROUTE_ID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Schedule not found for " + ROUTE_ID));

        Map<String, Object> body = new HashMap<>();
        body.put("scheduleName", schedule.getScheduleName());
        body.put("scheduleDesc", schedule.getScheduleDesc());
        body.put("triggerName", schedule.getTriggerName());
        body.put("triggerDesc", schedule.getTriggerDesc());
        body.put("cron", schedule.getCron());
        body.put("scheduleDate", today);

        exchange.getMessage().setBody(body);
    }
}
