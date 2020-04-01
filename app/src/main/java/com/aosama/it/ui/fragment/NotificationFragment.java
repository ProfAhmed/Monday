package com.aosama.it.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aosama.it.R;
import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.boards.CommentGroup;
import com.aosama.it.models.responses.boards.UserBoard;
import com.aosama.it.models.responses.notifications.NotificationData;
import com.aosama.it.models.wrappers.StateData;
import com.aosama.it.ui.adapter.CommentAdapter;
import com.aosama.it.ui.adapter.NotificationAdapter;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.MyUtilis;
import com.aosama.it.viewmodels.NotificationViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends Fragment implements NotificationAdapter.OnUserClicked {
    @BindView(R.id.rv)
    RecyclerView rv;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, v);

        AlertDialog dialog = MyUtilis.myDialog(getActivity());
        dialog.show();

        NotificationViewModel viewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        viewModel.getComments(MyConfig.NOTIFICATION_URL).observe(this, basicResponseStateData -> {
            dialog.dismiss();
            switch (basicResponseStateData.getStatus()) {
                case SUCCESS:

                    NotificationAdapter adapter = new NotificationAdapter(getActivity(),
                            basicResponseStateData.getData().getData().getNotificationModelList(), this);
                    rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rv.setAdapter(adapter);

                    break;
                case FAIL:
                    Toast.makeText(getActivity(), basicResponseStateData.getErrorsMessages() != null ? basicResponseStateData.getErrorsMessages().getErrorMessages().get(0) : null, Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    if (basicResponseStateData.getError() != null) {
//                            Toast.makeText(this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                        Log.v("Statues", "Error" + basicResponseStateData.getError().getMessage());
                    }
                    break;
                case CATCH:
                    Toast.makeText(getActivity(), getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                    break;
            }
        });
        return v;
    }

    @Override
    public void onUserClicked(View view, int position, UserBoard userBoard) {

    }
}
