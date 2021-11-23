package com.challenge.service;

import com.challenge.exception.DuplicatedSubscriptionException;
import com.challenge.exception.SubscriptionNotFoundException;
import com.challenge.model.Subscription;
import com.challenge.repository.SubscriptionRepository;
import org.apache.kafka.connect.errors.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SubscriptionServiceProcessorImpl implements SubscriptionServiceProcessor{

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionServiceProcessorImpl.class);

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription createOrUpdateSubscription(Subscription subscription) throws DuplicatedSubscriptionException {
        Subscription oldSubscription = subscriptionRepository.findByEmailAndNewsletterId(subscription.getEmail(),
                subscription.getNewsletterId());
        if(Objects.nonNull(oldSubscription)){
            if(Objects.isNull(subscription.getId())) {
                subscription.setId(oldSubscription.getId());
            }else{
                throw new DuplicatedSubscriptionException(subscription.getId());
            }
        }
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription deleteSubscription(String id) throws SubscriptionNotFoundException {
        Subscription subscription = getOneSubscription(id);
        subscriptionRepository.delete(subscription);
        return subscription;
    }

    @Override
    public Subscription getOneSubscription(String id) throws SubscriptionNotFoundException {
        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(id);
        if(subscriptionOptional.isPresent()){
            return subscriptionOptional.get();
        }else {
            throw new SubscriptionNotFoundException(id);
        }
    }

    @Override
    public List<Subscription> getAllSubscription(){
        return subscriptionRepository.findAll();
    }
}
