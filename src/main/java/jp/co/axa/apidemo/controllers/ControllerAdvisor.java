package jp.co.axa.apidemo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice(annotations = {RestController.class})
public class ControllerAdvisor {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity handleBindingErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        Map<String, Object> entity = new LinkedHashMap<>();
        entity.put("errors", errors);
        return new ResponseEntity(entity, HttpStatus.BAD_REQUEST);
    }
}
