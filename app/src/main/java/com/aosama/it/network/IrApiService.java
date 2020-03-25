package com.aosama.it.network;


import com.aosama.it.models.responses.ImageResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface IrApiService {
    @Multipart
    @POST("file/file")
    Call<ImageResponse> doUploadProfilePicture(@Header("Authorization") String token, @Part MultipartBody.Part file);
}
