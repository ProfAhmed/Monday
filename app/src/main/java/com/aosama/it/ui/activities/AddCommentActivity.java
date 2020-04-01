package com.aosama.it.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aosama.it.R;
import com.aosama.it.constants.Constants;
import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.ImageResponse;
import com.aosama.it.models.responses.boards.Assignee;
import com.aosama.it.models.responses.boards.NestedBoard;
import com.aosama.it.models.responses.boards.UserBoard;
import com.aosama.it.models.responses.file.FileResponse;
import com.aosama.it.ui.adapter.UserAdapter;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.MyUtilis;
import com.aosama.it.viewmodels.BasicResponsePostViewModel;
import com.aosama.it.viewmodels.UploadAttachmentViewModel;
import com.google.android.material.button.MaterialButton;
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

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class AddCommentActivity extends AppCompatActivity implements QueryTokenReceiver, SuggestionsResultListener, UserAdapter.OnUserClicked, SuggestionsVisibilityManager, UploadAttachmentViewModel.UploadImageHandler {

    private static final int PICKFILE_RESULT_CODE = 100;
    @BindView(R.id.btnUpdate)
    MaterialButton btnUpdate;
    @BindView(R.id.tvAttachmentName)
    TextView tvAttachmentName;
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
    private ProgressDialog mProgressDialog;

    StringBuilder userStringBuilder = new StringBuilder();

    BasicResponsePostViewModel viewModel;
    private NestedBoard nestedBoard;
    private String attachName, attachKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        ButterKnife.bind(this);
        Gson gson = new Gson();

        nestedBoard = gson.fromJson(
                getIntent().getStringExtra(Constants.SELECTED_USER), NestedBoard.class);

        viewModel = ViewModelProviders.of(this).get(BasicResponsePostViewModel.class);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.loading_msg));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMax(100);

        recyclerView = findViewById(R.id.mentions_grid);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(this, new ArrayList<Assignee>(), this);
        recyclerView.setAdapter(adapter);

        editor = findViewById(R.id.editor);
        editor.setTokenizer(new WordTokenizer(tokenizerConfig));
        editor.setQueryTokenReceiver(this);
        editor.setSuggestionsVisibilityManager(this);
        editor.setHint(getResources().getString(R.string.type_comment));


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userIds = null;
                if (userStringBuilder.length() > 0) {
                    userIds = userStringBuilder.substring(0, userStringBuilder.lastIndexOf(","));
                    Log.d("UserIds", userIds);
                }
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("taskId", getIntent().getStringExtra(Constants.TASK_ID));
                    jsonBody.put("commentData", editor.getText().toString());
                    jsonBody.put("attachName", attachName);
                    jsonBody.put("attachKey", attachKey);
                    jsonBody.put("isPrivate", false);
                    if (userIds != null)
                        jsonBody.put("mentionUsers", userIds);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                AlertDialog dialog = MyUtilis.myDialog(AddCommentActivity.this);
                dialog.show();

                viewModel.basicResponseStateLiveData(MyConfig.ADD_COMMENT, jsonBody).observe(AddCommentActivity.this, basicResponseStateData -> {
                    dialog.dismiss();
                    switch (basicResponseStateData.getStatus()) {
                        case SUCCESS:
                            if (basicResponseStateData.getData() != null) {
                                Toast.makeText(AddCommentActivity.this, basicResponseStateData.getData().getMessage(), Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(AddCommentActivity.this, CommentsActivity.class);
//                                intent.putExtra(Constants.TASK_ID, getIntent().getStringExtra(Constants.TASK_ID));
//                                startActivity(intent);
                                setResult(RESULT_OK);
                                finish();
                            }
                            break;
                        case FAIL:
                            Toast.makeText(AddCommentActivity.this, basicResponseStateData.getErrorsMessages() != null ? basicResponseStateData.getErrorsMessages().getErrorMessages().get(0) : null, Toast.LENGTH_SHORT).show();
                            break;
                        case ERROR:
                            if (basicResponseStateData.getError() != null) {
                                Toast.makeText(AddCommentActivity.this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                                Log.v("Statues", "Error" + basicResponseStateData.getError().getMessage());
                            }
                            break;
                        case CATCH:
                            Toast.makeText(AddCommentActivity.this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                            break;
                    }
                });
            }
        });

        editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("@"))
                    recyclerView.setVisibility(View.GONE);
                if (charSequence.length() > 0)
                    btnUpdate.setEnabled(true);
                else
                    btnUpdate.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0)
                    btnUpdate.setEnabled(true);
                else
                    btnUpdate.setEnabled(false);
            }
        });

    }


    public void back(View v) {
        onBackPressed();
    }

    public void selectAttachment(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Copy"),
                    PICKFILE_RESULT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
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

                    tvAttachmentName.setText(getFileName(uri));
                    int maxBufferSize = 1 * 1024 * 1024;

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Log.e("InputStream Size", "Size " + inputStream);
                        int bytesAvailable = inputStream.available();
                        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        final byte[] buffers = new byte[bufferSize];

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
    public List<String> onQueryReceived(@NonNull QueryToken queryToken) {
        List<String> buckets = Collections.singletonList(BUCKET);

        SuggestionsResult result = new SuggestionsResult(queryToken, nestedBoard.getUsers());
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

    @Override
    public void onUserClicked(View view, int position, UserBoard userBoard) {
        editor.insertMention(userBoard);
        recyclerView.swapAdapter(new UserAdapter(this, new ArrayList<Assignee>(), this), true);
        displaySuggestions(false);
        editor.requestFocus();
        userStringBuilder.append(userBoard.getId()).append(",");
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
        attachName = imageResponse.getData().getAttachName();
        attachKey = imageResponse.getData().getAttachKey();

        Toasty.success(this, getString(R.string.success)).show();
    }

}
