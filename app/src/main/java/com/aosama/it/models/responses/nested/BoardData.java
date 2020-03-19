package com.aosama.it.models.responses.nested;

import com.aosama.it.models.responses.boards.NestedBoard;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BoardData {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("nestedBoard")
    @Expose
    private List<NestedBoard> nestedBoards;

    public BoardData() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<NestedBoard> getNestedBoards() {
        return nestedBoards;
    }

    public void setNestedBoards(List<NestedBoard> nestedBoards) {
        this.nestedBoards = nestedBoards;
    }
}
