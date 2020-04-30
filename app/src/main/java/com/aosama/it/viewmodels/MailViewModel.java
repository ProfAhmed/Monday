package com.aosama.it.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.mail.DataMail;
import com.aosama.it.models.responses.notifications.NotificationData;
import com.aosama.it.models.wrappers.StateLiveData;
import com.aosama.it.repository.MailRepository;
import com.aosama.it.repository.NotificationRepository;

import java.util.List;

public class MailViewModel extends AndroidViewModel {
    private MailRepository repository;

    public MailViewModel(@NonNull Application application) {
        super(application);
        repository = new MailRepository(application);
    }

    public StateLiveData<BasicResponse<List<DataMail>>> getInbox(String url) {
        return repository.getInbox(url);
    }

}
