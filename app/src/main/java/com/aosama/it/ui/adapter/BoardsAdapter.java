package com.aosama.it.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class BoardsAdapter extends RecyclerView.Adapter<BoardsAdapter.BoardVH> {
    private ArrayList<TaskE> tasksGroups;
    private ArrayList<String> tableName;
    OnItemClick onItemClicklistener;
    Context mcContext;
    boolean isMeetinLin = false;

    private static final CircularProgressIndicator.ProgressTextAdapter TIME_TEXT_ADAPTER = currentProgress -> (int) currentProgress + " " + "%";

    public interface OnItemClick {
        void setOnBoardItemClick(View v, TaskE taskE);
    }

    public BoardsAdapter(OnItemClick onItemClicklistener, Context context) {
        this.onItemClicklistener = onItemClicklistener;
        this.mcContext = context;
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
//        holder.viewStatusColor.setBackgroundColor(Color.parseColor(taskE.getStatus().getColor()));
        holder.tvTaskName.setText("   " + taskE.getName());
        holder.tvStatus.setText(taskE.getStatus().getName());
        holder.tvStatus.setBackgroundColor(Color.parseColor(taskE.getStatus().getColor()));
        holder.tvEndDate.setText(MyUtilis.parseDate(taskE.getDueDate()));
//        holder.progress_horizontal.setProgress(taskE.getProgressValue());
        holder.ivComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClicklistener != null)
                    onItemClicklistener.setOnBoardItemClick(holder.ivComments, taskE);
            }
        });
        holder.ivAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClicklistener != null)
                    onItemClicklistener.setOnBoardItemClick(holder.ivAttachment, taskE);
            }
        });
        holder.ivPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClicklistener != null)
                    onItemClicklistener.setOnBoardItemClick(holder.ivPeople, taskE);
            }
        });


        holder.tvTaskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClicklistener != null)
                    onItemClicklistener.setOnBoardItemClick(holder.tvTaskName, taskE);
            }
        });


        holder.circular_progress.setMaxProgress(100);
        holder.circular_progress.setCurrentProgress(taskE.getProgressValue());
        holder.circular_progress.setProgressColor(Color.parseColor(taskE.getProgressColor()));
        holder.circular_progress.setProgressTextAdapter(TIME_TEXT_ADAPTER);
//        String add_date = MyUtilis.formateDate(taskE.getAddDate().substring(0, taskE.getDueDate().indexOf("T")));
        holder.tvInfo.setText(mcContext.getString(R.string.start_date) + ":  ");
        holder.tvInfoValue.setText(MyUtilis.parseDate(taskE.getStartDate()));
        holder.tvInfoValue.setTextColor(Color.BLACK);

        holder.ivAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMeetinLin = false;
                try {
                    holder.tvInfo.setText(mcContext.getString(R.string.add_date) + ":  ");
                    holder.tvInfoValue.setText(MyUtilis.parseDate(taskE.getAddDate()));
                    holder.tvInfoValue.setTextColor(Color.BLACK);

                } catch (NullPointerException e) {
                    holder.tvInfoValue.setText(" ");

                }
            }
        });
//        String due_date = MyUtilis.formateDate(taskE.getDueDate().substring(0, taskE.getDueDate().indexOf("T")));

        holder.ivFlowTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMeetinLin = false;
                try {
                    holder.tvInfo.setText(mcContext.getString(R.string.flow_title) + ":  ");
                    holder.tvInfoValue.setText(taskE.getFlowTitle());
                    holder.tvInfoValue.setTextColor(Color.BLACK);

                } catch (NullPointerException e) {
                    holder.tvInfoValue.setText(" ");

                }
            }
        });

        holder.ivStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMeetinLin = false;
                try {
                    holder.tvInfo.setText(mcContext.getString(R.string.start_date) + ":  ");
                    holder.tvInfoValue.setText(MyUtilis.parseDate(taskE.getStartDate()));
                    holder.tvInfoValue.setTextColor(Color.BLACK);

                } catch (NullPointerException e) {
                    holder.tvInfoValue.setText(" ");

                }
            }
        });

        holder.ivMeetingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMeetinLin = false;
                try {
                    holder.tvInfo.setText(mcContext.getString(R.string.meeting_time) + ":  ");
                    holder.tvInfoValue.setText(MyUtilis.parseDateWithAmPm(taskE.getMeetingTime(),
                            taskE.getMeetingTimeTime()));
                    holder.tvInfoValue.setTextColor(Color.BLACK);

                } catch (NullPointerException e) {
                    holder.tvInfoValue.setText(" ");
                }
            }
        });

        holder.ivMeetingLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMeetinLin = true;
                try {
                    holder.tvInfo.setText(mcContext.getString(R.string.meeting_link) + ":  ");
                    holder.tvInfoValue.setText(taskE.getMeetingUrl());
                    holder.tvInfoValue.setTextColor(Color.BLUE);

                } catch (NullPointerException e) {
                    holder.tvInfoValue.setText(" ");

                }
            }
        });

        holder.tvInfoValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isMeetinLin) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(taskE.getMeetingUrl()));
                        mcContext.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        //        @BindView(R.id.progress_horizontal)
//        ProgressBar progress_horizontal;
        @BindView(R.id.tvTableName)
        TextView tvTableName;
        @BindView(R.id.tvTaskName)
        TextView tvTaskName;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.tvEndDate)
        TextView tvEndDate;
        @BindView(R.id.tvInfo)
        TextView tvInfo;
        @BindView(R.id.tvInfoValue)
        TextView tvInfoValue;
        @BindView(R.id.ivMeetingTime)
        ImageView ivMeetingTime;
        @BindView(R.id.ivAddDate)
        ImageView ivAddDate;
        @BindView(R.id.ivFlowTitle)
        ImageView ivFlowTitle;
        @BindView(R.id.ivMeetingLink)
        ImageView ivMeetingLink;
        @BindView(R.id.ivStartDate)
        ImageView ivStartDate;
        @BindView(R.id.ivComments)
        ImageView ivComments;
        @BindView(R.id.ivAttachment)
        ImageView ivAttachment;
        @BindView(R.id.ivPeople)
        ImageView ivPeople;
        @BindView(R.id.circular_progress)
        CircularProgressIndicator circular_progress;

        public BoardVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

