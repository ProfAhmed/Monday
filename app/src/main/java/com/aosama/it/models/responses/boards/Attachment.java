package com.aosama.it.models.responses.boards;

import com.aosama.it.models.responses.sing_in.User;

import java.util.List;

public class Attachment {
    private boolean isPrivate;
    private String id;
    private String attachId;
    private String attachName;
    private String attachKey;
    private List<User> users = null;
    private String byId;
    private String byUserName;
    private String byShortName;
    private String byUserImage;
    private String byFullName;
    private String addDate;
    private String addDateTime;

    public String getAddDateTime() {
        return addDateTime;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getAttachKey() {
        return attachKey;
    }

    public void setAttachKey(String attachKey) {
        this.attachKey = attachKey;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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
}
