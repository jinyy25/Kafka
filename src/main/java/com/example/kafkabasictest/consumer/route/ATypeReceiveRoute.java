package com.example.kafkabasictest.consumer.route;

import org.apache.camel.component.kafka.consumer.KafkaManualCommit;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.kafkabasictest.consumer.config.Config;
import org.apache.camel.builder.RouteBuilder;

@Component
public class ATypeReceiveRoute extends RouteBuilder {

    @Autowired
    private Config config;

    public static final String ROUTE_ID = ATypeReceiveRoute.class.getCanonicalName();

    public String insertDataA1 = "INSERT INTO RECEIVE_DATA_A1 ( SCHEDULE_NAME , SCHEDULE_DESC , TRIGGER_NAME , TRIGGER_DESC , CRON , SCHEDULE_DATE) VALUES (:#scheduleName , :#scheduleDesc , :#triggerName , :#triggerDesc , :#cron , :#scheduleDate)";
    public String insertDataA2 = "INSERT INTO RECEIVE_DATA_A2 ( SCHEDULE_NAME , SCHEDULE_DESC , TRIGGER_NAME , TRIGGER_DESC , CRON , SCHEDULE_DATE) VALUES (:#scheduleName , :#scheduleDesc , :#triggerName , :#triggerDesc , :#cron , :#scheduleDate)";

    @Override
    public void configure() throws Exception {

        this.onCompletion().to("direct:ReceiveHistory");
        this.onException(Exception.class)
                .setProperty("failedData", simple("${body}"))
                .to("direct:ReceiveHistory")
                .to("direct:ExceptionComplete");

        // Route definition
        from("kafka:data-a?groupId=ATypeConsumer&allowManualCommit=true&autoCommitEnable=false&breakOnFirstError=true")
                .routeId(ROUTE_ID)
                .setProperty("agentId", constant(config.getAgentId()))
                .unmarshal().json(JsonLibrary.Jackson)
                .choice()
                .when(simple("${exchangeProperty.agentId} endsWith '1'"))
                .to("sql:" + insertDataA1 + "?batch=true")
                .otherwise()
                .to("sql:" + insertDataA2 + "?batch=true")
                .end()
                .process(exchange -> {
                    KafkaManualCommit manualCommit = exchange.getIn().getHeader("CamelKafkaManualCommit", KafkaManualCommit.class);
                    if (manualCommit != null) {
                        manualCommit.commit();
                    }
                })
                .log("Receive data-a");
    }
}
