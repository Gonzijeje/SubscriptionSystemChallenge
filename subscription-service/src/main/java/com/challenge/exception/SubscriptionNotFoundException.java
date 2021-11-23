package com.challenge.exception;

public class SubscriptionNotFoundException extends Exception{

    public SubscriptionNotFoundException(){
        super();
    }

    public SubscriptionNotFoundException(String id){
        super(String.format("Subscription with id: %s could not be found",id));
    }
}
