package com.aosama.it.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aosama.it.R;
import com.aosama.it.models.responses.boards.UserBoard;
import com.aosama.it.models.responses.notifications.TaskN;
import com.aosama.it.utiles.MyUtilis;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskView> {

    private Context mContext;
    private OnUserClicked onUserClicked = null;
    private List<TaskN> taskNList;

    public TasksAdapter(Context mContext, List<TaskN> taskNList,
                               OnUserClicked onUserClicked) {
        this.mContext = mContext;
        this.taskNList = taskNList;
        this.onUserClicked = onUserClicked;

    }

    private static final String TAG = "HAdapterUsers";

    @Override
    public void onBindViewHolder(@NonNull TaskView holder, int position) {

        TaskN taskN = taskNList.get(position);
        holder.tvTaskName.setText(taskN.getName());
        String date = taskN.getDueDate().substring(0, taskN.getDueDate().indexOf("T"));
        MyUtilis.parsDateYYMMDD(date);
        String daynum = MyUtilis.ParseDate.day + " ";
        String monthName = MyUtilis.ParseDate.monthString + " ";
        String year = MyUtilis.ParseDate.year;
        String dateRes = daynum + monthName + year;

        holder.tvDate.setText(dateRes);

    }


    @NonNull
    @Override
    public TaskView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_notification_item, parent, false);
        return new TaskView(view);
    }

    public interface OnUserClicked {
        //        void onUserClicked(View view, int position);
        void onUserClicked(View view, int position, UserBoard userBoard);
    }

    @Override
    public int getItemCount() {
        return taskNList.size();
    }


    class TaskView extends RecyclerView.ViewHolder {

        @BindView(R.id.tvNotification)
        TextView tvTaskName;
        @BindView(R.id.tvDate)
        TextView tvDate;


        TaskView(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
