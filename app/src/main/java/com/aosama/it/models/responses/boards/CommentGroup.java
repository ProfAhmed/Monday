package com.aosama.it.models.responses.boards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentGroup {
    @SerializedName("addDate")
    @Expose
    private String addDate;
    @SerializedName("updateDate")
    @Expose
    private String updateDate;
    @SerializedName("isDelete")
    @Expose
    private boolean isDelete;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("commentId")
    @Expose
    private String commentId;
    @SerializedName("byId")
    @Expose
    private String byId;
    @SerializedName("byUserName")
    @Expose
    private String byUserName;
    @SerializedName("byShortName")
    @Expose
    private String byShortName;
    @SerializedName("byUserImage")
    @Expose
    private String byUserImage;
    @SerializedName("byFullName")
    @Expose
    private String byFullName;
    @SerializedName("commentData")
    @Expose
    private String commentData;
    @SerializedName("attachments")
    @Expose
    private List<Object> attachments = null;
    @SerializedName("nestedComments")
    @Expose
    private List<NestedComment> nestedComments = null;

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
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

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getById() {
        return byId;
    }

    public void setById(String byId) {
        this.byId = byId;
    }

    public String getByUserName() {
        return byUserName;
    }

    public void setByUserName(String byUserName) {
        this.byUserName = byUserName;
    }

    public String getByShortName() {
        return byShortName;
    }

    public void setByShortName(String byShortName) {
        this.byShortName = byShortName;
    }

    public String getByUserImage() {
        return byUserImage;
    }

    public void setByUserImage(String byUserImage) {
        this.byUserImage = byUserImage;
    }

    public String getByFullName() {
        return byFullName;
    }

    public void setByFullName(String byFullName) {
        this.byFullName = byFullName;
    }

    public String getCommentData() {
        return commentData;
    }

    public void setCommentData(String commentData) {
        this.commentData = commentData;
    }

    public List<Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }

    public List<NestedComment> getNestedComments() {
        return nestedComments;
    }

    public void setNestedComments(List<NestedComment> nestedComments) {
        this.nestedComments = nestedComments;
    }
}
