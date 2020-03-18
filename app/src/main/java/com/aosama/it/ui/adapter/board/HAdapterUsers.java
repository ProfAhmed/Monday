package com.aosama.it.ui.adapter.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosama.it.R;
import com.aosama.it.models.responses.boards.UserBoard;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HAdapterUsers extends RecyclerView.Adapter<HAdapterUsers.UserView> {

    private Context mContext;
    private List<UserBoard> userBoards = new ArrayList<>();
    private OnUserClicked onUserClicked = null;

    public HAdapterUsers(Context mContext, List<UserBoard> userItems,
                         OnUserClicked onUserClicked) {
        this.mContext = mContext;
        this.userBoards = userItems;
        this.onUserClicked = onUserClicked;

    }

    @Override
    public void onBindViewHolder(@NonNull UserView holder, int position) {

        UserBoard userBoard = userBoards.get(position);

        holder.userName.setText(userBoard.getShortName());
        Picasso.get().load(userBoard.getUserImage())
                .into(holder.userPhoto, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUserClicked != null) {
                    onUserClicked.onUserClicked(v, position, userBoard);
                }
            }
        });

    }

    @NonNull
    @Override
    public UserView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_user, parent, false);
        return new UserView(view);
    }

    public interface OnUserClicked {
        //        void onUserClicked(View view, int position);
        void onUserClicked(View view, int position, UserBoard userBoard);
    }

    @Override
    public int getItemCount() {
        return userBoards.size();
    }


    class UserView extends RecyclerView.ViewHolder {

        @BindView(R.id.ivUserImagePhoto)
        ImageView userPhoto;
        @BindView(R.id.tvUserName)
        TextView userName;


        UserView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

