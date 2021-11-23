package com.challenge.service;

import com.challenge.exception.DuplicatedSubscriptionException;
import com.challenge.exception.SubscriptionNotFoundException;
import com.challenge.model.EventSubscription;
import com.challenge.model.Subscription;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Class that represent the kafka processor of the subscription service. By spring cloud kafka streams
 * consumes records from the topic of event subscriptions, persis the data in mongo, produce the created
 * subscription to kafka topic subscriptions and mail confirmation
 *
 * @author Gonzalo Collada
 * @version 1.0.0
 */
@Configuration
public class SubscriptionKafkaProcessor {

    @Autowired
    private SubscriptionServiceProcessor subscriptionServiceProcessor;

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionKafkaProcessor.class);

    @Bean
    public KafkaTemplate<String, Subscription> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public Consumer<KStream<String, EventSubscription>> subscriptionProcessor() {
        return kstream -> kstream.foreach((key, domain) -> {
            Subscription subscription = null;
            if(domain.getAction().equals("CREATE")){
                try {
                    subscription = subscriptionServiceProcessor.createOrUpdateSubscription(domain.getSubscription());
                    subscription.setActive(true);
                    kafkaTemplate().send("mail-confirmation", subscription);
                    LOG.info(String.format("Subscription created with id: %s",subscription.getId()));
                    kafkaTemplate().send("subscriptions", subscription);
                } catch (DuplicatedSubscriptionException e) {
                    LOG.error(e.getMessage());
                }
            }else if(domain.getAction().equals("DELETE")){
                try {
                    subscription = subscriptionServiceProcessor.deleteSubscription(key);
                    subscription.setActive(false);
                    LOG.info(String.format("Subscription removed with id: %s",subscription.getId()));
                    kafkaTemplate().send("subscriptions", subscription);
                } catch (SubscriptionNotFoundException e) {
                    LOG.error(e.getMessage());
                }
            }
        });
    }

    @Bean
    public ProducerFactory<String, Subscription> producerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                System.getenv().getOrDefault("KAFKA_HOST_1","localhost:9092"));
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                System.getenv().getOrDefault("KAFKA_HOST_2","localhost:9094"));
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                System.getenv().getOrDefault("KAFKA_HOST_3","localhost:9095"));
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs);
    }

}
