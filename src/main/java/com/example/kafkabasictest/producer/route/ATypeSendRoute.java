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
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ATypeSendRoute extends RouteBuilder {

    @Autowired
    private QuartzWork quartzWork;  // QuartzWork 주입

    public static final String ROUTE_ID = ATypeSendRoute.class.getCanonicalName();
    public static final String HISTORY_ROUTE = HistorySendRoute.class.getCanonicalName();

    @Override
    public void configure() throws Exception {
        onCompletion()
                .to("direct:" + HISTORY_ROUTE);


        onException(Exception.class)
                .to("direct:" + HISTORY_ROUTE);


        from("direct:" + ROUTE_ID)
                .routeId(ROUTE_ID)
                .process(this::follow)
                .marshal().json(JsonLibrary.Jackson)
                .to("kafka:data-a")
                .log("Send data-A ");
    }


    public void follow(Exchange exchange) {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // Quartz 설정 값 중 BTypeSendRoute 정보 가져오기
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
