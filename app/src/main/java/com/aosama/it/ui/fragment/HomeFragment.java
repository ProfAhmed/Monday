package com.aosama.it.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aosama.it.R;
import com.aosama.it.models.responses.boards.NestedBoard;
import com.aosama.it.ui.activities.ChangePasswordActivity;
import com.aosama.it.ui.activities.HomeActivity;
import com.aosama.it.ui.adapter.CustomExpandableListAdapter;
import com.aosama.it.utiles.ExpandableListDataPump;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.MyUtilis;
import com.aosama.it.utiles.PreferenceProcessor;
import com.aosama.it.viewmodels.HomeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<NestedBoard>> expandableListDetail;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        HomeViewModel viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        expandableListView = (ExpandableListView) root.findViewById(R.id.expandableListView);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                try {
                    Toast.makeText(
                            getActivity(),
                            expandableListTitle.get(groupPosition)
                                    + " -> "
                                    + expandableListDetail.get(
                                    expandableListTitle.get(groupPosition)).get(
                                    childPosition).getName(), Toast.LENGTH_SHORT
                    ).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        AlertDialog dialog = MyUtilis.myDialog(getActivity());
        dialog.show();
        viewModel.boards(MyConfig.BOARDS).observe(this, boardsResponseStateData -> {
            dialog.dismiss();
            switch (boardsResponseStateData.getStatus()) {
                case SUCCESS:

                    if (boardsResponseStateData.getData() != null) {
                        expandableListDetail = ExpandableListDataPump.getData(boardsResponseStateData.getData().getData().getBoardDataList());
                        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                        expandableListAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
                        expandableListView.setAdapter(expandableListAdapter);
                        Log.d("FetchedBoards", boardsResponseStateData.toString());
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
}