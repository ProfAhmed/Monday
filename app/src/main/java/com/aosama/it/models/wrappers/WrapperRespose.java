package com.aosama.it.models.wrappers;


import com.aosama.it.models.errors.ErrorsMessages;
import com.aosama.it.models.responses.BasicResponse;

public class WrapperRespose<T> {
    private BasicResponse data;
    private ErrorsMessages errors;
    private Throwable t;

    public WrapperRespose(BasicResponse data, ErrorsMessages errors, Throwable t) {
        this.data = data;
        this.errors = errors;
        this.t = t;
    }

    public BasicResponse getData() {
        return data;
    }

    public ErrorsMessages getErrors() {
        return errors;
    }

    public Throwable getT() {
        return t;
    }
}
