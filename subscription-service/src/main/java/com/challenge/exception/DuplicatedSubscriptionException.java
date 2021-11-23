package com.challenge.exception;

public class DuplicatedSubscriptionException extends Exception {

    public DuplicatedSubscriptionException() {
        super();
    }

    public DuplicatedSubscriptionException(String id) {
        super(String.format("You can no edit subscription with id: %s with this email and newsletterId",
                id));
    }
}
