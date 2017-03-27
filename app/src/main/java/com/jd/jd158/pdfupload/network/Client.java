package com.jd.jd158.pdfupload.network;


import com.google.gson.Gson;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class Client {

    private static final String URL = "http://localhost/AndroidPdfUpload/";

    static Retrofit getRetrofitClient() {

        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public static Api getApiService() {
        return getRetrofitClient().create(Api.class);
    }

    public interface Api {

        @Multipart
        @POST("upload.php")
        Call<UploadResponse> uploadPdf(@Part MultipartBody.Part file);

        @GET("getPdfs.php")
        Call<List<PdfResponse>> getPdfs();

    }


}
