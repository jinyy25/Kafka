package com.example.kafkabasictest.consumer.route;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.kafkabasictest.consumer.config.Config;
import org.apache.camel.Exchange;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HistoryWriteRoute extends RouteBuilder {

    @Autowired
    private Config config;

    public String sendHistory = "INSERT INTO DATA_HISTORY ( CATEGORY , EXCHANGE_ID , AGENT_ID , ROUTE_ID , HISTORY_DATE , STATE) VALUES ('송신' , :#exchangeId , :#agentId , :#routeId , :#today , :#state)";
    public String receiveHistory = "INSERT INTO DATA_HISTORY ( CATEGORY , EXCHANGE_ID , AGENT_ID , ROUTE_ID , HISTORY_DATE , STATE) VALUES ('수신' , :#exchangeId , :#agentId , :#routeId , :#today , :#state)";

    @Override
    public void configure() throws Exception {

        from("direct:SendHistory")
                .to("sql:" + sendHistory + "?batch=true")
                .log("Insert Send History");

        from("direct:ReceiveHistory")
                .process(this::receiveProcess)
                .to("sql:" + receiveHistory + "?batch=true")
                .log("Insert Receive History");
    }

    public void receiveProcess(Exchange exchange) {
        Date timeline = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> body = new HashMap<>();
        body.put("exchangeId", exchange.getExchangeId());
        body.put("agentId", this.config.getAgentId());
        body.put("routeId", exchange.getFromRouteId());
        body.put("today", dateformat.format(timeline));

        if (exchange.getProperty("CamelExceptionCaught", Exception.class) == null) {
            body.put("state", "success");
        } else {
            body.put("state", "fail");
        }

        exchange.getMessage().setBody(body);
    }
}