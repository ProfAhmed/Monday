package com.aosama.it.repository;

import android.app.Application;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aosama.it.models.errors.ErrorsMessages;
import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.boards.CommentGroup;
import com.aosama.it.models.responses.mail.DataMail;
import com.aosama.it.models.wrappers.StateLiveData;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.PreferenceProcessor;
import com.aosama.it.utiles.VolleySingleTone;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MailRepository {
    private Application mContext;

    public MailRepository(Application mContext) {
        this.mContext = mContext;
    }


    private Gson gson = new Gson();

    public StateLiveData<BasicResponse<List<DataMail>>> getInbox(String url) {

        StateLiveData<BasicResponse<List<DataMail>>> boardsResponseStateLiveData = new StateLiveData<>();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        url,
                        null,
                        response -> {
                            try {
                                boolean successful =
                                        response.getBoolean("successful");
                                if (successful) {
                                    Type dataType = new
                                            TypeToken<BasicResponse<List<DataMail>>>() {
                                            }.getType();
                                    BasicResponse<List<DataMail>> data =
                                            gson.fromJson(response.toString(),
                                                    dataType);
                                    boardsResponseStateLiveData.postSuccess(data);

                                } else {
                                    ErrorsMessages error = new Gson().fromJson(response.toString(), ErrorsMessages.class);
                                    boardsResponseStateLiveData.postFail(error);

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                boardsResponseStateLiveData.postCatch();
                            }
                        }, error -> {
                    Log.v("volley_error", error.toString());
                    error.printStackTrace();
                    boardsResponseStateLiveData.postError(error);
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        // Basic Authentication
                        //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                        String token = PreferenceProcessor.getInstance(mContext).getStr(MyConfig.MyPrefs.TOKEN, "");
                        headers.put("Authorization", "Bearer " + token);
                        headers.put("Language", Locale.getDefault().getLanguage());
                        headers.put("tz", MyConfig.TIME_ZONE);
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

        return boardsResponseStateLiveData;
    }

}
