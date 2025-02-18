package com.example.kafkabasictest.consumer.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class HistoryReceiveRoute extends RouteBuilder {
    public static final String ROUTE_ID = HistoryReceiveRoute.class.getCanonicalName();


    public void configure() throws Exception {
        from("kafka:data-history?brokers=localhost:9092&groupId=HistoryReceive")
                .routeId(ROUTE_ID)
                .unmarshal().json(JsonLibrary.Jackson)
                .marshal(new CsvDataFormat().setDelimiter(';'))
                .to("direct:SendHistory")
                .log("Received and processed History data");
    }
}