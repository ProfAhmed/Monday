package com.aosama.it.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.aosama.it.R;
import com.aosama.it.ui.adapter.BottomSheet;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InboxDetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvMessage)
    TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_details);
        ButterKnife.bind(this);
        String image = getIntent().getStringExtra("image");
        String name = getIntent().getStringExtra("name");
        String short_name = getIntent().getStringExtra("short_name");
        String body = getIntent().getStringExtra("body");

        tvName.setText(name);
        tvMessage.setText(body);
        if (image != null && !TextUtils.isEmpty(image) && image.length() > 0) {
            Picasso.get().load(image)
                    .into(ivAvatar, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        } else {
            String firstChar = "";
            if (short_name != null && short_name.length() > 0) {

                TextDrawable drawable2 = createTextDrawable(short_name);
                ivAvatar.setImageDrawable(drawable2);
            }
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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

    public void reply(View v) {

        BottomSheet bottomSheet = new BottomSheet();
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

    }
}
