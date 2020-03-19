package com.aosama.it.viewmodels;


import android.app.Application;

import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.wrappers.StateLiveData;
import com.aosama.it.repository.BasicResponseRepository;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class BasicResponsePostViewModel extends AndroidViewModel {
    private BasicResponseRepository repository;

    public BasicResponsePostViewModel(@NonNull Application application) {
        super(application);
        repository = new BasicResponseRepository(application);
    }

    public StateLiveData<BasicResponse> basicResponseStateLiveData(String url, JSONObject jsonBody) {
        return repository.basicResponsePostApi(url, jsonBody);
    }


}