package com.fitlux.estore.constants;

import com.fitlux.estore.constants.serviceCodes.ServiceCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.MDC;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private String code;
    private String requestId;
    private T data;
    private String message;

    public static <T> ApiResponse<T> success(ServiceCode serviceCode, T data) {
        return new ApiResponse<>(
                serviceCode.getCode(),
                MDC.get("requestId"),
                data,
                serviceCode.getMessage()
        );
    }

    public static <T> ApiResponse<T> error(ServiceCode serviceCode) {
        return new ApiResponse<>(
                serviceCode.getCode(),
                MDC.get("requestId"),
                null,
                serviceCode.getMessage()
        );
    }
}

