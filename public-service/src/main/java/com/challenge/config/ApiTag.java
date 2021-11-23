package com.challenge.config;

import springfox.documentation.service.Tag;

public class ApiTag {

    public static final String SUBSCRIPTIONS = "Subscriptions";

    public static Tag[] getTags() {
        return new Tag[] {
                new Tag(SUBSCRIPTIONS, "")
        };
    }
}
