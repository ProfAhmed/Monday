package com.aosama.it.models.responses.nested;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BoardData {

    @SerializedName("BoardData")
    @Expose
    private BoardData2 BoardData;

    @SerializedName("dashBoardData")
    @Expose
    private DashBoardData dashBoardData;

    public BoardData() {

    }

    public BoardData2 getBoardData() {
        return BoardData;
    }

    public void setBoardData(BoardData2 boardData) {
        BoardData = boardData;
    }

    public DashBoardData getDashBoardData() {
        return dashBoardData;
    }

    public void setDashBoardData(DashBoardData dashBoardData) {
        this.dashBoardData = dashBoardData;
    }
}
