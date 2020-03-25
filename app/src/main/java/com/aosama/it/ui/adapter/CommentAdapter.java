package com.aosama.it.ui.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.aosama.it.R;
import com.aosama.it.models.responses.boards.Attachment;
import com.aosama.it.models.responses.boards.CommentGroup;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentVH> {

    private Context mContext;
    private List<CommentGroup> commentGroups;
    private OnAttachClicked onAttachClicked = null;

    public interface OnAttachClicked {
        //        void onUserClicked(View view, int position);
        void onUserClicked(View view, int position, List<Attachment> attachments);
    }

    public CommentAdapter(Context mContext, List<CommentGroup> commentGroups, OnAttachClicked onAttachClicked) {
        this.mContext = mContext;
        this.commentGroups = commentGroups;
        this.onAttachClicked = onAttachClicked;
    }

    @NonNull
    @Override
    public CommentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_comment_layout, parent, false);
        return new CommentVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentVH holder, int position) {
        CommentGroup commentGroup = commentGroups.get(position);
        holder.tvUserName.setText(commentGroup.getByUserName());
        holder.ivAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if (onAttachClicked != null) {
                    onAttachClicked.onUserClicked(holder.itemView, position, commentGroup.getAttachments());
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvCommentData.setText(Html.fromHtml(commentGroup.getCommentData(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvCommentData.setText(Html.fromHtml(commentGroup.getCommentData()));
        }
        String path = commentGroup.getByUserImage();
        if (!TextUtils.isEmpty(path) && path != null
                && path.length() > 0) {
            Picasso.get().load(path)
                    .into(holder.userPhoto, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        } else {
            String firstChar = "";
            if (commentGroup.getByShortName().length() > 0) {
                TextDrawable drawable2 = createTextDrawable(commentGroup.getByShortName());
                holder.userPhoto.setImageDrawable(drawable2);
            }
        }
    }

    private TextDrawable createTextDrawable(String firstChar) {
//        TextDrawable drawable1 = TextDrawable.builder()
//                .buildRoundRect(firstChar, Color.RED, 10);
        int dimWH = (int) mContext.getResources()
                .getDimension(R.dimen._60sdp);
        int fonsSize =
                (int) mContext.getResources()
                        .getDimension(R.dimen._20ssp);
        return TextDrawable.builder()
                .beginConfig()
                .fontSize(fonsSize)
                .width(dimWH)  // width in px
                .height(dimWH) // height in px
                .endConfig()
                .buildRect(firstChar, Color.RED);
    }


    @Override
    public int getItemCount() {

        return commentGroups.size();
    }


    public class CommentVH extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDays)
        TextView tvDays;
        @BindView(R.id.tvUserName)
        TextView tvUserName;
        @BindView(R.id.tvCommentData)
        TextView tvCommentData;
        @BindView(R.id.tvReply)
        TextView tvReply;
        @BindView(R.id.ivUserImagePhoto)
        ImageView userPhoto;
        @BindView(R.id.ivAttachment)
        ImageView ivAttachment;

        public CommentVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}


