package com.challenge;

import com.challenge.model.Subscription;
import com.challenge.service.EmailNotificationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

/**
 *
 * This class is in charge of listening to kafka topic and call the service to send
 * the mail notification
 *
 * @author Gonzalo Collada
 * @version 1.0.0
 *
 */
@Component
public class EmailNotificationListener {

    private static final Logger LOG = LoggerFactory.getLogger(EmailNotificationListener.class);

    @Autowired
    private EmailNotificationService emailService;

    @KafkaListener(topicPattern = "${kafka.topics.mail-confirmation}", autoStartup = "${kafka.enabled}")
    public void listenToProjectStatusChange(ConsumerRecord<String, Subscription> record) {
        try {
            emailService.sendEmail(
                    "Welcome to the newsletter",
                    record.value()
            );
        } catch (MailException e) {
            LOG.error("Could not send e-mail", e);
        }
    }
}
