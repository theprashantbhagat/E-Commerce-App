package com.lcwd.electronic.store.ElectronicStore.exceptions;

import com.lcwd.electronic.store.ElectronicStore.payloads.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {

        log.info("Exception Handler Invoked!!");
        ApiResponse response = ApiResponse.builder().message(ex.getMessage()).success(true).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        Map<String,Object> response=new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error->{
            String message = error.getDefaultMessage();
           String field= ((FieldError)error).getField();
           response.put(field,message);
        });
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponse> handleBadApiRequest(BadApiRequestException ex) {

        log.info("Bad Api Request");
        ApiResponse response = ApiResponse.builder().message(ex.getMessage()).success(false).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
