package com.aosama.it.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.boards.CommentGroup;
import com.aosama.it.models.responses.boards.UserBoard;
import com.aosama.it.models.responses.nested.BoardData;
import com.aosama.it.models.wrappers.StateLiveData;
import com.aosama.it.repository.BoardNestedRepository;
import com.aosama.it.repository.CommentsRepository;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class CommentsViewModel extends AndroidViewModel {
    private CommentsRepository repository;

    public CommentsViewModel(@NonNull Application application) {
        super(application);
        repository = new CommentsRepository(application);
    }

    public StateLiveData<BasicResponse<List<CommentGroup>>> getComments(String url) {
        return repository.getComments(url);
    }

    public StateLiveData<BasicResponse<List<UserBoard>>> getUsers(String url) {
        return repository.getUsers(url);
    }

    public StateLiveData<BasicResponse> putComment(String url, JSONObject jsonBody) {
        return repository.putComment(url, jsonBody);
    }
}
