package com.fitlux.estore.constants;

import com.fitlux.estore.constants.serviceCodes.ServiceCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private String code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(ServiceCode serviceCode, T data) {
        return new ApiResponse<>(
                serviceCode.getCode(),
                serviceCode.getMessage(),
                data
        );
    }

    public static <T> ApiResponse<T> error(ServiceCode serviceCode) {
        return new ApiResponse<>(
                serviceCode.getCode(),
                serviceCode.getMessage(),
                null
        );
    }
}

