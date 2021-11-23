package com.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;

/**
 * Object DTO to represent Subscription entity
 *
 * @author Gonzalo Collada
 * @version 1.0.0
 */
@Data
public class Subscription {
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
    private boolean active;

}
