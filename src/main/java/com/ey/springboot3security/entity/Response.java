package com.ey.springboot3security.entity;

public class Response<T>{
    private int status;
    private String message;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Response(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Factory methods for convenience
    public static <T> Response<T> success(T data, String message) {
        return new Response<>(200, message, data);
    }

    public static <T> Response<T> error(String message, int status) {
        return new Response<>(status, message, null);
    }
}
