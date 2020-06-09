package com.aosama.it.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
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
import com.aosama.it.models.responses.mail.DataMail;
import com.aosama.it.utiles.MyUtilis;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxVH> {

    private Context mContext;
    List<DataMail> mails;
    private onInboxClick onInboxClick;
    private boolean isSent;

    public interface onInboxClick {
        void onInboxItemClicked(DataMail dataMail);
    }

    public InboxAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setOnInboxClick(InboxAdapter.onInboxClick onInboxClick) {
        this.onInboxClick = onInboxClick;
    }

    @NonNull
    @Override
    public InboxVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_inbox, parent, false);
        return new InboxVH(view);
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

    @Override
    public void onBindViewHolder(@NonNull InboxVH holder, int position) {
        DataMail mail = mails.get(position);
        StringBuilder usersStringBuilder = new StringBuilder();
        if (mail.getToUsers() != null) {

            for (int i = 0; i < mail.getToUsers().size(); i++) {
                usersStringBuilder.append(mail.getToUsers().get(i).getName());
                if (i != mail.getToUsers().size() - 1)
                    usersStringBuilder.append(", ");
            }
        }

        if (!isSent)
            holder.tvUserName.setText(mail.getFromUser().getName());
        else
            holder.tvUserName.setText(usersStringBuilder.toString());

        holder.tvContent.setText(Html.fromHtml(mail.getTitle()));
        String date = mail.getCreatedAt().substring(0, mail.getCreatedAt().indexOf("T"));
        MyUtilis.parsDateYYMMDD(date);
        String daynum = MyUtilis.ParseDate.day + " ";
        String monthName = MyUtilis.ParseDate.monthString + " ";
        String year = MyUtilis.ParseDate.year;
        String dateRes = daynum + monthName + year;
        try {
            holder.tvDate.setText(MyUtilis.parseDateWithAmPm(mail.getCreatedAt()));

        } catch (NullPointerException s) {
            s.printStackTrace();
        }

        String path = mail.getFromUser().getUserImage();
        if (!TextUtils.isEmpty(path) && path != null
                && path.length() > 0) {
            Picasso.get().load(path)
                    .into(holder.profile_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        } else {
            String firstChar = "";
            if (mail.getFromUser().getShortName().length() > 0) {

                TextDrawable drawable2 = createTextDrawable(mail.getFromUser().getShortName());
                holder.profile_image.setImageDrawable(drawable2);
            }
        }
        holder.itemView.setOnClickListener(view -> {
            if (onInboxClick != null)
                onInboxClick.onInboxItemClicked(mail);
        });
    }

    @Override
    public int getItemCount() {
        if (mails == null)
            return 0;
        else
            return mails.size();
    }

    public void setMails(List<DataMail> mails, boolean isSent) {
        this.mails = mails;
        this.isSent = isSent;
        notifyDataSetChanged();
    }

    public class InboxVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUserName)
        TextView tvUserName;
        @BindView(R.id.tvContent)
        TextView tvContent;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.profile_image)
        ImageView profile_image;

        public InboxVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}

