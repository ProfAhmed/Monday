package com.aosama.it.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aosama.it.R;
import com.aosama.it.models.responses.boards.Attachment;
import com.aosama.it.models.responses.boards.UserBoard;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentVH> {

    private Context mContext;
    private List<Attachment> attachments = new ArrayList<>();
    private OnUserClicked onUserClicked = null;

    public AttachmentAdapter(Context mContext, List<Attachment> userItems,
                             OnUserClicked onUserClicked) {
        this.mContext = mContext;
        this.attachments = userItems;
        this.onUserClicked = onUserClicked;

    }

    private static final String TAG = "HAdapterUsers";

    @Override
    public void onBindViewHolder(@NonNull AttachmentVH holder, int position) {

        Attachment attachment = attachments.get(position);


        holder.tvAttachmentName
                .setText(WordUtils.capitalize(attachment.getAttachName()));

        holder.ivFileDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUserClicked != null) {
                    onUserClicked.onUserClicked(v, position, attachment);
                }
            }
        });

    }

    @NonNull
    @Override
    public AttachmentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_attachment_item, parent, false);
        return new AttachmentVH(view);
    }

    public interface OnUserClicked {
        //        void onUserClicked(View view, int position);
        void onUserClicked(View view, int position, Attachment attachment);
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }


    class AttachmentVH extends RecyclerView.ViewHolder {

        @BindView(R.id.ivFileDownload)
        ImageView ivFileDownload;
        @BindView(R.id.tvAttachmentName)
        TextView tvAttachmentName;


        AttachmentVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}