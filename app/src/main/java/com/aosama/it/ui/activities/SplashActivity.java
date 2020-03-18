package com.aosama.it.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.aosama.it.IntroScreenActivity;
import com.aosama.it.R;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.PreferenceProcessor;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setLocale(PreferenceProcessor.getInstance(this).getStr(MyConfig.MyPrefs.LOCAL_LANG, Locale.getDefault().getLanguage()));
        boolean loggedIn = PreferenceProcessor.getInstance(this).getBool(MyConfig.MyPrefs.IS_LOGIN, false);
        Intent i;
        if (loggedIn) {
            i = new Intent(this, HomeActivity.class);
        } else {
            i = new Intent(this, IntroScreenActivity.class);
        }
        final Intent intent = i;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        }).start();
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }

}
