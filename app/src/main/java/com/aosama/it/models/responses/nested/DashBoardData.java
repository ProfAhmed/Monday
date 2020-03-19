package com.aosama.it.models.responses.nested;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashBoardData {

    @SerializedName("noOfTasks")
    @Expose
    private Integer noOfTasks;

    @SerializedName("noOfComments")
    @Expose
    private Integer noOfComments;

    public Integer getNoOfComments() {
        return noOfComments;
    }

    public void setNoOfComments(Integer noOfComments) {
        this.noOfComments = noOfComments;
    }

    @SerializedName("taskStatusNo")
    @Expose
    private List<TaskStatusNo> taskStatusNos;

    public Integer getNoOfTasks() {
        return noOfTasks;
    }

    public void setNoOfTasks(Integer noOfTasks) {
        this.noOfTasks = noOfTasks;
    }

    public List<TaskStatusNo> getTaskStatusNos() {
        return taskStatusNos;
    }

    public void setTaskStatusNos(List<TaskStatusNo> taskStatusNos) {
        this.taskStatusNos = taskStatusNos;
    }
}
