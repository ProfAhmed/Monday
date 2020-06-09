package com.aosama.it.utiles;


import android.os.Build;

import com.aosama.it.models.responses.boards.BoardDataList;
import com.aosama.it.models.responses.boards.NestedBoard;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExpandableListDataPump {
    public static HashMap<BoardDataList, List<NestedBoard>>
    getData(List<BoardDataList> boardDataList) {
        LinkedHashMap<BoardDataList, List<NestedBoard>> expandableListDetail =
                new LinkedHashMap<>();
//        for (BoardDataList element : boardDataList) {
//            expandableListDetail.put(element, element.getNestedBoard());
//        }

        for (int i = 0; i < boardDataList.size(); i++) {
            expandableListDetail.put(boardDataList.get(i), boardDataList.get(i).getNestedBoard());
        }
        return expandableListDetail;
    }
}
