package com.aosama.it.models.responses.boards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Comment;

import java.util.List;

public class TaskE {
    @SerializedName("isPrivate")
    @Expose
    private boolean isPrivate;
    @SerializedName("addDate")
    @Expose
    private String addDate;
    @SerializedName("progressValue")
    @Expose
    private int progressValue;
    @SerializedName("isDelete")
    @Expose
    private boolean isDelete;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("id")
    @Expose
    private String id2;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("dueDate")
    @Expose
    private String dueDate;
    @SerializedName("ownerId")
    @Expose
    private String ownerId;
    @SerializedName("assignee")
    @Expose
    private List<Assignee> assignee = null;
    @SerializedName("extraData")
    @Expose
    private List<Object> extraData = null;
    @SerializedName("comments")
    @Expose
    private List<CommentGroup> comments = null;

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public int getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(int progressValue) {
        this.progressValue = progressValue;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<Assignee> getAssignee() {
        return assignee;
    }

    public void setAssignee(List<Assignee> assignee) {
        this.assignee = assignee;
    }

    public List<Object> getExtraData() {
        return extraData;
    }

    public void setExtraData(List<Object> extraData) {
        this.extraData = extraData;
    }

    public List<CommentGroup> getComments() {
        return comments;
    }

    public void setComments(List<CommentGroup> comments) {
        this.comments = comments;
    }
}
