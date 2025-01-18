package com.ceylon_fusion.payment_service.exception;

import com.ceylon_fusion.payment_service.util.StandardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<StandardResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception occurred: ", ex);
        return new ResponseEntity<>(
                new StandardResponse(400, ex.getMessage(), null),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse> handleException(Exception ex) {
        log.error("Unexpected exception occurred: ", ex);
        return new ResponseEntity<>(
                new StandardResponse(500, "An unexpected error occurred", null),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
