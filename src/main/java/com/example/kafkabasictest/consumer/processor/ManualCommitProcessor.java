package com.example.kafkabasictest.consumer.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.consumer.KafkaManualCommit;
import org.springframework.stereotype.Component;

@Component("ManualCommitProcessor")
public class ManualCommitProcessor implements Processor {
    public static final String NAME = "ManualCommitProcessor";

    public ManualCommitProcessor() {
    }

    public void process(Exchange exchange) throws Exception {
        KafkaManualCommit manual = (KafkaManualCommit)exchange.getMessage().getHeader("CamelKafkaManualCommit", KafkaManualCommit.class);
        if (manual == null) {
            manual = (KafkaManualCommit)exchange.getProperty("CamelKafkaManualCommit", KafkaManualCommit.class);
        }

        manual.commit();
    }
}
