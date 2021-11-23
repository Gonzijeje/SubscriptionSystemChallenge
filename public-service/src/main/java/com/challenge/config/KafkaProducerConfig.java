package com.challenge.config;

import com.challenge.PublicServiceApplication;
import com.challenge.model.EventSubscription;
import com.challenge.model.Subscription;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Class of configuration for both consumer and producer in public microservice.
 * Starting the application, consumer generates a KTable storing the current
 * state of subscriptions processed by its own.
 *
 * @author Gonzalo Collada
 * @version 1.0.0
 *
 */
@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, EventSubscription> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public Consumer<KStream<String, Subscription>> publicServiceConsumer() {
        return input -> input
                // select new unique key for the kTable (email+newsletterId)
                .selectKey((key, value) -> value.getEmail().concat(value.getNewsletterId()))
                // groupBy is need before aggregation can be executed
                .groupByKey(Grouped.with(Serdes.String(), new JsonSerde<>(Subscription.class)))
                // aggregate current value and new value of the subscription object
                .reduce(
                        (aggValue, newValue) -> {
                            // check if subscription is active
                            if (newValue.isActive()){
                                return newValue;
                            }
                            // else subscription is deleted so return null
                            else {
                                return null;
                            }
                        },
                        Materialized.as(PublicServiceApplication.STORE_NAME)
                )
                .toStream();
    }

    @Bean
    public ProducerFactory<String, EventSubscription> producerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                System.getenv().getOrDefault("KAFKA_HOST_1","localhost:9092"));
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                System.getenv().getOrDefault("KAFKA_HOST_2","localhost:9094"));
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                System.getenv().getOrDefault("KAFKA_HOST_3","localhost:9095"));
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new  DefaultKafkaProducerFactory<>(configs);
    }

}