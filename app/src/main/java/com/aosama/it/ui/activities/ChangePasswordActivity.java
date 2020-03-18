package com.aosama.it.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aosama.it.R;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.MyUtilis;
import com.aosama.it.utiles.PreferenceProcessor;
import com.aosama.it.viewmodels.BasicResponsePostViewModel;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePasswordActivity extends AppCompatActivity {

    @BindView(R.id.etOldPassword)
    EditText etOldPassword;
    @BindView(R.id.etNewPassword)
    EditText etNewPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;

    BasicResponsePostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PreferenceProcessor.getInstance(this).getStr(MyConfig.MyPrefs.LOCAL_LANG, "ar").equals("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(BasicResponsePostViewModel.class);
    }

    void changePasswordApi() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("oldPassword", etOldPassword.getText().toString());
            jsonBody.put("password", etNewPassword.getText().toString());
            jsonBody.put("confirmPassword", etConfirmPassword.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog dialog = MyUtilis.myDialog(this);
        dialog.show();

        viewModel.basicResponseStateLiveData(MyConfig.CHANGE_PASSWORD_URL, jsonBody).observe(this, basicResponseStateData -> {
            dialog.dismiss();
            switch (basicResponseStateData.getStatus()) {
                case SUCCESS:
                    if (basicResponseStateData.getData() != null) {
                        Toast.makeText(this, basicResponseStateData.getData().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                case FAIL:
                    Toast.makeText(this, basicResponseStateData.getErrorsMessages() != null ? basicResponseStateData.getErrorsMessages().getErrorMessages().get(0) : null, Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    if (basicResponseStateData.getError() != null) {
                        Toast.makeText(this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                        Log.v("Statues", "Error" + basicResponseStateData.getError().getMessage());
                    }
                    break;
                case CATCH:
                    Toast.makeText(this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    boolean isInputsValid() {
        if (TextUtils.isEmpty(etOldPassword.getText().toString()))
            return false;
        if (TextUtils.isEmpty(etNewPassword.getText().toString()))
            return false;
        return !TextUtils.isEmpty(etConfirmPassword.getText().toString());
    }

    boolean isConfirmedPassword() {
        return etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString());
    }

    public void done(View view) {
        if (isInputsValid()) {
            if (isConfirmedPassword()) {
                changePasswordApi();
            } else {
                Toast.makeText(this, getString(R.string.confirm_password_msg), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.valid_input_msg), Toast.LENGTH_SHORT).show();
        }
    }

    public void finish(View view) {
        onBackPressed();
    }

}
