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

        holder.tvDate.setText(dateRes);

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
    public NotificationView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_notification_item, parent, false);
        return new NotificationView(view);
    }

    public interface OnUserClicked {
        //        void onUserClicked(View view, int position);
        void onUserClicked(View view, int position, UserBoard userBoard);
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


        NotificationView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
