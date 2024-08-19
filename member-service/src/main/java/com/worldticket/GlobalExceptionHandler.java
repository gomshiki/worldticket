package com.worldticket;

import com.worldticket.infra.AuthorizationException;
import com.worldticket.infra.DataNotFoundException;
import com.worldticket.infra.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>("Invalid argument provided : " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        return new ResponseEntity<>("Null value encountered : " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>("Runtime Exception " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred : " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*        @ExceptionHandler(MethodArgumentNotValidException.class)
            public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors().forEach(
                        error -> errors.put(error.getField(), error.getDefaultMessage())
                );
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            }
        */
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> handleAuthorizationException(AuthorizationException ex) {
        return new ResponseEntity<>("Authorization Exception: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(MemberNotFoundException ex) {
        return new ResponseEntity<>("MemberNotFound Exception : ", HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(DataNotFoundException ex) {
        return new ResponseEntity<>("DataNotFoundException : ", HttpStatus.NO_CONTENT);
    }

}
