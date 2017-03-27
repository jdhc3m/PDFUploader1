package com.jd.jd158.pdfupload.pdflist;

import android.support.annotation.NonNull;

import com.jd.jd158.pdfupload.network.Client;
import com.jd.jd158.pdfupload.network.PdfResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class PdfListPresenter implements PdfListContract.UserActionsListener {

    @NonNull
    private final PdfListContract.View mView;

    PdfListPresenter(@NonNull PdfListContract.View view) {
        mView = view;
    }

    @Override
    public void showPdfList() {
        mView.showError(false);
        mView.showLoading(true);
        Call<List<PdfResponse>> call = Client.getApiService().getPdfs();
        call.enqueue(new Callback<List<PdfResponse>>() {
            @Override
            public void onResponse(Call<List<PdfResponse>> call, Response<List<PdfResponse>> response) {
                mView.showLoading(false);
                mView.showPdfList(response.body());
            }

            @Override
            public void onFailure(Call<List<PdfResponse>> call, Throwable t) {
                mView.showLoading(false);
                mView.showError(true);
            }
        });
    }
}
