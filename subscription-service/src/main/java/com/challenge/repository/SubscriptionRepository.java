package com.challenge.repository;

import com.challenge.model.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    Subscription findByEmailAndNewsletterId(String email, String newsletterId);
}
