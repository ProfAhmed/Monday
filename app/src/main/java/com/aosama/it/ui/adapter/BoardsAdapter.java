package com.aosama.it.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aosama.it.R;
import com.aosama.it.models.responses.boards.TaskE;
import com.aosama.it.models.responses.boards.TasksGroup;
import com.aosama.it.utiles.MyUtilis;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoardsAdapter extends RecyclerView.Adapter<BoardsAdapter.BoardVH> {
    private ArrayList<TaskE> tasksGroups;
    private ArrayList<String> tableName;
    OnItemClick onItemClicklistener;

    public interface OnItemClick {
        void setOnBoardItemClick(View v, TaskE taskE);
    }

    public BoardsAdapter(OnItemClick onItemClicklistener) {
        this.onItemClicklistener = onItemClicklistener;
    }

    @NonNull
    @Override
    public BoardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_board_item, parent, false);
        return new BoardVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardVH holder, int position) {

        TaskE taskE = tasksGroups.get(position);

        if (taskE.getTableName() != null) {
            holder.tvTableName.setVisibility(View.VISIBLE);
            holder.tvTableName.setText(taskE.getTableName());
        } else holder.tvTableName.setVisibility(View.GONE);
        String date = MyUtilis.formateDate(taskE.getDueDate().substring(0, taskE.getDueDate().indexOf("T")));
        holder.viewStatusColor.setBackgroundColor(Color.parseColor(taskE.getStatus().getColor()));
        holder.tvTaskName.setText(taskE.getName());
        holder.tvStatus.setText(taskE.getStatus().getName());
        holder.tvEndDate.setText(date);
        holder.progress_horizontal.setProgress(taskE.getProgressValue());
        holder.btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClicklistener != null)
                    onItemClicklistener.setOnBoardItemClick(holder.btnComments, taskE);
            }
        });
        holder.btnAttachments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClicklistener != null)
                    onItemClicklistener.setOnBoardItemClick(holder.btnAttachments, taskE);
            }
        });

        holder.ivTeamData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClicklistener.setOnBoardItemClick(holder.ivTeamData, taskE);

            }
        });
        holder.ivDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClicklistener.setOnBoardItemClick(holder.ivDates, taskE);

            }
        });
        holder.ivMeetingData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClicklistener.setOnBoardItemClick(holder.ivMeetingData, taskE);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (tasksGroups != null)
            return tasksGroups.size();
        else
            return 0;
    }

    public void setTasksGroups(ArrayList<TaskE> tasksGroups) {
        this.tasksGroups = tasksGroups;
        notifyDataSetChanged();
    }

    public class BoardVH extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_horizontal)
        ProgressBar progress_horizontal;
        @BindView(R.id.tvTableName)
        TextView tvTableName;
        @BindView(R.id.tvTaskName)
        TextView tvTaskName;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.tvEndDate)
        TextView tvEndDate;
        @BindView(R.id.ivDates)
        ImageView ivDates;
        @BindView(R.id.ivMeetingData)
        ImageView ivMeetingData;
        @BindView(R.id.ivTeamData)
        ImageView ivTeamData;
        @BindView(R.id.ivMoreData)
        ImageView ivMoreData;
        @BindView(R.id.viewStatusColor)
        View viewStatusColor;
        @BindView(R.id.btnComments)
        MaterialButton btnComments;
        @BindView(R.id.btnAttachments)
        MaterialButton btnAttachments;

        public BoardVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

