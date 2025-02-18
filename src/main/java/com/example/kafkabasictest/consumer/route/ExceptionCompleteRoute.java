package com.example.kafkabasictest.consumer.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.camel.model.RouteDefinition;

import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ExceptionCompleteRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        // Route to handle Exception Completion
        from("direct:ExceptionComplete")
                .process(this::error)
                .marshal(new CsvDataFormat().setDelimiter(';'))
                .to("file:pass?fileName=pass.csv&fileExist=Append")
                .setBody(simple("${header.failedData}"))
                .marshal().json(JsonLibrary.Jackson)
                .to("file:timestamp?fileName=${header.kafka.TOPIC}_offset_timestamp.json&fileExist=Append")
                .process("ManualCommitProcessor");
    }

    // Error handling logic
    public void error(Exchange exchange) {
        Map<String, Object> pass = new LinkedHashMap<>();
        pass.put("topic", exchange.getMessage().getHeader("kafka.TOPIC"));
        pass.put("offset", exchange.getMessage().getHeader("kafka.OFFSET"));
        exchange.getIn().setBody(pass);
    }
}