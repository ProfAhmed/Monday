package com.aosama.it.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aosama.it.R;
import com.aosama.it.constants.Constants;
import com.aosama.it.models.responses.boards.BoardDataList;
import com.aosama.it.models.responses.boards.UserBoard;
import com.aosama.it.ui.adapter.board.HAdapterUsers;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BoardDetailsFragment extends Fragment implements
        HAdapterUsers.OnUserClicked {

    @BindView(R.id.rvAllUseres)
    RecyclerView rvAllUsers;
    private BoardDataList boardDataList = new BoardDataList();
    private Gson gson = new Gson();
    private HAdapterUsers adapterUsers;

    public static BoardDetailsFragment newInstance() {

        Bundle args = new Bundle();

        BoardDetailsFragment fragment = new BoardDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_board_item_details, null);
        ButterKnife.bind(BoardDetailsFragment.class, viewRoot);
        return viewRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.SELECTED_BORAD)) {
                boardDataList = gson.fromJson(
                        getArguments().getString(Constants.SELECTED_BORAD), BoardDataList.class);
            }
        }

        rvAllUsers.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        rvAllUsers.setHasFixedSize(false);
        if (!boardDataList.getNestedBoard().isEmpty()) {
            adapterUsers = new HAdapterUsers(getActivity(),
                    boardDataList.getNestedBoard()
                            .get(0).getUsers(), this);
            rvAllUsers.setAdapter(adapterUsers);
            adapterUsers.notifyDataSetChanged();

        }


    }


    @Override
    public void onUserClicked(View view, int position, UserBoard userBoard) {

        new AlertDialog.Builder(getActivity())
                .setTitle(userBoard.getShortName())
                .setMessage(userBoard.getFullName())
                .setCancelable(true)
                .show();

    }
}
