package com.fitlux.estore.common.exception;

import com.fitlux.estore.constants.serviceCodes.ServiceCode;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(ServiceCode code) {
        super(code);
    }
}
