package com.aosama.it.ui.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.aosama.it.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DotIndicatorPagerAdapter extends PagerAdapter {

    @BindView(R.id.ivItem_icon)
    ImageView ivItem_icon;
    private Context mContext;

    private static final List<Item> items = new ArrayList<>();

    public DotIndicatorPagerAdapter(Context mContext) {
        this.mContext = mContext;
        if (items.size() == 0) {
            items.add(new Item(0, R.drawable.a_2, null));
            items.add(new Item(0, R.drawable.a_3, null));
            items.add(new Item(0, R.drawable.a_4717, null));
            items.add(new Item(0, R.drawable.business_meeting_icon, null));
            items.add(new Item(0, R.drawable.developers_team, null));
            items.add(new Item(0, R.drawable.group_architects, null));
            items.add(new Item(0, R.drawable.illustration_business, null));
        }
        notifyDataSetChanged();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.material_page, container, false);
        ButterKnife.bind(this, itemView);

        ivItem_icon.setImageDrawable(mContext.getDrawable(items.get(position).imgIcon));
        container.addView(itemView);
        return itemView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private static class Item {
        private final int imgBck;
        private final int imgIcon;
        private final String mContent;

        private Item(int imgBck, int imgIcon, String mContent) {
            this.imgBck = imgBck;
            this.imgIcon = imgIcon;
            this.mContent = mContent;
        }
    }

}
