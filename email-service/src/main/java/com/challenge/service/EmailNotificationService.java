package com.challenge.service;

import com.challenge.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    @Value("${spring.mail.addresses.from}")
    private String emailFromAddress;

    @Value("${spring.mail.addresses.replyTo}")
    private String emailReplyToAddress;

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String subject, Subscription content) throws MailException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(emailFromAddress);
            messageHelper.setReplyTo(emailReplyToAddress);
            messageHelper.setTo(content.getEmail());
            messageHelper.setSubject(subject);
            messageHelper.setText(String.format("Hello %s , you are now subscribed to the newsletter %s ." +
                    "We will keep you updated with the latest news.",
                    content.getFirstName() != null ? content.getFirstName() : "",content.getNewsletterId()));
        };
        emailSender.send(messagePreparator);
    }
}
