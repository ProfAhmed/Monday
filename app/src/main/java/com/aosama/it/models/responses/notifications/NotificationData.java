package com.aosama.it.models.responses.notifications;

import com.aosama.it.models.responses.boards.NestedBoard;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationData {
    @SerializedName("notification")
    @Expose
    private List<NotificationModel> notificationModelList;

    @SerializedName("tasks")
    @Expose
    private List<TaskN> tasks;

    public List<NotificationModel> getNotificationModelList() {
        return notificationModelList;
    }

    public void setNotificationModelList(List<NotificationModel> notificationModelList) {
        this.notificationModelList = notificationModelList;
    }

    public List<TaskN> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskN> tasks) {
        this.tasks = tasks;
    }
}
