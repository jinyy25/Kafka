package com.example.kafkabasictest.meta;


import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class Consumer {

    public static void main(String[] args) {
        Properties configs = new Properties();
        configs.put("bootstrap.servers", "localhost:9092");
        configs.put("session.timeout.ms", "10000");
        configs.put("group.id", "test20190715");
        configs.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        configs.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer(configs);
        consumer.subscribe(Arrays.asList("test20190715"));

        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(500L);
            Iterator var5 = records.iterator();

            while(var5.hasNext()) {
                ConsumerRecord<String, String> record = (ConsumerRecord)var5.next();
                String s = record.topic();
                if (!"test20190715".equals(s)) {
                    throw new IllegalStateException("get message on topic" + record.topic());
                }

                System.out.println((String)record.value());
            }
        }
    }
}
