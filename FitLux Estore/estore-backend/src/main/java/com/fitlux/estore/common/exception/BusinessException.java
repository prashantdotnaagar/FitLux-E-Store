package com.fitlux.estore.common.exception;

import com.fitlux.estore.constants.serviceCodes.ServiceCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final ServiceCode serviceCode;
    public BusinessException(ServiceCode serviceCode){
        super(serviceCode.getMessage());
        this.serviceCode=serviceCode;
    }
}
