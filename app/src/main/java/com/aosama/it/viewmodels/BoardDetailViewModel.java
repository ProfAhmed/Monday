package com.aosama.it.viewmodels;

import android.app.Application;

import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.nested.BoardData;
import com.aosama.it.models.wrappers.StateLiveData;
import com.aosama.it.repository.BoardNestedRepository;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class BoardDetailViewModel extends AndroidViewModel {
    private BoardNestedRepository repository;

    public BoardDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new BoardNestedRepository(application);
    }

    public StateLiveData<BasicResponse<BoardData>>
    getBoardDetails(String url, HashMap<String, String> params) {
        return repository.getBoardDetails(url, params);
    }
}
