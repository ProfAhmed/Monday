package com.aosama.it.viewmodels;

import android.app.Application;

import com.aosama.it.models.*;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.boards.DataBoards;
import com.aosama.it.models.responses.sing_in.DataSignIn;
import com.aosama.it.models.wrappers.StateLiveData;
import com.aosama.it.repository.BasicResponseRepository;
import com.aosama.it.repository.BoardsRepository;
import org.json.JSONObject;

public class HomeViewModel extends AndroidViewModel {
    private BoardsRepository repository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new BoardsRepository(application);
    }

    public StateLiveData<BasicResponse<DataBoards>> boards(String url) {
        return repository.getBoards(url);
    }
}
