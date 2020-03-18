package com.aosama.it.models.wrappers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aosama.it.models.errors.ErrorsMessages;


public class StateData<T> {

    @NonNull
    private DataStatus status;

    @Nullable
    private T data;

    @Nullable
    private ErrorsMessages errorsMessages;

    @Nullable
    private Throwable error;

    public StateData() {
        this.status = DataStatus.CREATED;
        this.data = null;
        this.error = null;
        this.errorsMessages = null;
    }

    public StateData<T> loading() {
        this.status = DataStatus.LOADING;
        this.data = null;
        this.error = null;
        this.errorsMessages = null;
        return this;
    }

    public StateData<T> success(@NonNull T data) {
        this.status = DataStatus.SUCCESS;
        this.data = data;
        this.errorsMessages = null;
        this.error = null;
        return this;
    }

    public StateData<T> fail(@NonNull ErrorsMessages errorsMessages) {
        this.status = DataStatus.FAIL;
        this.data = null;
        this.errorsMessages = errorsMessages;
        this.error = null;
        return this;
    }

    public StateData<T> error(@NonNull Throwable error) {
        this.status = DataStatus.ERROR;
        this.data = null;
        this.error = error;
        return this;
    }

    public StateData<T> complete() {
        this.status = DataStatus.COMPLETE;
        return this;
    }

    StateData<T> catchEx() {
        this.status = DataStatus.CATCH;
        return this;
    }

    @NonNull
    public DataStatus getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    @Nullable
    public ErrorsMessages getErrorsMessages() {
        return errorsMessages;
    }

    public enum DataStatus {
        CREATED,
        SUCCESS,
        ERROR,
        LOADING,
        COMPLETE,
        FAIL,
        CATCH
    }
}