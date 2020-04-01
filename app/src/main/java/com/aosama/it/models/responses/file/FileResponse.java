package com.aosama.it.models.responses.file;

import com.aosama.it.models.responses.nested.BoardData2;
import com.aosama.it.models.responses.nested.DashBoardData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileResponse {
    @SerializedName("attachName")
    @Expose
    private String attachName;

    @SerializedName("attachKey")
    @Expose
    private String attachKey;

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
}
