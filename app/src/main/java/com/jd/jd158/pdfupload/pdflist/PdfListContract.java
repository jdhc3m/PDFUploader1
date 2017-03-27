package com.jd.jd158.pdfupload.pdflist;

import com.jd.jd158.pdfupload.network.PdfResponse;

import java.util.List;

public class PdfListContract {

    interface View {

        void showLoading(boolean show);

        void showError(boolean show);

        void showPdfList(List<PdfResponse> pdfs);

    }

    interface UserActionsListener{

        void showPdfList();
    }
}
