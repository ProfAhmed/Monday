package com.aosama.it.models.responses.boards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NestedBoard {
    @SerializedName("isPrivate")
    @Expose
    private boolean isPrivate;
    @SerializedName("isArchive")
    @Expose
    private boolean isArchive;
    @SerializedName("isDelete")
    @Expose
    private boolean isDelete;
    @SerializedName("_id2")
    @Expose
    private String id2;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("templateId")
    @Expose
    private String templateId;
    @SerializedName("ownerId")
    @Expose
    private String ownerId;
    @SerializedName("tasksGroup")
    @Expose
    private List<TasksGroup> tasksGroup = null;
    @SerializedName("attachmentsGeneral")
    @Expose
    private List<Object> attachmentsGeneral = null;
    @SerializedName("users")
    @Expose
    private List<UserBoard> users = null;
    @SerializedName("team")
    @Expose
    private Team team = null;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public void setArchive(boolean archive) {
        isArchive = archive;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<TasksGroup> getTasksGroup() {
        return tasksGroup;
    }

    public void setTasksGroup(List<TasksGroup> tasksGroup) {
        this.tasksGroup = tasksGroup;
    }

    public List<Object> getAttachmentsGeneral() {
        return attachmentsGeneral;
    }

    public void setAttachmentsGeneral(List<Object> attachmentsGeneral) {
        this.attachmentsGeneral = attachmentsGeneral;
    }

    public List<UserBoard> getUsers() {
        return users;
    }

    public void setUsers(List<UserBoard> users) {
        this.users = users;
    }
}
