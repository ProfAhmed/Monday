package com.aosama.it.models.responses.boards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TasksGroup {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("id")
    @Expose
    private String id2;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("templateId")
    @Expose
    private String templateId;
    @SerializedName("tasks")
    @Expose
    private List<TaskE> tasks = null;

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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public List<TaskE> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskE> tasks) {
        this.tasks = tasks;
    }
}
