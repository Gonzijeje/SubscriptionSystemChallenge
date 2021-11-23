package com.challenge.exception;

import org.apache.kafka.common.errors.TimeoutException;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exceptioj handler for REST requests in the application
 * <p>
 * Catch the different exceptions thrown by the services and return
 * the ErrorResponse showing the error on REST response
 * </p>
 *
 * @author Gonzalo Collada
 * @version 1.0.0
 */
@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceExceptionHandler.class);


    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundExceptions(SubscriptionNotFoundException exception) {

        ErrorResponseDto errorResponse = new ErrorResponseDto(exception.getMessage());

        LOG.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidStateStoreException.class)
    public ResponseEntity<ErrorResponseDto> handleKafkaExceptions(InvalidStateStoreException exception) {

        String errorMessage = "Data could not be retrieved from local sotorage in Kafka broker";
        ErrorResponseDto errorResponse = new ErrorResponseDto(errorMessage, exception.getMessage());

        LOG.error(errorMessage, exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponseDto> handleKafkaExceptions(TimeoutException exception) {

        String errorMessage = "Time out after trying to send data to kafka topic";
        ErrorResponseDto errorResponse = new ErrorResponseDto(errorMessage, exception.getMessage());

        LOG.error(errorMessage, exception);
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleKafkaExceptions(AuthenticationException exception) {

        String errorMessage = "Need authentication to access the resource";
        ErrorResponseDto errorResponse = new ErrorResponseDto(errorMessage, exception.getMessage());

        LOG.error(errorMessage, exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(Exception exception) {

        String errorMessage = "Error processing the request: ".concat(exception.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(errorMessage);

        LOG.error(errorMessage, exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        //Get all errors
        List<DetailErrorDto> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    DetailErrorDto detailErrorDto = new DetailErrorDto(error.getDefaultMessage(), error.getField());
                    return detailErrorDto;
                })
                .collect(Collectors.toList());
        List<DetailErrorDto> globalErrors = ex.getBindingResult().getGlobalErrors().stream()
                .map(error -> {
                    DetailErrorDto detailErrorDto = new DetailErrorDto();
                    detailErrorDto.setErrorCode(error.getDefaultMessage());
                    return detailErrorDto;
                })
                .collect(Collectors.toList());

        if (globalErrors != null && !globalErrors.isEmpty()) {
            errors.addAll(globalErrors);
        }
        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setMessage("Validation errors on request");
        errorResponse.setDetails(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }

}
