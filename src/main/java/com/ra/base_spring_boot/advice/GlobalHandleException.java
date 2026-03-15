package com.ra.base_spring_boot.advice;

import com.ra.base_spring_boot.dto.ErrorResponse;
import com.ra.base_spring_boot.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalHandleException {
    /**
     * @param ex MethodArgumentNotValidException
     * @param request HttpServletRequest
     * @apiNote handle validation exception for invalid request data (400)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return buildError(HttpStatus.BAD_REQUEST, "Validation failed", request, errors);
    }

    /**
     * @param ex MaxUploadSizeExceededException
     * @param request HttpServletRequest
     * @apiNote handle file upload size exception (400)
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * @param ex NoResourceFoundException
     * @param request HttpServletRequest
     * @apiNote handle missing resource exception (404)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * @param ex UsernameNotFoundException
     * @param request HttpServletRequest
     * @apiNote handle missing username exception (404)
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * @param ex HttpBadRequest
     * @param request HttpServletRequest
     * @apiNote handle bad request exception (400)
     */
    @ExceptionHandler(HttpBadRequest.class)
    public ResponseEntity<?> handleHttpBadReqeust(HttpBadRequest ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * @param ex HttpUnAuthorized
     * @param request HttpServletRequest
     * @apiNote handle unauthorized exception (401)
     */
    @ExceptionHandler(HttpUnAuthorized.class)
    public ResponseEntity<?> handleHttpUnAuthorized(HttpUnAuthorized ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    /**
     * @param ex HttpForbiden
     * @param request HttpServletRequest
     * @apiNote handle forbidden exception (403)
     */
    @ExceptionHandler(HttpForbiden.class)
    public ResponseEntity<?> handleHttpForbiden(HttpForbiden ex, HttpServletRequest request) {
        return buildError(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

    /**
     * @param ex HttpNotFound
     * @param request HttpServletRequest
     * @apiNote handle not found exception (404)
     */
    @ExceptionHandler(HttpNotFound.class)
    public ResponseEntity<?> handleHttpNotFound(HttpNotFound ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * @param ex HttpConflict
     * @param request HttpServletRequest
     * @apiNote handle conflict exception (409)
     */
    @ExceptionHandler(HttpConflict.class)
    public ResponseEntity<?> handleHttpConflict(HttpConflict ex, HttpServletRequest request) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * @param ex RuntimeException
     * @param request HttpServletRequest
     * @apiNote handle unexpected runtime exception (500)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        log.error("Unhandled runtime exception", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
    }


    /**
     * @param ex HttpRequestMethodNotSupportedException
     * @param request HttpServletRequest
     * @apiNote handle unsupported HTTP method exception (405)
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return buildError(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), request);
    }

    /**
     * @param ex HttpMediaTypeNotSupportedException
     * @param request HttpServletRequest
     * @apiNote handle unsupported media type exception (415)
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message, HttpServletRequest request) {
        return buildError(status, message, request, null);
    }

    private ResponseEntity<ErrorResponse> buildError(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            Map<String, String> errors
    ) {
        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .success(false)
                        .code(status.value())
                        .status(status.name())
                        .message(message)
                        .path(request.getRequestURI())
                        .timestamp(Instant.now())
                        .errors(errors)
                        .build()
        );
    }
}
