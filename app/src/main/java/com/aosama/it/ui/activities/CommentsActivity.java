package com.aosama.it.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aosama.it.R;
import com.aosama.it.constants.Constants;
import com.aosama.it.models.responses.ImageResponse;
import com.aosama.it.models.responses.boards.Attachment;
import com.aosama.it.models.responses.boards.BoardDataList;
import com.aosama.it.models.responses.boards.CommentGroup;
import com.aosama.it.models.responses.boards.NestedBoard;
import com.aosama.it.models.responses.boards.TaskE;
import com.aosama.it.ui.adapter.AttachmentAdapter;
import com.aosama.it.ui.adapter.CommentAdapter;
import com.aosama.it.ui.adapter.InboxAdapter;
import com.aosama.it.viewmodels.UploadAttachmentViewModel;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class CommentsActivity extends AppCompatActivity implements UploadAttachmentViewModel.UploadImageHandler, CommentAdapter.OnAttachClicked {
    private static final int PICKFILE_RESULT_CODE = 100;
    @BindView(R.id.linearBc)
    LinearLayout linearBc;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    NestedBoard nestedBoard;
    TaskE taskE;

    private ProgressDialog mProgressDialog;
    private TextView tvAttachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.loading_msg));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMax(100);

        Gson gson = new Gson();
        taskE = gson.fromJson(
                getIntent().getStringExtra(Constants.SELECTED_COMMENT), TaskE.class);
        nestedBoard = gson.fromJson(
                getIntent().getStringExtra(Constants.SELECTED_BORAD), NestedBoard.class);
        if (taskE.getComments() == null)
            return;
        if (taskE.getComments().isEmpty())
            return;

        linearBc.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        List<CommentGroup> commentGroups = new ArrayList<>();
        for (CommentGroup commentGroup : taskE.getComments()) {
            if (!commentGroup.isDelete())
                commentGroups.add(commentGroup);
        }
        CommentAdapter adapter = new CommentAdapter(this, commentGroups, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void back(View view) {
        onBackPressed();
    }

    public void addComment(View view) {
        Intent intent = new Intent(this, AddCommentActivity.class);
        Gson gson = new Gson();
        intent.putExtra(Constants.SELECTED_USER, gson.toJson(nestedBoard));
        intent.putExtra(Constants.TASK_ID, taskE.getId2());
        startActivity(intent);
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
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent returnIntent) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
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
//                            UploadAttachmentViewModel uploadAttachmentViewModel = new UploadAttachmentViewModel(this, this);
//                            uploadAttachmentViewModel.doUploadAttachment(file);
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
    public void onFinish(ImageResponse imageResponse) {
        mProgressDialog.setProgress(100);
        mProgressDialog.dismiss();
        Toasty.success(this, getString(R.string.success)).show();
    }

    @Override
    public void onUserClicked(View view, int position, List<Attachment> attachments) {
        ViewDialog alert = new ViewDialog();
        alert.showDialog(this, attachments);
    }

    public class ViewDialog implements AttachmentAdapter.OnUserClicked {
        private static final int PICKFILE_RESULT_CODE = 100;

        void showDialog(Activity activity, List<Attachment> attachments) {
            final Dialog dialog = new Dialog(activity);
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

            rv.setLayoutManager(new LinearLayoutManager(CommentsActivity.this));
            AttachmentAdapter adapter = new AttachmentAdapter(CommentsActivity.this, attachments, this);
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

        @Override
        public void onUserClicked(View view, int position, Attachment attachment) {

        }
    }

}
