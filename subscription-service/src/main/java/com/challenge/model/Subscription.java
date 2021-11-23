package com.challenge.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.*;

@Data
public class Subscription {
    @Id
    private String id;
    @NotNull
    @Email(regexp = ".*@.*\\..*")
    private String email;
    private String firstName;
    private String gender;
    @NotNull
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}")
    private String birthDate;
    @NotNull
    @AssertTrue
    private boolean consent;
    @NotBlank
    private String newsletterId;
    @Transient
    private boolean active;
}
