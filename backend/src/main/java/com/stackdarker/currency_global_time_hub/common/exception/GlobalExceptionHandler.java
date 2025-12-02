package com.stackdarker.currency_global_time_hub.common.exception;

// global exception handler for REST controllers

import com.stackdarker.currency_global_time_hub.common.api.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // external API failures (Frankfurter, World Bank, Weather, etc...)
    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ApiErrorResponse> handleExternalApiException(
            ExternalApiException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;

        ApiErrorResponse body = ApiErrorResponse
                .builder(status.value(), "External service error")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("EXTERNAL_API_ERROR")
                .service("external-api")
                .build();

        return ResponseEntity.status(status).body(body);
    }

    // bean validation on @RequestParam, @PathVariable....
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String msg = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .collect(Collectors.joining("; "));

        ApiErrorResponse body = ApiErrorResponse
                .builder(status.value(), "Validation failed")
                .message(msg)
                .path(request.getRequestURI())
                .errorCode("VALIDATION_ERROR")
                .build();

        return ResponseEntity.status(status).body(body);
    }

    // bean validation on @RequestBody
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String msg = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(f -> f.getField() + " " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ApiErrorResponse body = ApiErrorResponse
                .builder(status.value(), "Validation failed")
                .message(msg)
                .path(request.getDescription(false).replace("uri=", ""))
                .errorCode("VALIDATION_ERROR")
                .build();

        return new ResponseEntity<>(body, headers, status);
    }

    // bad JSON / unreadable request body
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        ApiErrorResponse body = ApiErrorResponse
                .builder(status.value(), "Malformed JSON request")
                .message(ex.getMostSpecificCause() != null
                        ? ex.getMostSpecificCause().getMessage()
                        : ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .errorCode("MALFORMED_JSON")
                .build();

        return new ResponseEntity<>(body, headers, status);
    }

    // fallback: unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiErrorResponse body = ApiErrorResponse
                .builder(status.value(), "Internal server error")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("INTERNAL_ERROR")
                .build();

        return ResponseEntity.status(status).body(body);
    }
}
