package com.backend.controller.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponseFactory {

    public ApiResponse success() {
        return success(null);
    }

    public ApiResponse success(final Object payload) {
        return new ApiResponse(payload);
    }

    public ApiResponse failure() {
        return new ApiResponse().setError();
    }
}