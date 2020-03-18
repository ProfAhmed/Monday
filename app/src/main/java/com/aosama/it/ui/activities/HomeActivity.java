package com.aosama.it.ui.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.aosama.it.IntroScreenActivity;
import com.aosama.it.R;
import com.aosama.it.ui.adapter.NavPanelListAdapter;
import com.aosama.it.ui.fragment.BoardDetailsFragment;
import com.aosama.it.ui.fragment.HomeFragment;
import com.aosama.it.ui.fragment.InboxFragment;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.PreferenceProcessor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.navList)
    ListView navlist;
    @BindView(R.id.tvProfileName)
    TextView tvProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PreferenceProcessor.getInstance(this).getStr(MyConfig.MyPrefs.LOCAL_LANG, "ar").equals("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.setFocusableInTouchMode(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        initDrawerHeader();

        NavPanelListAdapter adapter = initNavigationPanel();
        adapter.setOnItemClickListener(position -> {
            if (drawer.isDrawerOpen(GravityCompat.START))
                drawer.closeDrawer(GravityCompat.START);
            switch (position) {
                case 0:
                    getSupportActionBar().setTitle(getString(R.string.boards));
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
                    fab.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    getSupportActionBar().setTitle(getString(R.string.menu_inbox));
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new InboxFragment()).commit();
                    fab.setVisibility(View.GONE);
                    break;
                case 2:
                    Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                    break;

            }
        });
        if (savedInstanceState == null) {
            getSupportActionBar().setTitle(getString(R.string.boards));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
        }

        setLocale(PreferenceProcessor.getInstance(this).getStr(MyConfig.MyPrefs.LOCAL_LANG, "en"));
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }

    private NavPanelListAdapter initNavigationPanel() {
        NavPanelListAdapter adapter = new NavPanelListAdapter(this);
        navlist.setAdapter(adapter);
        return adapter;
    }

    private void initDrawerHeader() {
        tvProfileName.setText("Ahmed Ali");
    }

    public void signOut(View view) {
        PreferenceProcessor.getInstance(this).setBool(MyConfig.MyPrefs.IS_LOGIN, false);
        Intent intent = new Intent(this, IntroScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //checking if there are items at BackStack or not
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            getSupportFragmentManager().popBackStack(BoardDetailsFragment.class.getSimpleName(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }
}
