package com.aosama.it.utiles;


import android.os.Build;

import com.aosama.it.models.responses.boards.BoardDataList;
import com.aosama.it.models.responses.boards.NestedBoard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExpandableListDataPump {
    private static List<NestedBoard> tempNestedBoards = new ArrayList<>();

    public static HashMap<BoardDataList, List<NestedBoard>>
    getData(List<BoardDataList> boardDataList) {
        LinkedHashMap<BoardDataList, List<NestedBoard>> expandableListDetail =
                new LinkedHashMap<>();
        for (int i = 0; i < boardDataList.size(); i++) {
            for (int j = 0; j < boardDataList.get(i).getNestedBoard().size(); j++) {
                tempNestedBoards.add(boardDataList.get(i).getNestedBoard().get(j));
                if (boardDataList.get(i).getNestedBoard().get(j).getNestedBoard() != null) {
                    tempNestedBoards.addAll(boardDataList.get(i).getNestedBoard().get(j).getNestedBoard());
                }
            }
            expandableListDetail.put(boardDataList.get(i), tempNestedBoards);
            tempNestedBoards = new ArrayList<>();
        }
        return expandableListDetail;
    }
}
