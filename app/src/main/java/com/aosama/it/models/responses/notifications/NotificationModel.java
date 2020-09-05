package com.aosama.it.models.responses.notifications;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationModel {
    @SerializedName("isSeen")
    @Expose
    private boolean isSeen;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("nType")
    @Expose
    private String nType;
    @SerializedName("byName")
    @Expose
    private String byName;
    @SerializedName("paramStr")
    @Expose
    private List<String> paramStr = null;
    @SerializedName("time")
    @Expose
    private String time;   @SerializedName("timeTime")
    @Expose
    private String timeTime;

    public String getTimeTime() {
        return timeTime;
    }

    public void setTimeTime(String timeTime) {
        this.timeTime = timeTime;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getnType() {
        return nType;
    }

    public void setnType(String nType) {
        this.nType = nType;
    }

    public String getByName() {
        return byName;
    }

    public void setByName(String byName) {
        this.byName = byName;
    }

    public List<String> getParamStr() {
        return paramStr;
    }

    public void setParamStr(List<String> paramStr) {
        this.paramStr = paramStr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
