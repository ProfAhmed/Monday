package com.aosama.it.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aosama.it.R;
import com.aosama.it.ui.activities.InboxDetailsActivity;

import butterknife.ButterKnife;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxVH> {

    private Context mContext;

    public InboxAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public InboxVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_inbox, parent, false);
        return new InboxVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxVH holder, int position) {

    }

    @Override
    public int getItemCount() {

        return 15;
    }


    public class InboxVH extends RecyclerView.ViewHolder {

        public InboxVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, InboxDetailsActivity.class));
                }
            });
        }
    }
}

