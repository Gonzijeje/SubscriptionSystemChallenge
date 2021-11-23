package com.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    private String id;
    private String email;
    private String firstName;
    private String gender;
    private String birthDate;
    private boolean consent;
    private String newsletterId;
    private boolean active;

}
