package com.aosama.it.ui.adapter.board;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.aosama.it.R;
import com.aosama.it.models.responses.boards.UserBoard;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;

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

    private static final String TAG = "HAdapterUsers";

    @Override
    public void onBindViewHolder(@NonNull UserView holder, int position) {

        UserBoard userBoard = userBoards.get(position);


//        holder.userName.setText(userBoard.getShortName());
//        holder.userName.setText(userBoard.getFullName());
//        holder.userName.setText(StringUtils.capitalize(userBoard.getName()));
        holder.userName
                .setText(WordUtils.capitalize(userBoard.getName()));

        String path = userBoard.getUserImage();
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
            if (userBoard.getShortName().length() > 0) {
//                String firstChar = userBoard.getShortName().substring(0, 1).toUpperCase();
//                Log.e(TAG, "onBindViewHolder: " + userBoard.getShortName());
//                Log.e(TAG, "onBindViewHolder: " + userBoard.getFullName());
//                Log.e(TAG, "onBindViewHolder: " + userBoard.getName());
//                String[] names = userBoard.getName().split(" ");
//                if (names.length >= 2) {
//                    firstChar = names[0].substring(0, 1).toUpperCase() +
//                            names[1].substring(0, 1).toUpperCase();
//                } else {
//                    if (names.length == 1) {
//                        firstChar = names[0].substring(0, 1).toUpperCase();
//                    }
//                }
//                TextDrawable drawable2 = createTextDrawable(firstChar);
                TextDrawable drawable2 = createTextDrawable(userBoard.getShortName());
                holder.userPhoto.setImageDrawable(drawable2);
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUserClicked != null) {
                    onUserClicked.onUserClicked(v, position, userBoard);
                }
            }
        });

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

