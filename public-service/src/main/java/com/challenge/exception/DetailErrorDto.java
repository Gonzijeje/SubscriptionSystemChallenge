package com.challenge.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailErrorDto {

    private String errorCode;
    private String errorField;
}
