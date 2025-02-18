package com.example.kafkabasictest.consumer.route;

import com.example.kafkabasictest.consumer.config.Config;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.camel.model.OnExceptionDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;

import org.apache.commons.csv.CSVFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BTypeReceiveRoute extends RouteBuilder {

    public static final String ROUTE_ID = BTypeReceiveRoute.class.getCanonicalName();

    @Autowired
    private Config config;

    @Override
    public void configure() throws Exception {
        // Completion and exception handling
        onCompletion().to("direct:ReceiveHistory");

        onException(Exception.class)
                .setProperty("failedData", simple("${body}"))
                .to("direct:ReceiveHistory")
                .to("direct:ExceptionComplete");

        // Process for RECEIVER-AGENT-1
        if (config.getAgentId().equalsIgnoreCase("RECEIVE-AGENT-1")) {
            from("kafka:data-b?groupId=BTypeConsumer1&allowManualCommit=true&autoCommitEnable=false&breakOnFirstError=true")
                    .routeId(ROUTE_ID)
                    .setProperty("agentId", constant(config.getAgentId()))
                    .unmarshal().jacksonXml() // Deserialize XML
                    .marshal(new CsvDataFormat().setFormat(CSVFormat.EXCEL)) // Marshal to CSV
                    .to("file:data-B?fileName=test.csv&fileExist=Append") // Save to file
                    .process("ManualCommitProcessor") // Commit after processing
                    .log("Receive data-b1");
        } else {
            // Process for other agents
            from("kafka:data-b?groupId=BTypeConsumer2&allowManualCommit=true&autoCommitEnable=false&breakOnFirstError=true")
                    .routeId(ROUTE_ID)
                    .setProperty("agentId", constant(config.getAgentId()))
                    .unmarshal().jacksonXml() // Deserialize XML
                    .marshal(new CsvDataFormat().setFormat(CSVFormat.EXCEL)) // Marshal to CSV
                    .to("file:data-B?fileName=test.csv&fileExist=Append") // Save to file
                    .process("ManualCommitProcessor") // Commit after processing
                    .log("Receive data-b2");
        }
    }
}