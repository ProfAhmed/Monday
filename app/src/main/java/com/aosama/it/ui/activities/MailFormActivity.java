package com.aosama.it.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aosama.it.R;
import com.aosama.it.constants.Constants;
import com.aosama.it.models.responses.boards.Assignee;
import com.aosama.it.models.responses.boards.NestedBoard;
import com.aosama.it.models.responses.boards.UserBoard;
import com.aosama.it.ui.adapter.UserAdapter;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.MyUtilis;
import com.aosama.it.viewmodels.BasicResponsePostViewModel;
import com.aosama.it.viewmodels.CommentsViewModel;
import com.aosama.it.viewmodels.UploadAttachmentViewModel;
import com.google.android.material.button.MaterialButton;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MailFormActivity extends AppCompatActivity implements QueryTokenReceiver, SuggestionsResultListener, UserAdapter.OnUserClicked, SuggestionsVisibilityManager {

    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.btnSend)
    MaterialButton btnSend;

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
        setContentView(R.layout.activity_mail_form);
        ButterKnife.bind(this);

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

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userIds = null;
                if (userStringBuilder.length() > 0) {
                    userIds = userStringBuilder.substring(0, userStringBuilder.lastIndexOf(","));
                    Log.d("UserIds", userIds);
                }
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("title", etTitle.getText().toString());
                    jsonBody.put("body", etMessage.getText().toString());
//                    jsonBody.put("attachName", attachName);
//                    jsonBody.put("attachKey", attachKey);
                    jsonBody.put("isPrivate", false);
                    if (userIds != null)
                        jsonBody.put("users", userIds);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                AlertDialog dialog = MyUtilis.myDialog(MailFormActivity.this);
                dialog.show();

                viewModel.basicResponseStateLiveData(MyConfig.ADD_MAIL, jsonBody).observe(MailFormActivity.this, basicResponseStateData -> {
                    dialog.dismiss();
                    switch (basicResponseStateData.getStatus()) {
                        case SUCCESS:
                            if (basicResponseStateData.getData() != null) {
                                Toast.makeText(MailFormActivity.this, basicResponseStateData.getData().getMessage(), Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(AddCommentActivity.this, CommentsActivity.class);
//                                intent.putExtra(Constants.TASK_ID, getIntent().getStringExtra(Constants.TASK_ID));
//                                startActivity(intent);
                                setResult(RESULT_OK);
                                finish();
                            }
                            break;
                        case FAIL:
                            Toast.makeText(MailFormActivity.this, basicResponseStateData.getErrorsMessages() != null ? basicResponseStateData.getErrorsMessages().getErrorMessages().get(0) : null, Toast.LENGTH_SHORT).show();
                            break;
                        case ERROR:
                            if (basicResponseStateData.getError() != null) {
                                Toast.makeText(MailFormActivity.this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                                Log.v("Statues", "Error" + basicResponseStateData.getError().getMessage());
                            }
                            break;
                        case CATCH:
                            Toast.makeText(MailFormActivity.this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
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
                    btnSend.setEnabled(true);
                else
                    btnSend.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0)
                    btnSend.setEnabled(true);
                else
                    btnSend.setEnabled(false);
            }
        });

        if (MyConfig.userBoards == null) {
            AlertDialog dialog2 = MyUtilis.myDialog(this);
            dialog2.show();
            CommentsViewModel viewModel2 = ViewModelProviders.of(this).get(CommentsViewModel.class);

            viewModel2.getUsers(MyConfig.GET_users_URL).observe(this, basicResponseStateData -> {

                dialog2.dismiss();
                switch (basicResponseStateData.getStatus()) {
                    case SUCCESS:
                        if (basicResponseStateData.getData().getData() != null)
                            MyConfig.userBoards = basicResponseStateData.getData().getData();
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

    @Override
    public void onUserClicked(View view, int position, UserBoard userBoard) {
        editor.insertMention(userBoard);
        recyclerView.swapAdapter(new UserAdapter(this, new ArrayList<Assignee>(), this), true);
        displaySuggestions(false);
        editor.requestFocus();
        userStringBuilder.append(userBoard.getId2()).append(",");
    }

}
