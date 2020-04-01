package com.aosama.it.ui.adapter;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.aosama.it.R;
import com.aosama.it.ui.fragment.NotificationFragment;
import com.aosama.it.ui.fragment.TasksFragment;

public class NotificationAndTasksAdapter extends FragmentPagerAdapter {

    private String[] title;
    private final Fragment[] fragment = new Fragment[2];

    public NotificationAndTasksAdapter(FragmentManager fm, Context context) {
        super(fm);
        fragment[0] = new NotificationFragment();
        fragment[1] = new TasksFragment();
        String notification = context.getString(R.string.menu_notifications);
        String tasks = context.getString(R.string.tasks);
        title = new String[]{notification, tasks};
    }

    @Override
    public Fragment getItem(int position) {
        Log.v("Title-Item = ", String.valueOf(position));
        return fragment[position];
    }


    @Override
    public CharSequence getPageTitle(int position) {
        Log.v("Title-PageTitle = ", title[position]);
        return title[position];
    }


    @Override
    public int getCount() {
        return fragment.length;
    }

}
