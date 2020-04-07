package com.aosama.it.models.responses.notifications;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskN {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("boardId")
    @Expose
    private String boardId;
    @SerializedName("id2")
    @Expose
    private String id2;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dueDate")
    @Expose
    private String dueDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}