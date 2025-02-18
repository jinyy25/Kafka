package com.example.kafkabasictest.producer.schedule;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ScheduleJob implements Job {

    @Autowired
    private ProducerTemplate template;

    @Autowired
    private CamelContext camelContext;


    public void execute(JobExecutionContext context) throws JobExecutionException {
        Exchange exchange = new DefaultExchange(this.camelContext);
        String routeId = context.getJobDetail().getKey().getName();
        exchange = this.template.send("direct:" + routeId, exchange);
    }
}
