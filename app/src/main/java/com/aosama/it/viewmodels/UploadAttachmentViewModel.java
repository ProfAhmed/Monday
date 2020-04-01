package com.aosama.it.viewmodels;

import android.content.Context;
import android.util.Log;

import com.aosama.it.models.responses.BasicResponse;
import com.aosama.it.models.responses.ImageResponse;
import com.aosama.it.models.responses.file.FileResponse;
import com.aosama.it.network.ApiUtilise;
import com.aosama.it.network.IrApiService;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.PreferenceProcessor;
import com.aosama.it.utiles.ProgressRequestBody;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadAttachmentViewModel implements ProgressRequestBody.UploadCallbacks {

    Context mContext;
    UploadImageHandler handler;

    @Override
    public void onProgressUpdate(int percentage) {
        handler.onProgressUpdate(percentage);
    }

    public interface UploadImageHandler {

        void onProgressUpdate(int percentage);

        void onError();

        void onFinish(BasicResponse<FileResponse> imageResponse);


    }

    public UploadAttachmentViewModel(Context mContext, UploadImageHandler handler) {
        this.mContext = mContext;
        this.handler = handler;
    }

    public void doUploadAttachment(File file) {
        IrApiService uploadAPIs = ApiUtilise.getAPIService();
        //Create a file object using file path

        final ProgressRequestBody fileBody = new ProgressRequestBody(file, "image", this);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        String token = PreferenceProcessor.getInstance(mContext).getStr(MyConfig.MyPrefs.TOKEN, "");
        String auth = "Bearer " + token;
        Call<BasicResponse<FileResponse>> call = uploadAPIs.doUploadProfilePicture(auth, filePart);
        call.enqueue(new Callback<BasicResponse<FileResponse>>() {
            @Override
            public void onResponse(Call<BasicResponse<FileResponse>> call, Response<BasicResponse<FileResponse>> response) {
                if (response.message().equals("OK")) {
                    if (response.body().getSuccessful()) {
                        handler.onFinish(response.body());
                    }
                } else {
                    Log.v("ResponseUpload", response.message());
                    handler.onError();

                }
            }

            @Override
            public void onFailure(Call<BasicResponse<FileResponse>> call, Throwable t) {
                Log.v("ResponseUpload", t.getMessage());
                handler.onError();

            }
        });

    }
}
