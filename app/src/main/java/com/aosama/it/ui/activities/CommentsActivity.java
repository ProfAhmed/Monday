package com.aosama.it.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aosama.it.R;
import com.aosama.it.constants.Constants;
import com.aosama.it.models.responses.boards.BoardDataList;
import com.aosama.it.models.responses.boards.CommentGroup;
import com.aosama.it.models.responses.boards.TaskE;
import com.aosama.it.ui.adapter.CommentAdapter;
import com.aosama.it.ui.adapter.InboxAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsActivity extends AppCompatActivity {
    @BindView(R.id.linearBc)
    LinearLayout linearBc;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    TaskE taskE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        Gson gson = new Gson();
        taskE = gson.fromJson(
                getIntent().getStringExtra(Constants.SELECTED_COMMENT), TaskE.class);
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
        CommentAdapter adapter = new CommentAdapter(this, commentGroups);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void back(View view) {
        onBackPressed();
    }
}
