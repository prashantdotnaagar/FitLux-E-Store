package com.fitlux.estore.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1) // Run after RequestIdFilter
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
    private static final int MAX_PAYLOAD_LENGTH = 1000;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, MAX_PAYLOAD_LENGTH);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            
            // Log Request
            String requestBody = getStringValue(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
            logger.info("REQUEST: method={} uri={} body={}", 
                    request.getMethod(), request.getRequestURI(), sanitize(requestBody));

            // Log Response
            String responseBody = getStringValue(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());
            logger.info("RESPONSE: status={} duration={}ms body={}", 
                    response.getStatus(), duration, responseBody);

            // Important: Copy content back to original response
            responseWrapper.copyBodyToResponse();
        }
    }

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            if (contentAsByteArray.length == 0) {
                return "";
            }
            int length = Math.min(contentAsByteArray.length, MAX_PAYLOAD_LENGTH);
            return new String(contentAsByteArray, 0, length, characterEncoding != null ? characterEncoding : StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return "[Error reading content]";
        }
    }
    
    private String sanitize(String body) {
        if (body == null || body.isEmpty()) {
            return body;
        }
        // Simple sanitization for password fields in JSON
        return body.replaceAll("(\"password\"\\s*:\\s*\")[^\"]*(\")", "$1******$2");
    }
}
