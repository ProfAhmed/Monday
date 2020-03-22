package com.aosama.it.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.aosama.it.R;
import com.aosama.it.models.responses.boards.Assignee;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomListAdapterDialog extends BaseAdapter {

    private ArrayList<Assignee> listData;

    private LayoutInflater layoutInflater;

    private Context mContext;

    public CustomListAdapterDialog(Context context, ArrayList<Assignee> listData) {
        this.mContext = context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_dialog, null);
            holder = new ViewHolder();
            holder.unitView = (TextView) convertView.findViewById(R.id.tvUserName);
            holder.userPhoto = convertView.findViewById(R.id.ivUserImagePhoto);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.unitView.setText(listData.get(position).getFullName());
        String path = listData.get(position).getUserImage();
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
            if (listData.get(position).getShortName().length() > 0) {
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
                TextDrawable drawable2 = createTextDrawable(listData.get(position).getShortName());
                holder.userPhoto.setImageDrawable(drawable2);
            }
        }

        return convertView;
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

    static class ViewHolder {
        TextView unitView;
        ImageView userPhoto;
    }

}