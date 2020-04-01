package com.aosama.it.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.boards.CommentGroup;
import com.aosama.it.models.responses.notifications.NotificationData;
import com.aosama.it.models.responses.notifications.NotificationModel;
import com.aosama.it.models.wrappers.StateLiveData;
import com.aosama.it.repository.CommentsRepository;
import com.aosama.it.repository.NotificationRepository;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {
    private NotificationRepository repository;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        repository = new NotificationRepository(application);
    }

    public StateLiveData<BasicResponse<NotificationData>> getComments(String url) {
        return repository.getNotifications(url);
    }


}
