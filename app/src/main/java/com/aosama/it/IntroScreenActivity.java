package com.aosama.it;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aosama.it.ui.activities.LoginActivity;
import com.aosama.it.ui.adapter.DotIndicatorPagerAdapter;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.PreferenceProcessor;
import com.aosama.it.utiles.ZoomOutPageTransformer;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroScreenActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    InkPageIndicator indicator;
    @BindView(R.id.tvLanguage)
    TextView tvLanguage;

    String currLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PreferenceProcessor.getInstance(this).getStr(MyConfig.MyPrefs.LOCAL_LANG, Locale.getDefault().getLanguage()).equals("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setContentView(R.layout.activity_intro_screen);
        ButterKnife.bind(this);

        try {
            final DotIndicatorPagerAdapter adapter = new DotIndicatorPagerAdapter(this);
            viewPager.setAdapter(adapter);
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            indicator.setViewPager(viewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLang();
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
//        finish();
    }


    private void setLang() {
        currLang = PreferenceProcessor.getInstance(this).getStr(MyConfig.MyPrefs.LOCAL_LANG, Locale.getDefault().getLanguage());
        switch (currLang) {
            case "en":
                tvLanguage.setText(getString(R.string.english));
                break;
            case "ar":
                tvLanguage.setText(getString(R.string.arabaic));
                break;

        }
        tvLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
//        try {
//            Field[] fields = popup.getClass().getDeclaredFields();
//            for (Field field : fields) {
//                if ("mPopup".equals(field.getName())) {
//                    field.setAccessible(true);
//                    Object menuPopupHelper = field.get(popup);
//                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
//                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
//                    setForceIcons.invoke(menuPopupHelper, true);
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_languages_popup, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        PreferenceProcessor.getInstance(this).setStr(MyConfig.MyPrefs.LOCAL_LANG, lang);
        Intent refresh = new Intent(this, IntroScreenActivity.class);
        finish();
        startActivity(refresh);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case R.id.enLang:
                setLocale("en");
                break;
            case R.id.arLang:
                setLocale("ar");
                break;
        }
        return false;
    }
}
