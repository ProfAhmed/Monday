package com.aosama.it.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aosama.it.R;
import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.mail.DataMail;
import com.aosama.it.models.wrappers.StateData;
import com.aosama.it.ui.activities.InboxDetailsActivity;
import com.aosama.it.ui.activities.MailFormActivity;
import com.aosama.it.ui.adapter.InboxAdapter;
import com.aosama.it.ui.adapter.NotificationAdapter;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.MyUtilis;
import com.aosama.it.viewmodels.MailViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.saket.inboxrecyclerview.InboxRecyclerView;


public class InboxFragment extends Fragment implements View.OnClickListener, InboxAdapter.onInboxClick {

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.tvInbox)
    TextView tvInbox;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.ivSend)
    ImageView ivSend;
    private InboxAdapter adapter;

    public InboxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inbox, container, false);
        ButterKnife.bind(this, v);

        adapter = new InboxAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        tvInbox.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        adapter.setOnInboxClick(this);
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MailFormActivity.class));
            }
        });
        getInbox(MyConfig.MAILS, false);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvInbox:
                tvInbox.setTextColor(Color.WHITE);
                tvInbox.setBackground(getActivity().getDrawable(R.drawable.layout_bg_green));
                tvSend.setTextColor(Color.BLACK);
                tvSend.setBackground(getActivity().getDrawable(R.drawable.layout_bg_white));
                getInbox(MyConfig.MAILS, false);
                break;
            case R.id.tvSend:

                tvSend.setTextColor(Color.WHITE);
                tvSend.setBackground(getActivity().getDrawable(R.drawable.layout_bg_green));
                tvInbox.setTextColor(Color.BLACK);
                tvInbox.setBackground(getActivity().getDrawable(R.drawable.layout_bg_white));
                getInbox(MyConfig.MAILS + "/sent", true);
                break;
        }
    }

    void getInbox(String url, boolean isSend) {
        MailViewModel viewModel = ViewModelProviders.of(this).get(MailViewModel.class);
        AlertDialog dialog = MyUtilis.myDialog(getActivity());
        dialog.show();

        viewModel.getInbox(url).observe(this, basicResponseStateData -> {
            dialog.dismiss();
            switch (basicResponseStateData.getStatus()) {
                case SUCCESS:
                    adapter.setMails(basicResponseStateData.getData().getData(), isSend);
                    break;
                case FAIL:
                    Toast.makeText(getActivity(), basicResponseStateData.getErrorsMessages() != null ? basicResponseStateData.getErrorsMessages().getErrorMessages().get(0) : null, Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    if (basicResponseStateData.getError() != null) {
                        Toast.makeText(getActivity(), getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                        Log.v("Statues", "Error" + basicResponseStateData.getError().getMessage());
                    }
                    break;
                case CATCH:
                    Toast.makeText(getActivity(), getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    @Override
    public void onInboxItemClicked(DataMail dataMail) {
        Intent intent = new Intent(getActivity(), InboxDetailsActivity.class);
        intent.putExtra("image", dataMail.getFromUser().getUserImage());
        intent.putExtra("name", dataMail.getFromUser().getName());
        intent.putExtra("short_name", dataMail.getFromUser().getShortName());
        intent.putExtra("body", dataMail.getBody());

        startActivity(intent);
    }
}
