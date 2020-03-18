package com.aosama.it.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aosama.it.R;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.MyUtilis;
import com.aosama.it.utiles.PreferenceProcessor;
import com.aosama.it.viewmodels.SignInViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.etUserEmail)
    EditText etUserEmail;
    @BindView(R.id.etUserPassword)
    EditText etUserPassword;
    @BindView(R.id.textLayout)
    TextInputLayout textLayout;
    @BindView(R.id.btnLogin)
    MaterialButton btnLogin;

    private SignInViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PreferenceProcessor.getInstance(this).getStr(MyConfig.MyPrefs.LOCAL_LANG, "ar").equals("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(SignInViewModel.class);

        etUserEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isValidEmail(etUserEmail.getText().toString()))
                    textLayout.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isValidEmail(etUserEmail.getText().toString()))
                    textLayout.setVisibility(View.VISIBLE);
            }
        });
        etUserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etUserPassword.getText().toString().length() > 4)
                    btnLogin.setEnabled(true);
            }
        });
    }

    public boolean isValidEmail(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    private void signInApi() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", etUserEmail.getText().toString());
            jsonBody.put("password", etUserPassword.getText().toString());
            jsonBody.put("deviceId", PreferenceProcessor.getInstance(this).getStr(MyConfig.MyPrefs.FIREBASE_TOKEN, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog dialog = MyUtilis.myDialog(this);
        dialog.show();
        viewModel.signIn(MyConfig.SIGNIN_URL, jsonBody).observe(this, signInResponseStateData -> {
            dialog.dismiss();
            switch (signInResponseStateData.getStatus()) {
                case SUCCESS:

                    if (signInResponseStateData.getData() != null) {
                        PreferenceProcessor.getInstance(this).setStr(MyConfig.MyPrefs.TOKEN,
                                signInResponseStateData.getData().getData().getToken());
                        if (signInResponseStateData.getData().getData().getUser().getMustChangePassword()) {
                            startActivity(new Intent(this, ChangePasswordActivity.class));
                        } else {
//                            PreferenceProcessor.getInstance(this).setStr(MyConfig.MyPrefs.NAME, signInResponseStateData.getData().getData().getUser().getName());
//                            PreferenceProcessor.getInstance(this).setStr(MyConfig.MyPrefs.IMAGE, signInResponseStateData.getData().getData().getUser().getImage());
//                            PreferenceProcessor.getInstance(this).setFloat(MyConfig.MyPrefs.RATE, signInResponseStateData.getData().getData().getUser().getRate());
                            PreferenceProcessor.getInstance(this).setBool(MyConfig.MyPrefs.IS_LOGIN, true);

                            startActivity(new Intent(this, HomeActivity.class));
                            finish();

                        }
                    }
                    break;
                case FAIL:
                    Toast.makeText(this, signInResponseStateData.getErrorsMessages() != null ? signInResponseStateData.getErrorsMessages().getErrorMessages().get(0) : null, Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    if (signInResponseStateData.getError() != null) {
                        Toast.makeText(this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                        Log.v("Statues", "Error" + signInResponseStateData.getError().getMessage());
                    }
                    break;
                case CATCH:
                    Toast.makeText(this, getString(R.string.no_connection_msg), Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    public void login(View view) {
        signInApi();

    }

    public void back(View view) {
        onBackPressed();
    }
}
