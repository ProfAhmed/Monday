package com.aosama.it.models.responses.boards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BoardDataList {
    @SerializedName("isPrivate")
    @Expose
    private boolean isPrivate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("nestedBoard")
    @Expose
    private List<NestedBoard> nestedBoard = null;

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<NestedBoard> getNestedBoard() {
        return nestedBoard;
    }

    public void setNestedBoard(List<NestedBoard> nestedBoard) {
        this.nestedBoard = nestedBoard;
    }


}
