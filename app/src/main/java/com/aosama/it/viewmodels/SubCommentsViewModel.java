package com.aosama.it.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.boards.CommentGroup;
import com.aosama.it.models.wrappers.StateLiveData;
import com.aosama.it.repository.CommentsRepository;
import com.aosama.it.repository.SubCommentsRepository;

import java.util.List;

public class SubCommentsViewModel extends AndroidViewModel {
    private SubCommentsRepository repository;

    public SubCommentsViewModel(@NonNull Application application) {
        super(application);
        repository = new SubCommentsRepository(application);
    }

    public StateLiveData<BasicResponse<CommentGroup>> getSubComments(String url) {
        return repository.getSubComments(url);
    }

}
