package com.aosama.it.models.responses.mail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ToUser {
    @SerializedName("_id2")
    @Expose
    private String id2;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

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
}
