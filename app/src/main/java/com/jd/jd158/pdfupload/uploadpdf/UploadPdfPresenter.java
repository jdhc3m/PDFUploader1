package com.jd.jd158.pdfupload.uploadpdf;

import android.support.annotation.NonNull;

import com.jd.jd158.pdfupload.network.Client;
import com.jd.jd158.pdfupload.network.UploadResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class UploadPdfPresenter implements UploadPdfContract.UserActionsListener {

    @NonNull
    private final UploadPdfContract.View mView;

    UploadPdfPresenter(@NonNull UploadPdfContract.View view) {
        mView = view;
    }

    @Override
    public void onClickSelectPdf() {
       // mView.showFileChooser();
    }

    @Override
    public void onClickUploadPdf(String filePath, String fileName) {

        if (fileName.isEmpty() || filePath == null) {
            mView.showUploadError("Please select a PDF and insert a name.");
        } else {

            final RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), new File(filePath));
            MultipartBody.Part part = MultipartBody.Part.createFormData("name", fileName, requestFile);

            Call<UploadResponse> call = Client.getApiService().uploadPdf(part);
            call.enqueue(new Callback<UploadResponse>() {
                @Override
                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                    if (!response.body().error) {
                        mView.showUploadSuccess();
                    } else {
                        mView.showUploadError("Try again, something wrong happened");
                    }
                }

                @Override
                public void onFailure(Call<UploadResponse> call, Throwable t) {
                    mView.showUploadError("Try again, something wrong happened");
                }
            });


        }

    }

    @Override
    public void onClickPdfList() {
        mView.showPdfList();
    }
}
