package com.aosama.it.repository;

import android.app.Application;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aosama.it.models.errors.ErrorsMessages;
import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.sing_in.DataSignIn;
import com.aosama.it.models.wrappers.StateLiveData;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.PreferenceProcessor;
import com.aosama.it.utiles.VolleySingleTone;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class SignInRepository {
    private Application mContext;

    public SignInRepository(Application mContext) {
        this.mContext = mContext;
    }

    public StateLiveData<BasicResponse<DataSignIn>> singIn(String url, JSONObject jsonBody) {

        StateLiveData<BasicResponse<DataSignIn>> signInResponseStateLiveData = new StateLiveData<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        boolean successful = response.getBoolean("successful");
                        if (successful) {
                            Type dataType = new TypeToken<BasicResponse<DataSignIn>>() {
                            }.getType();
                            BasicResponse<DataSignIn> data = new Gson().fromJson(response.toString(), dataType);
                            signInResponseStateLiveData.postSuccess(data);

                        } else {
                            ErrorsMessages error = new Gson().fromJson(response.toString(), ErrorsMessages.class);
                            signInResponseStateLiveData.postFail(error);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        signInResponseStateLiveData.postCatch();
                    }
                }, error -> {
            Log.v("volley_error", error.toString());
            error.printStackTrace();
            signInResponseStateLiveData.postError(error);
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

//                    headers.put("Authorization", "Bearer " + Constants.TOKEN);
                headers.put("lang", PreferenceProcessor.getInstance(mContext).getStr(MyConfig.MyPrefs.LANG, "en"));
                return headers;

            }
        };

        //handle timeout error
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Add the request to the RequestQueue.
        VolleySingleTone.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

        return signInResponseStateLiveData;
    }

}
