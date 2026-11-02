
package com.likhitha.job_tracker_api.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(JobNotFoundException.class)
        public ResponseEntity<Map<String, Object>> handleJobNotFoundException(
                        JobNotFoundException exception,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                HttpStatus.NOT_FOUND,
                                exception.getMessage(),
                                request);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidationErrors(
                        MethodArgumentNotValidException exception,
                        HttpServletRequest request) {

                Map<String, Object> response = buildErrorBody(
                                HttpStatus.BAD_REQUEST,
                                "Validation failed",
                                request);

                Map<String, String> fieldErrors = new HashMap<>();

                exception.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> fieldErrors.put(
                                                error.getField(),
                                                error.getDefaultMessage()));

                response.put("fieldErrors", fieldErrors);

                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<Map<String, Object>> handleResponseStatusException(
                        ResponseStatusException exception,
                        HttpServletRequest request) {

                HttpStatus status = HttpStatus.valueOf(
                                exception.getStatusCode().value());

                String message = exception.getReason() != null
                                ? exception.getReason()
                                : status.getReasonPhrase();

                return buildErrorResponse(status, message, request);
        }

        @ExceptionHandler({
                        HttpMessageNotReadableException.class,
                        MethodArgumentTypeMismatchException.class
        })
        public ResponseEntity<Map<String, Object>> handleBadRequest(
                        Exception exception,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "Invalid request format or parameter value",
                                request);
        }

        private ResponseEntity<Map<String, Object>> buildErrorResponse(
                        HttpStatus status,
                        String message,
                        HttpServletRequest request) {

                return ResponseEntity.status(status)
                                .body(buildErrorBody(status, message, request));
        }

        private Map<String, Object> buildErrorBody(
                        HttpStatus status,
                        String message,
                        HttpServletRequest request) {

                Map<String, Object> error = new HashMap<>();

                error.put("timestamp", LocalDateTime.now().toString());
                error.put("status", status.value());
                error.put("error", status.getReasonPhrase());
                error.put("message", message);
                error.put("path", request.getRequestURI());

                return error;
        }
}
