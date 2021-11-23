package com.challenge.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {

    private String message;
    private String detailMessage;
    private List<DetailErrorDto> details;


    public ErrorResponseDto(String message){
        super();
        this.message = message;
    }

    public ErrorResponseDto(String message, String detailMessage){
        super();
        this.message = message;
        this.detailMessage = detailMessage;
    }
}
