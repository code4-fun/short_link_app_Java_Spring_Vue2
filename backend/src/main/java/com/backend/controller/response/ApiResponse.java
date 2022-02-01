package com.backend.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse {
    private Object data;
    private Status status = Status.OK;

    protected ApiResponse(Object data) {
        setData(data);
    }

    public ApiResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public ApiResponse setError() {
        this.status = Status.ERROR;
        return this;
    }

    public enum Status {
        OK,
        ERROR
    }
}