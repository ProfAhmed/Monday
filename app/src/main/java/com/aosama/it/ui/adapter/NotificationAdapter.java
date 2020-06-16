package com.aosama.it.ui.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.aosama.it.models.responses.notifications.NotificationModel;
import com.aosama.it.utiles.MyUtilis;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationView> {

    private Context mContext;
    private OnUserClicked onUserClicked = null;
    private List<NotificationModel> notificationModelList;

    public NotificationAdapter(Context mContext, List<NotificationModel> notificationModelList,
                               OnUserClicked onUserClicked) {
        this.mContext = mContext;
        this.notificationModelList = notificationModelList;
        this.onUserClicked = onUserClicked;

    }

    private static final String TAG = "HAdapterUsers";

    @Override
    public void onBindViewHolder(@NonNull NotificationView holder, int position) {

        NotificationModel notificationModel = notificationModelList.get(position);
        holder.tvNotification.setText(notificationModel.getMsg());
        String date = notificationModel.getTime().substring(0, notificationModel.getTime().indexOf("T"));
        MyUtilis.parsDateYYMMDD(date);
        String daynum = MyUtilis.ParseDate.day + " ";
        String monthName = MyUtilis.ParseDate.monthString + " ";
        String year = MyUtilis.ParseDate.year;
        String dateRes = daynum + monthName + year;

        holder.tvDate.setText(MyUtilis.parseDateWithAmPm(notificationModel.getTime()));
        switch (notificationModel.getnType()) {
            case "b":
                holder.ivIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_noti1));
                break;
            case "m":
                holder.ivIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_noti2));

                break;
            case "c":
                holder.ivIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_noti3));
                break;
            case "t":
                holder.ivIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_noti4));
                break;
            case "meeting":
                holder.ivIcon.setImageDrawable(mContext.getDrawable(R.drawable.susf_logo));
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onUserClicked != null)
                    onUserClicked.onUserClicked(view, position, notificationModel);
            }
        });
    }

    @NonNull
    @Override
    public NotificationView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_notification_item, parent, false);
        return new NotificationView(view);
    }

    public interface OnUserClicked {
        //        void onUserClicked(View view, int position);
        void onUserClicked(View view, int position, NotificationModel notificationModel);
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }


    class NotificationView extends RecyclerView.ViewHolder {

        @BindView(R.id.tvNotification)
        TextView tvNotification;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.ivIcon)
        CircleImageView ivIcon;


        NotificationView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
