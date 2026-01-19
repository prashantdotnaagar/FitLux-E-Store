package com.fitlux.estore.common.exception;


import com.fitlux.estore.common.response.ErrorResponse;

import com.fitlux.estore.constants.serviceCodes.ServiceCode;
import com.fitlux.estore.constants.serviceCodes.enums.serviceCodeImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse>handleBusinessException(BusinessException ex, HttpServletRequest request){
        return buildResponse(
                ex.getServiceCode(),
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
    }

    /* ===================== VALIDATION ERRORS ===================== */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                serviceCodeImpl.INVALID_REQUEST,
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                serviceCodeImpl.INVALID_REQUEST,
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
    }

    /* ===================== TYPE & ARGUMENT ERRORS ===================== */

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                serviceCodeImpl.INVALID_REQUEST,
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
    }

    /* ===================== SECURITY ERRORS ===================== */

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            UnauthorizedException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                ex.getServiceCode(),
                HttpStatus.UNAUTHORIZED,
                request.getRequestURI()
        );
    }

    /* ===================== FALLBACK (500) ===================== */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                serviceCodeImpl.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI()
        );
    }

    /* ===================== HELPER ===================== */


    private ResponseEntity<ErrorResponse>buildResponse(ServiceCode serviceCode, HttpStatus status, String path){
    ErrorResponse response = new ErrorResponse(
                serviceCode.getCode(),
                serviceCode.getMessage(),
                path,
                LocalDateTime.now()
        );
    return new ResponseEntity<>(response,status);
    }
}
