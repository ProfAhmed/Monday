package com.aosama.it.models.responses.boards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataBoards {

    @SerializedName("BoardDataList")
    @Expose
    private List<BoardDataList> boardDataList = null;

    public List<BoardDataList> getBoardDataList() {
        return boardDataList;
    }

    public void setBoardDataList(List<BoardDataList> boardDataList) {
        this.boardDataList = boardDataList;
    }
}
