package com.challenge.model;

import lombok.Data;

@Data
public class EventSubscription {

    private String action;
    private Subscription subscription;
}
