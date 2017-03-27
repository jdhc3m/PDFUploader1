package com.jd.jd158.pdfupload.uploadpdf;

public class UploadPdfContract {

    interface View {

        void showUploadSuccess();

        void showUploadError(String message);

        void showFileChooser();

        void showPdfList();

    }

    interface UserActionsListener{

        void onClickSelectPdf();

        void onClickUploadPdf(String filePath, String fileName);

        void onClickPdfList();
    }
}
