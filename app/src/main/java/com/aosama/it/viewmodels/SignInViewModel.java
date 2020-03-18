package com.aosama.it.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.sing_in.DataSignIn;
import com.aosama.it.models.wrappers.StateLiveData;
import com.aosama.it.repository.SignInRepository;

import org.json.JSONObject;

public class SignInViewModel extends AndroidViewModel {
    private SignInRepository repository;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        repository = new SignInRepository(application);
    }

    public StateLiveData<BasicResponse<DataSignIn>> signIn(String url, JSONObject jsonBody) {
        return repository.singIn(url, jsonBody);
    }
}
