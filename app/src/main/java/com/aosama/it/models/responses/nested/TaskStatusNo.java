package com.aosama.it.models.responses.nested;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TaskStatusNo {

    @SerializedName("noOfTasks")
    @Expose
    private Integer noOfTasks;

    @SerializedName("taskStatusNo")
    @Expose
    private List<TaskStatusNoObject> taskStatusNos;

    @SerializedName("noOfComments")
    @Expose
    private Integer noOfComments;

    public Integer getNoOfTasks() {
        return noOfTasks;
    }

    public void setNoOfTasks(Integer noOfTasks) {
        this.noOfTasks = noOfTasks;
    }

    public List<TaskStatusNoObject> getTaskStatusNos() {
        return taskStatusNos;
    }

    public void setTaskStatusNos(List<TaskStatusNoObject> taskStatusNos) {
        this.taskStatusNos = taskStatusNos;
    }

    public Integer getNoOfComments() {
        return noOfComments;
    }

    public void setNoOfComments(Integer noOfComments) {
        this.noOfComments = noOfComments;
    }
}
