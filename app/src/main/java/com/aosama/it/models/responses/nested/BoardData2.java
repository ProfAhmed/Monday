package com.aosama.it.models.responses.nested;

import com.aosama.it.models.responses.boards.NestedBoard;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BoardData2 {


    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("nestedBoard")
    @Expose
    private List<NestedBoard> nestedBoards;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<NestedBoard> getNestedBoards() {
        return nestedBoards;
    }

    public void setNestedBoards(List<NestedBoard> nestedBoards) {
        this.nestedBoards = nestedBoards;
    }


}
