package com.aosama.it.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.aosama.it.models.responses.file.FileResponse;
import com.aosama.it.models.responses.nested.BoardData;
import com.aosama.it.ui.adapter.AttachmentAdapter;
import com.aosama.it.ui.adapter.BoardsAdapter;
import com.aosama.it.ui.adapter.CustomListAdapterAssigneeDialog;
import com.aosama.it.ui.adapter.CustomListAdapterUsersDialog;
import com.aosama.it.ui.adapter.board.HAdapterUsers;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.MyUtilis;
import com.aosama.it.utiles.PreferenceProcessor;
import com.aosama.it.viewmodels.BoardDetailViewModel;
import com.aosama.it.viewmodels.CommentsViewModel;
import com.aosama.it.viewmodels.UploadAttachmentViewModel;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class TasksActivity extends AppCompatActivity implements
        HAdapterUsers.OnUserClicked, BoardsAdapter.OnItemClick, UploadAttachmentViewModel.UploadImageHandler {

    private static final String TAG = "TasksActivity";
    public static final int PICKFILE_RESULT_CODE_BOARD = 200;
    public static final int PICKFILE_RESULT_CODE_TASK = 100;
    @BindView(R.id.rvAllUseres)
    RecyclerView rvAllUsers;
    @BindView(R.id.tvName)
    TextView tvName;
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
    @BindView(R.id.ivPrivate)
    ImageView ivLock;

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
    private String type;

    public void back(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        ButterKnife.bind(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.loading_msg));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMax(100);

        init();
        gettingThePassedBoardModel();

        showDialog();
        fetchingData();

        ivAttachment.setOnClickListener(view1 -> {
            alertBoard = new ViewDialogAttachmentsBoard();
            alertBoard.showDialog(this, nestedBoard.getAttachmentsGeneral());

        });

        ivPeople.setOnClickListener(view12 -> {
            if (userBoards != null && userBoards.size() > 0)
                showDialogUsers((ArrayList<UserBoard>) userBoards);
            else {
                Toast.makeText(this, getString(R.string.no_users_here), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void refreshAdapterUsers() {
        adapterUsers = new HAdapterUsers(this,
                userBoards,
                this);
        rvAllUsers.setAdapter(adapterUsers);
        adapterUsers.notifyDataSetChanged();
    }

    private void showDialogUsers(ArrayList<UserBoard> assigneeList) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        View view = getLayoutInflater().inflate(R.layout.dialog_people, null);

        ListView lv = view.findViewById(R.id.custom_list);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        // Change MyActivity.this and myListOfItems to your own values
        CustomListAdapterUsersDialog clad = new CustomListAdapterUsersDialog(this, assigneeList);

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
                                        if (basicResponseStateData.getData().getData().getBoardData().getNestedBoards() == null) {
                                            Intent intent = new Intent(this, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                            startActivity(intent);
                                            return;
                                        }

                                        if (basicResponseStateData.getData().getData().getBoardData().getNestedBoards() != null &&
                                                basicResponseStateData.getData().getData().getBoardData().getNestedBoards().size() == 0) {
                                            Intent intent = new Intent(this, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                            startActivity(intent);
                                            return;
                                        }
                                        fillViewWithData(basicResponseStateData.getData());
                                        fillTable(basicResponseStateData.getData().getData().getBoardData().getNestedBoards().get(0));
                                        nestedBoard = basicResponseStateData.getData().getData().getBoardData().getNestedBoards().get(0);
                                        boardId = nestedBoard.getId();
                                        tvName.setText(basicResponseStateData.getData().getData().getBoardData().getNestedBoards().get(0).getName());
                                    }
                                    Log.e(TAG, "fetchingData: success");
                                    break;
                                case FAIL:
                                    Log.e(TAG, "fetchingData: failed");
                                    Toast.makeText(this,
                                            basicResponseStateData.getErrorsMessages()
                                                    != null ?
                                                    basicResponseStateData.getErrorsMessages()
                                                            .getErrorMessages().get(0) : null,
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                case ERROR:
                                    Log.e(TAG, "fetchingData: error");
                                    if (basicResponseStateData.getError() != null) {
                                        Toast.makeText(this,
                                                getString(R.string.
                                                        no_connection_msg), Toast.LENGTH_LONG).show();
                                        Log.v("Statues", "Error" + basicResponseStateData
                                                .getError().getMessage());
                                    }
                                    break;
                                case CATCH:
                                    Toast.makeText(this,
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

    private void fillTable(NestedBoard nestedBoard) {
        ArrayList<TaskE> taskES = new ArrayList<>();
        final BoardsAdapter adapter = new BoardsAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        for (int j = 0; j < nestedBoard.getTasksGroup().size(); j++) {
            try {
                nestedBoard.getTasksGroup().get(j).getTasks().get(0).setTableName(nestedBoard.getTasksGroup().get(j).getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
//            taskES.addAll(nestedBoard.getTasksGroup().get(j).getTasks());
            for (TaskE t :
                    nestedBoard.getTasksGroup().get(j).getTasks()) {
                if (!t.isDelete())
                    taskES.add(t);
            }
            adapter.setTasksGroups(taskES);
        }

    }

    private void fireTaskItemComments(TaskE taskE, NestedBoard nestedBoard) {

        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra(Constants.SELECTED_COMMENT, gson.toJson(taskE));
        intent.putExtra(Constants.SELECTED_BORAD, gson.toJson(nestedBoard));

        startActivity(intent);

    }

    private void gettingThePassedBoardModel() {
        if (getIntent() != null) {
//                boardDataList = gson.fromJson(
//                        getArguments().getString(Constants.SELECTED_BORAD), BoardDataList.class);
            id = getIntent().getStringExtra(Constants.SELECTED_BORAD);
            tvName.setText(getIntent().getStringExtra("name"));
            if (getIntent().getBooleanExtra("is_private", false))
                ivLock.setVisibility(View.VISIBLE);
            else ivLock.setVisibility(View.GONE);
        }
    }

    private void init() {
        boardDetailViewModel = ViewModelProviders.of(this)
                .get(BoardDetailViewModel.class);


        rvAllUsers.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        rvAllUsers.setHasFixedSize(false);

    }

    private void showDialog() {
        dialog = MyUtilis.myDialog(this);
        dialog.show();
    }

    private void showDialogPeople(ArrayList<Assignee> assigneeList) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        View view = getLayoutInflater().inflate(R.layout.dialog_people, null);

        ListView lv = view.findViewById(R.id.custom_list);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // Change MyActivity.this and myListOfItems to your own values
        CustomListAdapterAssigneeDialog clad = new CustomListAdapterAssigneeDialog(this, assigneeList);

        lv.setAdapter(clad);
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
                alert.showDialog(this, taskE.getAttachments());
                break;

            case R.id.ivPeople:
                showDialogPeople((ArrayList<Assignee>) taskE.getAssignee());
                break;
            case R.id.tvTaskName:
                showDialogDates(taskE);
        }
    }

    private void showDialogDates(TaskE taskE) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        View view = getLayoutInflater().inflate(R.layout.dialog_dates, null);

        TextView tvStartDate = view.findViewById(R.id.tvStartDate);

        tvStartDate.setText(MyUtilis.formateDate(taskE.getName()));
        dialog.setContentView(view);

        dialog.show();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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

            rv.setLayoutManager(new LinearLayoutManager(activity));
            AttachmentAdapter adapter = new AttachmentAdapter(activity, attachments, this);
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

            rv.setLayoutManager(new LinearLayoutManager(activity));
            AttachmentAdapter adapter = new AttachmentAdapter(activity, attachments, this);
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
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(MyConfig.GET_FILE + "/" + attachment.getAttachId());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.addRequestHeader("Authorization", "Bearer " + PreferenceProcessor.getInstance(TasksActivity.this).getStr(MyConfig.MyPrefs.TOKEN, ""));
            Long ref = downloadManager.enqueue(request);
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
        int dimWH = (int) getResources()
                .getDimension(R.dimen._60sdp);
        int fonsSize =
                (int) getResources()
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
        alert.showDialog(this, userBoard);

    }

    public String getFileName(Uri uri) {

        Cursor mCursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        int indexedname = mCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        mCursor.moveToFirst();
        String filename = mCursor.getString(indexedname);
        mCursor.close();
        return filename;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE_BOARD:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();

                    File file = new File(this.getCacheDir(), getFileName(uri));

                    int maxBufferSize = 1 * 1024 * 1024;

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Log.e("InputStream Size", "Size " + inputStream);
                        int bytesAvailable = inputStream.available();
                        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        final byte[] buffers = new byte[bufferSize];
                        tvAttachment.setText(getFileName(uri));
                        FileOutputStream outputStream = new FileOutputStream(file);
                        int read = 0;
                        while ((read = inputStream.read(buffers)) != -1) {
                            outputStream.write(buffers, 0, read);
                        }
                        Log.e("File Size", "Size " + file.length());
                        inputStream.close();
                        outputStream.close();

                        Log.e("File Path", "Path " + file.getPath());
                        Log.e("File Size", "Size " + file.length());

                        if (file.length() > 0) {
                            UploadAttachmentViewModel uploadAttachmentViewModel = new UploadAttachmentViewModel(this, this);
                            uploadAttachmentViewModel.doUploadAttachment(file);
                            type = "b";
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "cannot open file picker", Toast.LENGTH_SHORT).show();
                }
                break;

            case PICKFILE_RESULT_CODE_TASK:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();

                    File file = new File(this.getCacheDir(), getFileName(uri));

                    int maxBufferSize = 1 * 1024 * 1024;

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Log.e("InputStream Size", "Size " + inputStream);
                        int bytesAvailable = inputStream.available();
                        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        final byte[] buffers = new byte[bufferSize];
                        tvAttachment.setText(getFileName(uri));
                        FileOutputStream outputStream = new FileOutputStream(file);
                        int read = 0;
                        while ((read = inputStream.read(buffers)) != -1) {
                            outputStream.write(buffers, 0, read);
                        }
                        Log.e("File Size", "Size " + file.length());
                        inputStream.close();
                        outputStream.close();

                        Log.e("File Path", "Path " + file.getPath());
                        Log.e("File Size", "Size " + file.length());

                        if (file.length() > 0) {
                            UploadAttachmentViewModel uploadAttachmentViewModel = new UploadAttachmentViewModel(this, this);
                            uploadAttachmentViewModel.doUploadAttachment(file);
                            type = "t";
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "cannot open file picker", Toast.LENGTH_SHORT).show();
                }
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onProgressUpdate(int percentage) {
        mProgressDialog.show();
        mProgressDialog.setProgress(percentage);
    }

    @Override
    public void onError() {
        mProgressDialog.dismiss();
        Toasty.error(this, getString(R.string.ef_error_create_image_file)).show();

    }

    @Override
    public void onFinish(BasicResponse<FileResponse> imageResponse) {
        mProgressDialog.setProgress(100);
        mProgressDialog.dismiss();
        Toasty.success(this, getString(R.string.success)).show();
        attachName = imageResponse.getData().getAttachName();
        attachKey = imageResponse.getData().getAttachKey();
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("attachName", attachName);
            jsonBody.put("attachKey", attachKey);
            jsonBody.put("isPrivate", false);
            jsonBody.put("type", type);

        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog dialog = MyUtilis.myDialog(this);
        dialog.show();
        CommentsViewModel viewModel = ViewModelProviders.of(this).get(CommentsViewModel.class);
        String id = null;
        if (type.equals("b"))
            id = boardId;
        else if (type.equals("t"))
            id = taskId;
        viewModel.putComment(MyConfig.ADD_ATTACH_GENERAL + id, jsonBody).observe(this, basicResponseStateData -> {
            dialog.dismiss();
            switch (basicResponseStateData.getStatus()) {
                case SUCCESS:
                    if (basicResponseStateData.getData() != null) {
                        if (type.equals("t"))
                            alert.dismiss();
                        else if (type.equals("b"))
                            alertBoard.dismiss();

                        Toast.makeText(this, basicResponseStateData.getData().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    break;
                case FAIL:
                    Toast.makeText(this, basicResponseStateData.getErrorsMessages() != null ? basicResponseStateData.getErrorsMessages().getErrorMessages().get(0) : null, Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    if (basicResponseStateData.getError() != null) {
                        Toast.makeText(this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                        Log.v("Statues", "Error" + basicResponseStateData.getError().getMessage());
                    }
                    break;
                case CATCH:
                    Toast.makeText(this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                    break;
            }
        });

    }
}
