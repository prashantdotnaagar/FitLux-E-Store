package com.fitlux.estore.common.exception;

import com.fitlux.estore.constants.serviceCodes.ServiceCode;

public class ResourceNotFoundException extends  BusinessException{
    public ResourceNotFoundException(ServiceCode code){
        super(code);
    }
}
