// exception/ErrorResponseDto.java
package com.example.alert.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    private int status;
    private String message;
    private Map<String, String> errors;
}