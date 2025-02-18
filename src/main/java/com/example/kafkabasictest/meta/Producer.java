package com.example.kafkabasictest.meta;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.Properties;

public class Producer {

    public static void main(String[] args) throws IOException {
        Properties configs = new Properties();
        configs.put("bootstrap.servers", "localhost:9092");
        configs.put("acks", "all");
        configs.put("block.on.buffer.full", "true");
        configs.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> producer = new KafkaProducer(configs);

        for(int i = 0; i < 5; ++i) {
            String v = "hello" + i;
            producer.send(new ProducerRecord("test20190715", v));
        }

        producer.flush();
        producer.close();
    }
}
