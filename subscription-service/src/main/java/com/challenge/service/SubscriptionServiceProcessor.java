package com.challenge.service;

import com.challenge.exception.DuplicatedSubscriptionException;
import com.challenge.exception.SubscriptionNotFoundException;
import com.challenge.model.Subscription;

import java.util.List;

/**
 * Class with defined operations with subscriptions
 *
 * @author Gonzalo Collada
 * @version 1.0.0
 */
public interface SubscriptionServiceProcessor {

    Subscription createOrUpdateSubscription(Subscription subscription) throws DuplicatedSubscriptionException;

    Subscription deleteSubscription(String id) throws SubscriptionNotFoundException;

    Subscription getOneSubscription(String id) throws SubscriptionNotFoundException;

    List<Subscription> getAllSubscription();

}
