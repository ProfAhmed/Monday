package com.aosama.it.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.aosama.it.R;
import com.aosama.it.models.responses.boards.UserBoard;
import com.aosama.it.ui.adapter.board.HAdapterUsers;
import com.linkedin.android.spyglass.suggestions.interfaces.Suggestible;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserView> {

    private Context mContext;
    private OnUserClicked onUserClicked = null;
    private List<? extends Suggestible> suggestions;
    private List<Suggestible> suggestionsFilter;

    public UserAdapter(Context mContext, List<? extends Suggestible> userItems,
                       OnUserClicked onUserClicked) {
        this.mContext = mContext;
        this.suggestions = userItems;
        this.onUserClicked = onUserClicked;
        suggestionsFilter = new ArrayList<>();

    }

    private static final String TAG = "HAdapterUsers";

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserView holder, int position) {

        Suggestible suggestion = suggestions.get(position);
        if (!(suggestion instanceof UserBoard)) {
            return;
        }
        UserBoard userBoard = (UserBoard) suggestion;
        if (userBoard.getName() != null)
            holder.userName.setText(WordUtils.capitalize(userBoard.getName()));
        else
            holder.userName.setText(WordUtils.capitalize(userBoard.getUserName()));

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
        return TextDrawable.builder().beginConfig().
                textColor(Color.BLUE)
//                .beginConfig()
                .fontSize(fonsSize)
                .bold()
                .width(dimWH)  // width in px
                .height(dimWH) // height in px
                .endConfig()
                .buildRect(firstChar, Color.parseColor("#41C5C3C3"));

    }

    @NonNull
    @Override
    public UserAdapter.UserView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_user_item, parent, false);
        return new UserAdapter.UserView(view);
    }

    public void setSuggestions(List<? extends Suggestible> suggestions) {
        this.suggestions = suggestions;
//        this.suggestionsFilter.addAll(suggestions);

    }

//    public void filter(String charText) {
//        charText = charText.toUpperCase(Locale.getDefault());
//        if (suggestions != null) {
//            suggestions.clear();
//        }
//        if (charText.length() == 0 && (suggestions != null)) {
//            suggestions.addAll(suggestionsFilter);
//        } else {
//            for (Suggestible countryModel : suggestionsFilter) {
//                UserBoard userBoard = (UserBoard) countryModel;
//                String country = countryModel.getSuggestiblePrimaryText().toUpperCase();
//                if (country.charAt(0) == charText.charAt(0) && country.contains(charText)) {
//                    suggestions.add(userBoard);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

    public interface OnUserClicked {
        //        void onUserClicked(View view, int position);
        void onUserClicked(View view, int position, UserBoard userBoard);
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
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
