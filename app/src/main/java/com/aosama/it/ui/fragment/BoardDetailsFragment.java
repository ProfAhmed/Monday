package com.aosama.it.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.aosama.it.R;
import com.aosama.it.constants.Constants;
import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.boards.Assignee;
import com.aosama.it.models.responses.boards.Attachment;
import com.aosama.it.models.responses.boards.BoardDataList;
import com.aosama.it.models.responses.boards.NestedBoard;
import com.aosama.it.models.responses.boards.TaskE;
import com.aosama.it.models.responses.boards.UserBoard;
import com.aosama.it.models.responses.nested.BoardData;
import com.aosama.it.ui.activities.CommentsActivity;
import com.aosama.it.ui.adapter.AttachmentAdapter;
import com.aosama.it.ui.adapter.BoardsAdapter;
import com.aosama.it.ui.adapter.CustomListAdapterAssigneeDialog;
import com.aosama.it.ui.adapter.CustomListAdapterUsersDialog;
import com.aosama.it.ui.adapter.board.HAdapterUsers;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.MyUtilis;
import com.aosama.it.viewmodels.BoardDetailViewModel;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoardDetailsFragment extends Fragment implements
        HAdapterUsers.OnUserClicked, BoardsAdapter.OnItemClick {

    private static final String TAG = "BoardDetailsFragment";
    public static final int PICKFILE_RESULT_CODE_BOARD = 200;
    public static final int PICKFILE_RESULT_CODE_TASK = 100;
    @BindView(R.id.rvAllUseres)
    RecyclerView rvAllUsers;
    @BindView(R.id.tvTeamName)
    TextView tvTeamName;
    @BindView(R.id.tvNoTasksHere)
    TextView tvNoTasksHere;
    @BindView(R.id.llContainer)
    LinearLayout llContainer;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.ivAttachment)
    ImageView ivAttachment;
    @BindView(R.id.ivPeople)
    ImageView ivPeople;

    private BoardDataList boardDataList = new BoardDataList();
    private Gson gson = new Gson();
    private HAdapterUsers adapterUsers;
    private List<UserBoard> userBoards = new ArrayList<>();
    private BoardDetailViewModel boardDetailViewModel = null;
    private android.app.AlertDialog dialog = null;
    private String id;
    private NestedBoard nestedBoard;
    static public TextView tvAttachment;
    public static ViewDialogAttachments alert;

    public static ProgressDialog mProgressDialog;
    public static String attachName;
    public static String attachKey;
    public static String taskId;
    public static String boardId;
    public static ViewDialogAttachmentsBoard alertBoard;

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
        //ButterKnife.bind(this, viewRoot);
        ButterKnife.bind(BoardDetailsFragment.this, viewRoot);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getResources().getString(R.string.loading_msg));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMax(100);

        return viewRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        gettingThePassedBoardModel();

        showDialog();
        fetchingData();

        ivAttachment.setOnClickListener(view1 -> {
            alertBoard = new ViewDialogAttachmentsBoard();
            alertBoard.showDialog(getActivity(), nestedBoard.getAttachmentsGeneral());

        });
        //setting an empty list to the adapter
//        settingAdapter();

        ivPeople.setOnClickListener(view12 -> {
            if (userBoards != null && userBoards.size() > 0)
                showDialogUsers((ArrayList<UserBoard>) userBoards);
            else {
                Toast.makeText(getActivity(), getString(R.string.no_users_here), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void refreshAdapterUsers() {
        adapterUsers = new HAdapterUsers(getActivity(),
                userBoards,
                this);
        rvAllUsers.setAdapter(adapterUsers);
        adapterUsers.notifyDataSetChanged();
    }

    private void showDialogUsers(ArrayList<UserBoard> assigneeList) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        View view = getLayoutInflater().inflate(R.layout.dialog_people, null);

        ListView lv = view.findViewById(R.id.custom_list);

        // Change MyActivity.this and myListOfItems to your own values
        CustomListAdapterUsersDialog clad = new CustomListAdapterUsersDialog(getActivity(), assigneeList);

        lv.setAdapter(clad);
        dialog.setContentView(view);

        dialog.show();

    }

    private void fetchingData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        Log.e(TAG, "fetchingData: " + id);

        String url = MyConfig.NESTED + "?id=" + id;
        boardDetailViewModel.getBoardDetails(url, params)
                .observe(this,
                        basicResponseStateData -> {
                            dialog.dismiss();
                            switch (basicResponseStateData.getStatus()) {
                                case SUCCESS:
                                    if (basicResponseStateData.getData() != null) {
                                        fillViewWithData(basicResponseStateData.getData());
                                        fillTable(basicResponseStateData.getData().getData().getBoardData().getNestedBoards().get(0));
                                        nestedBoard = basicResponseStateData.getData().getData().getBoardData().getNestedBoards().get(0);
                                        boardId = nestedBoard.getId();
                                    }
                                    Log.e(TAG, "fetchingData: success");
                                    break;
                                case FAIL:
                                    Log.e(TAG, "fetchingData: failed");
                                    Toast.makeText(getActivity(),
                                            basicResponseStateData.getErrorsMessages()
                                                    != null ?
                                                    basicResponseStateData.getErrorsMessages()
                                                            .getErrorMessages().get(0) : null,
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                case ERROR:
                                    Log.e(TAG, "fetchingData: error");
                                    if (basicResponseStateData.getError() != null) {
                                        Toast.makeText(getActivity(),
                                                getString(R.string.
                                                        no_connection_msg), Toast.LENGTH_LONG).show();
                                        Log.v("Statues", "Error" + basicResponseStateData
                                                .getError().getMessage());
                                    }
                                    break;
                                case CATCH:
                                    Toast.makeText(getActivity(),
                                            getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                                    break;
                            }
                        });

    }

    private void fillViewWithData(BasicResponse<BoardData> data) {
        userBoards = data.getData()
                .getBoardData()
                .getNestedBoards()
                .get(0)
                .getUsers();
        refreshAdapterUsers();


        //-------------
        //setting the teamname
        try {
            tvTeamName.setText(data.getData()
                    .getBoardData().getNestedBoards()
                    .get(0).getTeam().getTeamName());
            if (data.getData()
                    .getBoardData().getNestedBoards()
                    .get(0).getTeam().getTeamName().length() == 0)
                tvTeamName.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
            tvTeamName.setVisibility(View.GONE);
        }
        //

    }

//    private void fillTable(NestedBoard nestedBoard) {
//        for (int j = 0; j < nestedBoard.getTasksGroup().size(); j++) {
//            if (nestedBoard.getTasksGroup().get(j).getTasks().size() == 0) {
//                tvNoTasksHere.setVisibility(View.VISIBLE);
//                continue;
//            }
//            LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService
//                    (Context.LAYOUT_INFLATER_SERVICE);
//            View view = inflater.inflate(R.layout.dynamic_table_layout, null, false);
//            TextView tvTableName = view.findViewById(R.id.tvTableName);
//
//            TableLayout tbl_header = view.findViewById(R.id.tbl_header);
//            TableLayout tbl_scrolled = view.findViewById(R.id.tbl_scrolled);
//            tvTableName.setText(nestedBoard.getTasksGroup().get(j).getName());
//            llContainer.addView(view);
//
//            for (int i = 0; i < nestedBoard.getTasksGroup().get(j).getTasks().size(); i++) {
//                LayoutInflater inflater1 = (LayoutInflater) getActivity().getApplicationContext().getSystemService
//                        (Context.LAYOUT_INFLATER_SERVICE);
//                LayoutInflater inflater2 = (LayoutInflater) getActivity().getApplicationContext().getSystemService
//                        (Context.LAYOUT_INFLATER_SERVICE);
//
//                View view1 = inflater1.inflate(R.layout.fixed_row_layout, null, false);
//                View view2 = inflater1.inflate(R.layout.dynamic_row_layout, null, false);
//                ImageView ivComments = view2.findViewById(R.id.ivComments);
//
//                int finalI = i;
//                int finalJ = j;
//                ivComments.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                        Toast.makeText(getActivity(), nestedBoard.getTasksGroup().get(finalJ).getTasks().get(finalI).getName(), Toast.LENGTH_SHORT).show();
//                        fireTaskItemComments(nestedBoard.getTasksGroup().get(finalJ).getTasks().get(finalI), nestedBoard);
//                    }
//                });
//                TextView tvName = view1.findViewById(R.id.tvName);
//                TextView tvStatus = view2.findViewById(R.id.tvStatus);
//                TextView tvAddDate = view2.findViewById(R.id.tvAddDate);
//                TextView tvStartDate = view2.findViewById(R.id.StartDate);
//                TextView tvDueDate = view2.findViewById(R.id.tvDueDate);
//                TextView tvMeetingUrl = view2.findViewById(R.id.tvMeetingUrl);
//                TextView tvMeetingTime = view2.findViewById(R.id.tvMeetingTime);
//                TextView tvTeam = view2.findViewById(R.id.tvTeam);
//
//                tvName.setText(nestedBoard.getTasksGroup().get(j).getTasks().get(i).getName());
//                tvStatus.setText(nestedBoard.getTasksGroup().get(j).getTasks().get(i).getStatus().getName());
//                tvAddDate.setText(MyUtilis.formateDate(nestedBoard.getTasksGroup().get(j).getTasks().get(i).getAddDate()));
//                tvStartDate.setText(MyUtilis.formateDate(nestedBoard.getTasksGroup().get(j).getTasks().get(i).getStartDate()));
//                tvDueDate.setText(MyUtilis.formateDate(nestedBoard.getTasksGroup().get(j).getTasks().get(i).getDueDate()));
//                tvMeetingUrl.setText(nestedBoard.getTasksGroup().get(j).getTasks().get(i).getMeetingUrl());
//                try {
//                    tvMeetingTime.setText(MyUtilis.formateDateTime(nestedBoard.getTasksGroup().get(j).getTasks().get(i).getMeetingTime()));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (nestedBoard.getTasksGroup().get(j).getTasks().get(i).getAssignee() != null) {
//                    tvTeam.setText(String.valueOf(nestedBoard.getTasksGroup().get(j).getTasks().get(i).getAssignee().size()));
//
//                    int finalI1 = i;
//                    int finalJ1 = j;
//                    tvTeam.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            showDialogPeople((ArrayList<Assignee>) nestedBoard.getTasksGroup().get(finalJ1).getTasks().get(finalI1).getAssignee());
//                        }
//                    });
//                }
//                tvStatus.setBackgroundColor(Color.parseColor(nestedBoard.getTasksGroup().get(j).getTasks().get(i).getStatus().getColor()));
//                tbl_header.addView(view1);
//                tbl_scrolled.addView(view2);
//
//            }
//        }
//    }

    private void fillTable(NestedBoard nestedBoard) {
        ArrayList<TaskE> taskES = new ArrayList<>();
        final BoardsAdapter adapter = new BoardsAdapter(this, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        for (int j = 0; j < nestedBoard.getTasksGroup().size(); j++) {
            try {
                nestedBoard.getTasksGroup().get(j).getTasks().get(0).setTableName(nestedBoard.getTasksGroup().get(j).getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            taskES.addAll(nestedBoard.getTasksGroup().get(j).getTasks());
            adapter.setTasksGroups(taskES);
        }

    }

    private void fireTaskItemComments(TaskE taskE, NestedBoard nestedBoard) {

        Intent intent = new Intent(getActivity(), CommentsActivity.class);
        intent.putExtra(Constants.SELECTED_COMMENT, gson.toJson(taskE));
        intent.putExtra(Constants.SELECTED_BORAD, gson.toJson(nestedBoard));

        startActivity(intent);

    }

    private void gettingThePassedBoardModel() {
        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.SELECTED_BORAD)) {
//                boardDataList = gson.fromJson(
//                        getArguments().getString(Constants.SELECTED_BORAD), BoardDataList.class);
                id = getArguments().getString(Constants.SELECTED_BORAD);
            }
        }
    }

    private void init() {
        boardDetailViewModel = ViewModelProviders.of(getActivity())
                .get(BoardDetailViewModel.class);


        rvAllUsers.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        rvAllUsers.setHasFixedSize(false);

    }

    private void showDialog() {
        dialog = MyUtilis.myDialog(getActivity());
        dialog.show();
    }

    private void showDialogPeople(ArrayList<Assignee> assigneeList) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        View view = getLayoutInflater().inflate(R.layout.dialog_people, null);

        ListView lv = view.findViewById(R.id.custom_list);

        // Change MyActivity.this and myListOfItems to your own values
        CustomListAdapterAssigneeDialog clad = new CustomListAdapterAssigneeDialog(getActivity(), assigneeList);

        lv.setAdapter(clad);
        dialog.setContentView(view);

        dialog.show();

    }


    private void showDialogDates(TaskE taskE) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        View view = getLayoutInflater().inflate(R.layout.dialog_dates, null);

        TextView tvStartDate = view.findViewById(R.id.tvStartDate);

        tvStartDate.setText(MyUtilis.formateDate(taskE.getStartDate()));
        dialog.setContentView(view);

        dialog.show();

    }

    private void showDialogMeeting(TaskE taskE) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        View view = getLayoutInflater().inflate(R.layout.dialog_meeting, null);

        TextView tvMeetingTime = view.findViewById(R.id.tvMeetingTime);
        TextView tvMeetingLink = view.findViewById(R.id.tvMeetingUrl);

        tvMeetingLink.setText(taskE.getMeetingUrl());
        try {
//            tvMeetingTime.setText(MyUtilis.formateDateTime(taskE.getMeetingTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.setContentView(view);

        dialog.show();

    }

    @Override
    public void setOnBoardItemClick(View v, TaskE taskE) {
        switch (v.getId()) {
            case R.id.ivComments:
                fireTaskItemComments(taskE, nestedBoard);
                break;
            case R.id.ivAttachment:
                taskId = taskE.getId2();
                alert = new ViewDialogAttachments();
                alert.showDialog(getActivity(), taskE.getAttachments());
                break;

            case R.id.ivPeople:
                showDialogPeople((ArrayList<Assignee>) taskE.getAssignee());

        }
    }


    public class ViewDialogAttachments implements AttachmentAdapter.OnUserClicked {
        private Dialog dialog;

        void showDialog(Activity activity, List<Attachment> attachments) {
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.custom_attachment_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            ImageButton imBtnCancel = dialog.findViewById(R.id.imBtnCancel);
            ImageView ivAttachment = dialog.findViewById(R.id.ivAttachment);
            tvAttachment = dialog.findViewById(R.id.tvAttachmentName);
            RecyclerView rv = dialog.findViewById(R.id.rv);

            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            AttachmentAdapter adapter = new AttachmentAdapter(getActivity(), attachments, this);
            rv.setAdapter(adapter);

            imBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            ivAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                    try {
                        activity.startActivityForResult(
                                Intent.createChooser(intent, "Select a File to Copy"),
                                PICKFILE_RESULT_CODE_TASK);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(activity, "Please install a File Manager.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
        }

        public void dismiss() {
            if (dialog != null)
                dialog.dismiss();
        }

        @Override
        public void onUserClicked(View view, int position, Attachment attachment) {

        }
    }

    public class ViewDialogAttachmentsBoard implements AttachmentAdapter.OnUserClicked {
        private static final int PICKFILE_RESULT_CODE = 100;
        private Dialog dialog;

        void showDialog(Activity activity, List<Attachment> attachments) {
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.custom_attachment_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            ImageButton imBtnCancel = dialog.findViewById(R.id.imBtnCancel);
            ImageView ivAttachment = dialog.findViewById(R.id.ivAttachment);
            tvAttachment = dialog.findViewById(R.id.tvAttachmentName);
            RecyclerView rv = dialog.findViewById(R.id.rv);

            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            AttachmentAdapter adapter = new AttachmentAdapter(getActivity(), attachments, this);
            rv.setAdapter(adapter);

            imBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            ivAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                    try {
                        activity.startActivityForResult(
                                Intent.createChooser(intent, "Select a File to Copy"),
                                PICKFILE_RESULT_CODE_BOARD);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(activity, "Please install a File Manager.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
        }

        public void dismiss() {
            if (dialog != null)
                dialog.dismiss();
        }

        @Override
        public void onUserClicked(View view, int position, Attachment attachment) {

        }
    }

    public class ViewDialog {
        void showDialog(Activity activity, UserBoard userBoard) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.custom_people_card_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            ImageButton imBtnCancel = dialog.findViewById(R.id.imBtnCancel);
            ImageView userPic = dialog.findViewById(R.id.ivUserImagePhoto);
            TextView tvUserName = dialog.findViewById(R.id.tvUserName);
            tvUserName.setText(userBoard.getFullName());

            String path = userBoard.getUserImage();
            if (!TextUtils.isEmpty(path) && path != null
                    && path.length() > 0) {
                Picasso.get().load(path)
                        .into(userPic, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            } else {
                String firstChar = "";
                if (userBoard.getShortName().length() > 0) {

                    TextDrawable drawable2 = createTextDrawable(userBoard.getShortName());
                    userPic.setImageDrawable(drawable2);
                }
                String successMessage = "";

            }
            imBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private TextDrawable createTextDrawable(String firstChar) {
//        TextDrawable drawable1 = TextDrawable.builder()
//                .buildRoundRect(firstChar, Color.RED, 10);
        int dimWH = (int) getActivity().getResources()
                .getDimension(R.dimen._60sdp);
        int fonsSize =
                (int) getActivity().getResources()
                        .getDimension(R.dimen._20ssp);
        return TextDrawable.builder().beginConfig().
                textColor(Color.BLUE)
//                .beginConfig()
                .fontSize(fonsSize)
                .bold()
                .width(dimWH)  // width in px
                .height(dimWH) // height in px
                .endConfig()
                .buildRect(firstChar, Color.parseColor("#41C5C3C3"));

    }

    @Override
    public void onUserClicked(View view, int position, UserBoard userBoard) {
        ViewDialog alert = new ViewDialog();
        alert.showDialog(getActivity(), userBoard);

    }
}
