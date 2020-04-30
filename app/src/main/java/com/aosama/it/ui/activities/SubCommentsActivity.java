package com.aosama.it.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.aosama.it.R;
import com.aosama.it.constants.Constants;
import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.boards.Assignee;
import com.aosama.it.models.responses.boards.Attachment;
import com.aosama.it.models.responses.boards.CommentGroup;
import com.aosama.it.models.responses.boards.NestedBoard;
import com.aosama.it.models.responses.boards.NestedComment;
import com.aosama.it.models.responses.boards.TaskE;
import com.aosama.it.models.responses.boards.UserBoard;
import com.aosama.it.models.responses.file.FileResponse;
import com.aosama.it.models.wrappers.StateData;
import com.aosama.it.repository.CommentsRepository;
import com.aosama.it.repository.SubCommentsRepository;
import com.aosama.it.ui.adapter.AttachmentAdapter;
import com.aosama.it.ui.adapter.CommentAdapter;
import com.aosama.it.ui.adapter.SubCommentsAdapter;
import com.aosama.it.ui.adapter.UserAdapter;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.MyUtilis;
import com.aosama.it.utiles.PreferenceProcessor;
import com.aosama.it.viewmodels.BasicResponsePostViewModel;
import com.aosama.it.viewmodels.CommentsViewModel;
import com.aosama.it.viewmodels.SubCommentsViewModel;
import com.aosama.it.viewmodels.UploadAttachmentViewModel;
import com.google.gson.Gson;
import com.linkedin.android.spyglass.suggestions.SuggestionsResult;
import com.linkedin.android.spyglass.suggestions.interfaces.Suggestible;
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsResultListener;
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsVisibilityManager;
import com.linkedin.android.spyglass.tokenization.QueryToken;
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizer;
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizerConfig;
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver;
import com.linkedin.android.spyglass.ui.MentionsEditText;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class SubCommentsActivity extends AppCompatActivity implements CommentAdapter.OnAttachClicked, SubCommentsAdapter.OnAttachClicked, UploadAttachmentViewModel.UploadImageHandler,
        QueryTokenReceiver, SuggestionsResultListener, UserAdapter.OnUserClicked, SuggestionsVisibilityManager {
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvCommentData)
    TextView tvCommentData;
    @BindView(R.id.tvReply)
    TextView tvReply;
    @BindView(R.id.ivUserImagePhoto)
    ImageView userPhoto;
    @BindView(R.id.ivAttachment)
    ImageView ivAttachment;
    @BindView(R.id.ivSelection)
    ImageView ivSelection;
    @BindView(R.id.ivSend)
    ImageView ivSend;
    @BindView(R.id.rv)
    RecyclerView rv;

    private ProgressDialog mProgressDialog;
    private TextView tvAttachment;
    private String attachName, attachKey;
    private SubCommentsViewModel viewModel;
    private String taskId;
    private String commentId;
    private ViewDialog alert;//dialog for attachments

    private static final String BUCKET = "people-network";
    private static final WordTokenizerConfig tokenizerConfig = new WordTokenizerConfig
            .Builder()
            .setExplicitChars("@")
            .setThreshold(100)
            .setMaxNumKeywords(6)
            .build();

    private RecyclerView recyclerView;
    private MentionsEditText editor;
    private UserAdapter adapter;
    NestedBoard nestedBoard;
    StringBuilder userStringBuilder = new StringBuilder();
    private CommentGroup commentGroup;
    PowerMenu powerMenu;
    private String commentData;
    private boolean isUpdateComment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_comments);
        ButterKnife.bind(this);

        Gson gson = new Gson();
        nestedBoard = gson.fromJson(
                getIntent().getStringExtra(Constants.SELECTED_BORAD), NestedBoard.class);

        viewModel = ViewModelProviders.of(this).get(SubCommentsViewModel.class);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.loading_msg));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMax(100);

        commentId = getIntent().getStringExtra(Constants.SELECTED_COMMENT);
        getSubComments(commentId);

        recyclerView = findViewById(R.id.mentions_grid);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(this, new ArrayList<Assignee>(), this);
        recyclerView.setAdapter(adapter);

        editor = findViewById(R.id.editor);
        editor.setTokenizer(new WordTokenizer(tokenizerConfig));
        editor.setQueryTokenReceiver(this);
        editor.setSuggestionsVisibilityManager(this);
        editor.setHint(getResources().getString(R.string.type_comment));
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userIds = null;
                if (userStringBuilder.length() > 0) {
                    userIds = userStringBuilder.substring(0, userStringBuilder.lastIndexOf(","));
                    Log.d("UserIds", userIds);
                }
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("commentData", editor.getText().toString());
                    if (userIds != null)
                        jsonBody.put("mentionUsers", userIds);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                AlertDialog dialog = MyUtilis.myDialog(SubCommentsActivity.this);
                dialog.show();
                BasicResponsePostViewModel viewModel = ViewModelProviders.of(SubCommentsActivity.this).get(BasicResponsePostViewModel.class);
                if (isUpdateComment) {
                    isUpdateComment = false;
                    SubCommentsViewModel viewModel1 = ViewModelProviders.of(SubCommentsActivity.this).get(SubCommentsViewModel.class);
                    viewModel1.puComment(MyConfig.ADD_COMMENT + "/" + commentId, jsonBody).observe(SubCommentsActivity.this, basicResponseStateData -> {
                        dialog.dismiss();
                        switch (basicResponseStateData.getStatus()) {
                            case SUCCESS:
                                if (basicResponseStateData.getData() != null) {
                                    Toast.makeText(SubCommentsActivity.this, basicResponseStateData.getData().getMessage(), Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(AddCommentActivity.this, CommentsActivity.class);
//                                intent.putExtra(Constants.TASK_ID, getIntent().getStringExtra(Constants.TASK_ID));
//                                startActivity(intent);
                                    tvCommentData.setText(editor.getText().toString());
                                    editor.setText("");
//                                    getSubComments(commentId);
                                }
                                break;
                            case FAIL:
                                Toast.makeText(SubCommentsActivity.this, basicResponseStateData.getErrorsMessages() != null ? basicResponseStateData.getErrorsMessages().getErrorMessages().get(0) : null, Toast.LENGTH_SHORT).show();
                                editor.setText("");
                                break;
                            case ERROR:
                                if (basicResponseStateData.getError() != null) {
                                    editor.setText("");
                                    Toast.makeText(SubCommentsActivity.this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                                    Log.v("Statues", "Error" + basicResponseStateData.getError().getMessage());
                                }
                                break;
                            case CATCH:
                                editor.setText("");
                                Toast.makeText(SubCommentsActivity.this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                                break;
                        }
                    });
                } else {
                    try {
                        jsonBody.put("commentId", commentId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    viewModel.basicResponseStateLiveData(MyConfig.ADD_SUB_COMMENT, jsonBody).observe(SubCommentsActivity.this, basicResponseStateData -> {
                        dialog.dismiss();
                        switch (basicResponseStateData.getStatus()) {
                            case SUCCESS:
                                if (basicResponseStateData.getData() != null) {
                                    Toast.makeText(SubCommentsActivity.this, basicResponseStateData.getData().getMessage(), Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(AddCommentActivity.this, CommentsActivity.class);
//                                intent.putExtra(Constants.TASK_ID, getIntent().getStringExtra(Constants.TASK_ID));
//                                startActivity(intent);
                                    editor.setText("");
                                    getSubComments(commentId);
                                }
                                break;
                            case FAIL:
                                Toast.makeText(SubCommentsActivity.this, basicResponseStateData.getErrorsMessages() != null ? basicResponseStateData.getErrorsMessages().getErrorMessages().get(0) : null, Toast.LENGTH_SHORT).show();
                                break;
                            case ERROR:
                                if (basicResponseStateData.getError() != null) {
                                    Toast.makeText(SubCommentsActivity.this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                                    Log.v("Statues", "Error" + basicResponseStateData.getError().getMessage());
                                }
                                break;
                            case CATCH:
                                Toast.makeText(SubCommentsActivity.this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                                break;
                        }
                    });
                }
            }
        });
    }

    private void getSubComments(String commentId) {
        List<NestedComment> commentGroups = new ArrayList<>();

        AlertDialog dialog = MyUtilis.myDialog(this);
        dialog.show();
        viewModel.getSubComments(MyConfig.SUBCOMMENTS_URL + commentId).observe(this, basicResponseStateData -> {

            dialog.dismiss();
            switch (basicResponseStateData.getStatus()) {
                case SUCCESS:
                    commentGroup = basicResponseStateData.getData().getData();
                    String userId = PreferenceProcessor.getInstance(this).getStr(MyConfig.MyPrefs.USER_ID, "");
                    if (userId.equals(commentGroup.getById()))
                        ivSelection.setVisibility(View.VISIBLE);
                    ivAttachment.setOnClickListener(view -> {
                        alert = new ViewDialog();
                        alert.showDialog(this, commentGroup.getAttachments());
                    });
                    for (NestedComment nestedComment : basicResponseStateData.getData().getData().getNestedComments()) {
                        if (!nestedComment.isDelete())
                            commentGroups.add(nestedComment);
                    }
                    commentData = commentGroup.getCommentData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvCommentData.setText(Html.fromHtml(commentGroup.getCommentData(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvCommentData.setText(Html.fromHtml(commentGroup.getCommentData()));
                    }
                    String path = commentGroup.getByUserImage();
                    if (!TextUtils.isEmpty(path) && path != null
                            && path.length() > 0) {
                        Picasso.get().load(path)
                                .into(userPhoto, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });
                    } else {
                        String firstChar = "";
                        if (commentGroup.getByShortName().length() > 0) {
                            TextDrawable drawable2 = createTextDrawable(commentGroup.getByShortName());
                            userPhoto.setImageDrawable(drawable2);
                        }
                    }

                    SubCommentsAdapter adapter = new SubCommentsAdapter(this, commentGroups, this);
                    rv.setLayoutManager(new LinearLayoutManager(this));
                    rv.setAdapter(adapter);

                    break;
                case FAIL:
                    Toast.makeText(this, basicResponseStateData.getErrorsMessages() != null ? basicResponseStateData.getErrorsMessages().getErrorMessages().get(0) : null, Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    if (basicResponseStateData.getError() != null) {
//                            Toast.makeText(this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                        Log.v("Statues", "Error" + basicResponseStateData.getError().getMessage());
                    }
                    break;
                case CATCH:
                    Toast.makeText(this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                    break;
            }
        });

    }

    private TextDrawable createTextDrawable(String firstChar) {
//        TextDrawable drawable1 = TextDrawable.builder()
//                .buildRoundRect(firstChar, Color.RED, 10);
        int dimWH = (int) getResources()
                .getDimension(R.dimen._60sdp);
        int fonsSize =
                (int) getResources()
                        .getDimension(R.dimen._20ssp);
        return TextDrawable.builder()
                .beginConfig()
                .fontSize(fonsSize)
                .width(dimWH)  // width in px
                .height(dimWH) // height in px
                .endConfig()
                .buildRect(firstChar, Color.RED);
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent returnIntent) {
        switch (requestCode) {
            case ViewDialog.PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = returnIntent.getData();

                    File file = new File(getCacheDir(), getFileName(uri));

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
        super.onActivityResult(requestCode, resultCode, returnIntent);

    }

    @Override
    public void onUserClicked(View view, int position, List<Attachment> attachments, String commentId) {

    }

    private String getFileName(Uri uri) {

        Cursor mCursor =
                getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        int indexedname = mCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        mCursor.moveToFirst();
        String filename = mCursor.getString(indexedname);
        mCursor.close();
        return filename;
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
            jsonBody.put("type", "c");

        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog dialog = MyUtilis.myDialog(this);
        dialog.show();
        CommentsViewModel commentsViewModel = ViewModelProviders.of(this).get(CommentsViewModel.class);

        commentsViewModel.putComment(MyConfig.ADD_ATTACH_GENERAL + commentId, jsonBody).observe(this, basicResponseStateData -> {
            dialog.dismiss();
            switch (basicResponseStateData.getStatus()) {
                case SUCCESS:
                    if (basicResponseStateData.getData() != null) {
                        alert.dismiss();
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

    @Override
    public void onUserClicked(View view, int position, UserBoard userBoard) {
        editor.insertMention(userBoard);
        recyclerView.swapAdapter(new UserAdapter(this, new ArrayList<Assignee>(), this), true);
        displaySuggestions(false);
        editor.requestFocus();
        userStringBuilder.append(userBoard.getId2()).append(",");

    }

    @Override
    public List<String> onQueryReceived(@NonNull QueryToken queryToken) {
        List<String> buckets = Collections.singletonList(BUCKET);

        SuggestionsResult result = new SuggestionsResult(queryToken, MyConfig.userBoards);
        // Have suggestions, now call the listener (which is this activity)
        onReceiveSuggestionsResult(result, BUCKET);
        return buckets;
    }

    // --------------------------------------------------
    // SuggestionsResultListener Implementation
    // --------------------------------------------------

    @Override
    public void onReceiveSuggestionsResult(@NonNull SuggestionsResult result, @NonNull String bucket) {
        List<? extends Suggestible> suggestions = result.getSuggestions();
        adapter = new UserAdapter(this, result.getSuggestions(), this);
        recyclerView.swapAdapter(adapter, true);
        boolean display = suggestions != null && suggestions.size() > 0;
        displaySuggestions(display);
    }

    // --------------------------------------------------
    // SuggestionsManager Implementation
    // --------------------------------------------------

    @Override
    public void displaySuggestions(boolean display) {
        if (display) {
            recyclerView.setVisibility(RecyclerView.VISIBLE);
        } else {
            recyclerView.setVisibility(RecyclerView.GONE);
        }
    }

    @Override
    public boolean isDisplayingSuggestions() {
        return recyclerView.getVisibility() == RecyclerView.VISIBLE;
    }

    public class ViewDialog implements AttachmentAdapter.OnUserClicked {
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

            rv.setLayoutManager(new LinearLayoutManager(SubCommentsActivity.this));
            AttachmentAdapter adapter = new AttachmentAdapter(SubCommentsActivity.this, attachments, this);
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
                                PICKFILE_RESULT_CODE);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(activity, "Please install a File Manager.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
        }

        void dismiss() {
            if (dialog != null)
                dialog.dismiss();
        }

        @Override
        public void onUserClicked(View view, int position, Attachment attachment) {
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(MyConfig.GET_FILE + "/" + attachment.getAttachId());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.addRequestHeader("Authorization", "Bearer " + PreferenceProcessor.getInstance(SubCommentsActivity.this).getStr(MyConfig.MyPrefs.TOKEN, ""));
            Long ref = downloadManager.enqueue(request);
        }
    }


    @SuppressLint("ResourceType")
    public void popmenu(View view) {
        String information = getString(R.string.update);
        String logout = getString(R.string.delete);
        List<PowerMenuItem> list = new ArrayList<>();
        list.add(new PowerMenuItem(information));
        list.add(new PowerMenuItem(logout));
        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = (position, item) -> {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
            powerMenu.dismiss();
            switch (position) {
                case 0:
                    if (commentData != null) {
                        editor.requestFocus();
                        editor.setText(Html.fromHtml(commentData));
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editor, InputMethodManager.SHOW_IMPLICIT);
                        isUpdateComment = true;
                    }
                    break;
                case 1:
                    AlertDialog dialog = MyUtilis.myDialog(SubCommentsActivity.this);

                    dialog.show();
                    viewModel.deleteComment(MyConfig.COMMENTS_URL + commentId, null).observe(this, new Observer<StateData<BasicResponse>>() {
                        @Override
                        public void onChanged(StateData<BasicResponse> basicResponseStateData) {
                            dialog.dismiss();
                            switch (basicResponseStateData.getStatus()) {
                                case SUCCESS:
                                    if (basicResponseStateData.getData() != null) {
                                        Toast.makeText(SubCommentsActivity.this, basicResponseStateData.getData().getMessage(), Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(AddCommentActivity.this, CommentsActivity.class);
//                                intent.putExtra(Constants.TASK_ID, getIntent().getStringExtra(Constants.TASK_ID));
//                                startActivity(intent);
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                    break;
                                case FAIL:
                                    Toast.makeText(SubCommentsActivity.this, basicResponseStateData.getErrorsMessages() != null ? basicResponseStateData.getErrorsMessages().getErrorMessages().get(0) : null, Toast.LENGTH_SHORT).show();
                                    break;
                                case ERROR:
                                    if (basicResponseStateData.getError() != null) {
                                        Toast.makeText(SubCommentsActivity.this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                                        Log.v("Statues", "Error" + basicResponseStateData.getError().getMessage());
                                    }
                                    break;
                                case CATCH:
                                    Toast.makeText(SubCommentsActivity.this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    });
                    break;
            }
        };
        powerMenu = new PowerMenu.Builder(this)
                .addItemList(list)
                .setAnimation(MenuAnimation.SHOWUP_BOTTOM_LEFT) // Animation start point (TOP | LEFT)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setTextColor(getResources().getColor(R.color.black))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(getResources().getColor(R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();
//        powerMenu.showAsDropDown(view); // view is an anchor
        powerMenu.showAsDropDown(view, 0, 0);
//        powerMenu.showAsAnchorLeftBottom(view); // showing the menu by left-bottom align about the anchor view.
        powerMenu.showAsAnchorLeftTop(view);// showing the menu by left-top align about the anchor view.
//        powerMenu.showAsAnchorRightTop(view); // showing the menu by rgiht-bottom align about the anchor view.
//        powerMenu.showAsAnchorRightBottom(view); // showing the menu by rgiht-top align about the anchor view.
    }
}
