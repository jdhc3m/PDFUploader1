package com.jd.jd158.pdfupload.network;


import com.google.gson.annotations.SerializedName;

public class UploadResponse {

    @SerializedName("error")
    public boolean error;

    @SerializedName("filename")
    public boolean filename;
}
