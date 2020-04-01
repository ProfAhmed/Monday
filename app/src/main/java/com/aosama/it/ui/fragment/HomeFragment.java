package com.aosama.it.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.aosama.it.R;
import com.aosama.it.constants.Constants;
import com.aosama.it.models.responses.boards.BoardDataList;
import com.aosama.it.models.responses.boards.NestedBoard;
import com.aosama.it.ui.adapter.CustomExpandableListAdapter;
import com.aosama.it.utiles.ExpandableListDataPump;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.MyUtilis;
import com.aosama.it.viewmodels.HomeViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


public class HomeFragment extends Fragment {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<BoardDataList> expandableListTitle;
    private HashMap<BoardDataList, List<NestedBoard>> expandableListDetail;

    private List<BoardDataList> boardDataLists = new ArrayList<>();
    private Gson gson = new Gson();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getActivity().getString(R.string.boards));
        HomeViewModel viewModel = ViewModelProviders.
                of(this).get(HomeViewModel.class);
        expandableListView = root.findViewById(R.id.expandableListView);

        expandableListView.setOnGroupExpandListener(groupPosition -> {
//                Toast.makeText(getActivity(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
        });

        expandableListView.setOnGroupCollapseListener(groupPosition -> {
//                Toast.makeText(getActivity(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();

        });

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            try {
//                    Toast.makeText(
//                            getActivity(),
//                            expandableListTitle.get(groupPosition)
//                                    + " -> "
//                                    + expandableListDetail.get(
//                                    expandableListTitle.get(groupPosition))
//                                    .get(childPosition).getName(), Toast.LENGTH_SHORT
//                    ).show();
                toolbar.setTitle(expandableListDetail.get(
                        expandableListTitle.get(groupPosition))
                        .get(childPosition).getName());
                ///here
                fireBoardItemDetails(expandableListDetail.get(
                        expandableListTitle.get(groupPosition))
                        .get(childPosition).getId());


            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        });

        AlertDialog dialog = MyUtilis.myDialog(getActivity());
        dialog.show();
        viewModel.boards(MyConfig.BOARDS).observe(this,
                boardsResponseStateData -> {
                    dialog.dismiss();
                    switch (boardsResponseStateData.getStatus()) {
                        case SUCCESS:
                            if (boardsResponseStateData.getData() != null) {
                                boardDataLists = boardsResponseStateData
                                        .getData()
                                        .getData()
                                        .getBoardDataList();
                                if (boardDataLists != null) {
                                    expandableListDetail =
                                            ExpandableListDataPump.
                                                    getData(boardDataLists);
//                                    expandableListTitle = new
//                                            ArrayList(expandableListDetail.keySet());
                                    expandableListTitle = new ArrayList<BoardDataList>(expandableListDetail.keySet());
                                    expandableListAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
                                    expandableListView.setAdapter(expandableListAdapter);
                                    Log.d("FetchedBoards", boardsResponseStateData.toString());
                                }
                            }
                            break;
                        case FAIL:
                            Toast.makeText(getActivity(), boardsResponseStateData.getErrorsMessages() != null ? boardsResponseStateData.getErrorsMessages().getErrorMessages().get(0) : null, Toast.LENGTH_SHORT).show();
                            break;
                        case ERROR:
                            if (boardsResponseStateData.getError() != null) {
                                Toast.makeText(getActivity(), getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                                Log.v("Statues", "Error" + boardsResponseStateData.getError().getMessage());
                            }
                            break;
                        case CATCH:
                            Toast.makeText(getActivity(), getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                            break;
                    }
                });
        return root;
    }

    private void fireBoardItemDetails(String id) {

        BoardDetailsFragment boardDetailsFragment = new BoardDetailsFragment();
        Bundle b = new Bundle();
        b.putString(Constants.SELECTED_BORAD, id);
        boardDetailsFragment.setArguments(b);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(BoardDetailsFragment.class.getSimpleName())
                .replace(R.id.nav_host_fragment, boardDetailsFragment).commit();


    }
}