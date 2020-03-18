package com.aosama.it.utiles;


import com.aosama.it.models.responses.boards.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<NestedBoard>> getData(List<BoardDataList> boardDataList) {
        HashMap<String, List<NestedBoard>> expandableListDetail = new HashMap<String, List<NestedBoard>>();
        for (BoardDataList element : boardDataList) {
            expandableListDetail.put(element.getName(), element.getNestedBoard());
        }
        return expandableListDetail;
    }
}
