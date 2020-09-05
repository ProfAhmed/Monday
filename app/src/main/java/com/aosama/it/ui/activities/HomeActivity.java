package com.aosama.it.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.amulyakhare.textdrawable.TextDrawable;
import com.aosama.it.IntroScreenActivity;
import com.aosama.it.R;
import com.aosama.it.constants.Constants;
import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.file.FileResponse;
import com.aosama.it.ui.adapter.NavPanelListAdapter;
import com.aosama.it.ui.fragment.BoardDetailsFragment;
import com.aosama.it.ui.fragment.HomeFragment;
import com.aosama.it.ui.fragment.InboxFragment;
import com.aosama.it.ui.fragment.NotificationFragment;
import com.aosama.it.ui.fragment.NotificationsAndTasksFragment;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.MyUtilis;
import com.aosama.it.utiles.PreferenceProcessor;
import com.aosama.it.viewmodels.CommentsViewModel;
import com.aosama.it.viewmodels.UploadAttachmentViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.navList)
    ListView navlist;
    @BindView(R.id.profile_image)
    ImageView profile_image;
    @BindView(R.id.tvProfileName)
    TextView tvProfileName;
    private String type;

////    private void fireBoardItemDetails(String id) {
//
//        BoardDetailsFragment boardDetailsFragment = new BoardDetailsFragment();
//        Bundle b = new Bundle();
//        b.putString(Constants.SELECTED_BORAD, id);
//        boardDetailsFragment.setArguments(b);
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .addToBackStack(BoardDetailsFragment.class.getSimpleName())
//                .replace(R.id.nav_host_fragment, boardDetailsFragment).commit();
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (PreferenceProcessor.getInstance(this).getStr(MyConfig.MyPrefs.LOCAL_LANG, "ar").equals("ar")) {
//            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        } else {
//            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//        }
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
//                    fab.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    getSupportActionBar().setTitle(getString(R.string.menu_inbox));
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new InboxFragment()).commit();
//                    fab.setVisibility(View.GONE);
                    break;
                case 2:
                    getSupportActionBar().setTitle(getString(R.string.menu_notifications));
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new NotificationsAndTasksFragment()).commit();
//                    fab.setVisibility(View.GONE);

//                    Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
//                    Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
//                    Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                    break;

            }
        });
        String id = getIntent().getStringExtra("id");
        if (savedInstanceState == null && id == null) {
            getSupportActionBar().setTitle(getString(R.string.boards));
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
        }

        if (id != null) {
//            fireBoardItemDetails(id);
        }
//        setLocale(PreferenceProcessor.getInstance(this).getStr(MyConfig.MyPrefs.LOCAL_LANG, "en"));
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
        String userName = PreferenceProcessor.getInstance(this).getStr(MyConfig.MyPrefs.NAME, "user Name");
        String shortName = PreferenceProcessor.getInstance(this).getStr(MyConfig.MyPrefs.SHORT_NAME, "UN");
        String path = PreferenceProcessor.getInstance(this).getStr(MyConfig.MyPrefs.IMAGE, null);
        tvProfileName.setText(userName);

        if (!TextUtils.isEmpty(path) && path != null
                && path.length() > 0) {
            Picasso.get().load(path)
                    .into(profile_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        } else {
            String firstChar = "";
            if (shortName.length() > 0) {

                TextDrawable drawable2 = createTextDrawable(shortName);
                profile_image.setImageDrawable(drawable2);
            }
        }

    }

    private TextDrawable createTextDrawable(String firstChar) {
//        TextDrawable drawable1 = TextDrawable.builder()
//                .buildRoundRect(firstChar, Color.RED, 10);
        int dimWH = (int) getResources()
                .getDimension(R.dimen._60sdp);
        int fonsSize =
                (int) getResources()
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

    public void signOut(View view) {
        PreferenceProcessor.getInstance(this).setBool(MyConfig.MyPrefs.IS_LOGIN, false);
        Intent intent = new Intent(this, IntroScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }

    public String getFileName(Uri uri) {

        Cursor mCursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        int indexedname = mCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        mCursor.moveToFirst();
        String filename = mCursor.getString(indexedname);
        mCursor.close();
        return filename;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        switch (requestCode) {
//            case BoardDetailsFragment.PICKFILE_RESULT_CODE_BOARD:
//                if (resultCode == RESULT_OK) {
//                    // Get the Uri of the selected file
//                    Uri uri = data.getData();
//
//                    File file = new File(this.getCacheDir(), getFileName(uri));
//
//                    int maxBufferSize = 1 * 1024 * 1024;
//
//                    try {
//                        InputStream inputStream = getContentResolver().openInputStream(uri);
//                        Log.e("InputStream Size", "Size " + inputStream);
//                        int bytesAvailable = inputStream.available();
//                        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                        final byte[] buffers = new byte[bufferSize];
//                        BoardDetailsFragment.tvAttachment.setText(getFileName(uri));
//                        FileOutputStream outputStream = new FileOutputStream(file);
//                        int read = 0;
//                        while ((read = inputStream.read(buffers)) != -1) {
//                            outputStream.write(buffers, 0, read);
//                        }
//                        Log.e("File Size", "Size " + file.length());
//                        inputStream.close();
//                        outputStream.close();
//
//                        Log.e("File Path", "Path " + file.getPath());
//                        Log.e("File Size", "Size " + file.length());
//
//                        if (file.length() > 0) {
//                            UploadAttachmentViewModel uploadAttachmentViewModel = new UploadAttachmentViewModel(this, this);
//                            uploadAttachmentViewModel.doUploadAttachment(file);
//                            type = "b";
//                        }
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (OutOfMemoryError e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Toast.makeText(this, "cannot open file picker", Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//            case BoardDetailsFragment.PICKFILE_RESULT_CODE_TASK:
//                if (resultCode == RESULT_OK) {
//                    // Get the Uri of the selected file
//                    Uri uri = data.getData();
//
//                    File file = new File(this.getCacheDir(), getFileName(uri));
//
//                    int maxBufferSize = 1 * 1024 * 1024;
//
//                    try {
//                        InputStream inputStream = getContentResolver().openInputStream(uri);
//                        Log.e("InputStream Size", "Size " + inputStream);
//                        int bytesAvailable = inputStream.available();
//                        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                        final byte[] buffers = new byte[bufferSize];
//                        BoardDetailsFragment.tvAttachment.setText(getFileName(uri));
//                        FileOutputStream outputStream = new FileOutputStream(file);
//                        int read = 0;
//                        while ((read = inputStream.read(buffers)) != -1) {
//                            outputStream.write(buffers, 0, read);
//                        }
//                        Log.e("File Size", "Size " + file.length());
//                        inputStream.close();
//                        outputStream.close();
//
//                        Log.e("File Path", "Path " + file.getPath());
//                        Log.e("File Size", "Size " + file.length());
//
//                        if (file.length() > 0) {
//                            UploadAttachmentViewModel uploadAttachmentViewModel = new UploadAttachmentViewModel(this, this);
//                            uploadAttachmentViewModel.doUploadAttachment(file);
//                            type = "t";
//                        }
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (OutOfMemoryError e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Toast.makeText(this, "cannot open file picker", Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }

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
//
//    @Override
//    public void onProgressUpdate(int percentage) {
////        BoardDetailsFragment.mProgressDialog.show();
////        BoardDetailsFragment.mProgressDialog.setProgress(percentage);
//    }
//
//    @Override
//    public void onError() {
////        BoardDetailsFragment.mProgressDialog.dismiss();
//        Toasty.error(this, getString(R.string.ef_error_create_image_file)).show();
//
//    }
//
//    @Override
//    public void onFinish(BasicResponse<FileResponse> imageResponse) {
//        BoardDetailsFragment.mProgressDialog.setProgress(100);
//        BoardDetailsFragment.mProgressDialog.dismiss();
//        Toasty.success(this, getString(R.string.success)).show();
//        BoardDetailsFragment.attachName = imageResponse.getData().getAttachName();
//        BoardDetailsFragment.attachKey = imageResponse.getData().getAttachKey();
//        JSONObject jsonBody = new JSONObject();
//        try {
//            jsonBody.put("attachName", BoardDetailsFragment.attachName);
//            jsonBody.put("attachKey", BoardDetailsFragment.attachKey);
//            jsonBody.put("isPrivate", false);
//            jsonBody.put("type", type);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        AlertDialog dialog = MyUtilis.myDialog(this);
//        dialog.show();
//        CommentsViewModel viewModel = ViewModelProviders.of(this).get(CommentsViewModel.class);
//        String id = null;
//        if (type.equals("b"))
//            id = BoardDetailsFragment.boardId;
//        else if (type.equals("t"))
//            id = BoardDetailsFragment.taskId;
//        viewModel.putComment(MyConfig.ADD_ATTACH_GENERAL + id, jsonBody).observe(this, basicResponseStateData -> {
//            dialog.dismiss();
//            switch (basicResponseStateData.getStatus()) {
//                case SUCCESS:
//                    if (basicResponseStateData.getData() != null) {
//                        if (type.equals("t"))
//                            BoardDetailsFragment.alert.dismiss();
//                        else if (type.equals("b"))
//                            BoardDetailsFragment.alertBoard.dismiss();
//
//                        Toast.makeText(this, basicResponseStateData.getData().getMessage(), Toast.LENGTH_SHORT).show();
//
//                    }
//                    break;
//                case FAIL:
//                    Toast.makeText(this, basicResponseStateData.getErrorsMessages() != null ? basicResponseStateData.getErrorsMessages().getErrorMessages().get(0) : null, Toast.LENGTH_SHORT).show();
//                    break;
//                case ERROR:
//                    if (basicResponseStateData.getError() != null) {
//                        Toast.makeText(this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
//                        Log.v("Statues", "Error" + basicResponseStateData.getError().getMessage());
//                    }
//                    break;
//                case CATCH:
//                    Toast.makeText(this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
//                    break;
//            }
//        });
//
//    }
}
