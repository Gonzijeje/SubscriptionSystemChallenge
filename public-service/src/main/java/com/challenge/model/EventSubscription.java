package com.challenge.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Object DTO to represent events of subscriptions
 *
 * These events can be CREATE/DELETE subscriptions which are sent to related kafka topic
 *
 * @author Gonzalo Collada
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
public class EventSubscription {

    private String action;
    private Subscription subscription;

}
