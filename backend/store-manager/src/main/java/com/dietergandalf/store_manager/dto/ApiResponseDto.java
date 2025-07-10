package com.dietergandalf.store_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseDto<T> {
    private boolean success;
    private String message;
    private T data;
    private String error;
    
    public static <T> ApiResponseDto<T> success(T data) {
        return ApiResponseDto.<T>builder()
                .success(true)
                .data(data)
                .build();
    }
    
    public static <T> ApiResponseDto<T> success(T data, String message) {
        return ApiResponseDto.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }
    
    public static <T> ApiResponseDto<T> error(String error) {
        return ApiResponseDto.<T>builder()
                .success(false)
                .error(error)
                .build();
    }
    
    public static <T> ApiResponseDto<T> error(String error, String message) {
        return ApiResponseDto.<T>builder()
                .success(false)
                .error(error)
                .message(message)
                .build();
    }
}
