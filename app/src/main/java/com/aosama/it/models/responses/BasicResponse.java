package com.aosama.it.models.responses;

public class BasicResponse<T>  {
    private String message = null;
    private T data;
    private Boolean successful;

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Boolean getSuccessful() {
        return successful;
    }
}
