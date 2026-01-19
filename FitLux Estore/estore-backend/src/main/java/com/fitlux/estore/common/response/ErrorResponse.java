package com.fitlux.estore.common.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private String path;
    private LocalDateTime timestamp;

}
