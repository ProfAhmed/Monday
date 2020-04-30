package com.aosama.it.models.responses.mail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataMail {
    @SerializedName("isSeen")
    @Expose
    private boolean isSeen;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("fromUser")
    @Expose
    private FromUser fromUser;
    @SerializedName("toUsers")
    @Expose
    private List<ToUser> toUsers = null;
    @SerializedName("attachments")
    @Expose
    private List<Object> attachments = null;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private String id;


    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public FromUser getFromUser() {
        return fromUser;
    }

    public void setFromUser(FromUser fromUser) {
        this.fromUser = fromUser;
    }

    public List<ToUser> getToUsers() {
        return toUsers;
    }

    public void setToUsers(List<ToUser> toUsers) {
        this.toUsers = toUsers;
    }

    public List<Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
