package com.aosama.it.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aosama.it.R;
import com.aosama.it.ui.adapter.InboxAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.saket.inboxrecyclerview.InboxRecyclerView;


public class InboxFragment extends Fragment {

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    public InboxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inbox, container, false);
        ButterKnife.bind(this, v);

        InboxAdapter adapter = new InboxAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return v;
    }

}
