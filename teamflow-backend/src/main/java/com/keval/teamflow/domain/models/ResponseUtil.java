package com.keval.teamflow.domain.models;

import com.keval.teamflow.dto.ApiResponse;

public class ResponseUtil {
    public static  <T>ApiResponse<T> success(T data, int statusCode,String message) {
        return ApiResponse.<T>builder()
                .status(statusCode)
                .message(message)
                .data(data)
                .build();
    }
    public static <T> ApiResponse<T> error(String message, int status) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(null)
                .build();
    }
}
