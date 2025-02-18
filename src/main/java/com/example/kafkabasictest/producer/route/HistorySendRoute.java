package com.example.kafkabasictest.producer.route;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.kafkabasictest.producer.config.Config;
import org.apache.camel.Exchange;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;





@Component
public class HistorySendRoute extends RouteBuilder {
    public static final String HISTORY_ROUTE = HistorySendRoute.class.getCanonicalName();

    @Autowired
    private Config config;

    @Override
    public void configure() throws Exception {
        from("direct:" + HISTORY_ROUTE)
                .routeId(HISTORY_ROUTE)
                .process(this::content)
                .marshal().json(JsonLibrary.Jackson)
                .to("kafka:data-history")
                .log("Send History");
    }

    private void content(Exchange exchange) {
        Date timeline = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, Object> body = new HashMap<>();
        body.put("exchangeId", exchange.getExchangeId());
        body.put("routeId", exchange.getFromRouteId());
        body.put("agentId", config.getAgentId());
        body.put("today", dateformat.format(timeline));
        if (exchange.getProperty("CamelExceptionCaught", Exception.class) == null) {
            body.put("state", "success");
        } else {
            body.put("state", "fail");
        }

        exchange.getMessage().setBody(body);
    }
}

