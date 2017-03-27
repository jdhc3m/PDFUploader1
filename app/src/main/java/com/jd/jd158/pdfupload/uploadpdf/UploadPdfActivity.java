package com.jd.jd158.pdfupload.uploadpdf;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.jd.jd158.pdfupload.BaseActivity;
import com.jd.jd158.pdfupload.R;
import com.jd.jd158.pdfupload.pdflist.PdfListActivity;
import com.jd.jd158.pdfupload.util.FilePath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class UploadPdfActivity extends BaseActivity implements UploadPdfContract.View{

    private EditText mName;
    private EditText mDob;
    private EditText mAddress;
    private Button mSaveButton;
    EditText mMobile;
    EditText mID;
    EditText mEmergencyContactName;
    EditText mEmergencyContactMobile;

    File myFile;
    String fileName;
    File pdfFolder;

    public static final String LOG_TAG = "TAG";


    //Pdf request code
    private int PICK_PDF_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Uri to store the image uri
    private String mFilePath = null;

    private UploadPdfContract.UserActionsListener mPresenter;

    //@BindView(R.id.editTextName)
    protected EditText mPdfName;

    @BindView(R.id.urlTextView)
    protected TextView mUrlText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);


        mSaveButton = (Button) findViewById(R.id.button_save);
        mName = (EditText)  findViewById(R.id.name);
        mAddress = (EditText) findViewById(R.id.address);
        mDob = (EditText)  findViewById(R.id.dob);
        mMobile = (EditText)  findViewById(R.id.mobile);
        mID = (EditText)  findViewById(R.id.id);
        mEmergencyContactName = (EditText)  findViewById(R.id.emergency_name);
        mEmergencyContactMobile = (EditText) findViewById(R.id.emergency_mobile);

        requestStoragePermission();
    }

    @Override
    public void dependencyInjection() {
        mPresenter = new UploadPdfPresenter(this);
    }


    @OnClick(R.id.buttonUpload)
    public void onClickUploadPdf() {
        String name = pdfFolder.toString().trim();


        if(name != null){
            mPresenter.onClickUploadPdf(name, fileName);
            mUrlText.setText(getString(R.string.uploading, name));
        }else{
            showUploadError(getString(R.string.storage_error));
        }
    }

    @OnClick(R.id.button_save)
    public void onClickSelectPdf() {

        if (mName.getText().toString().isEmpty()){
            mName.setError("Name is empty");
            mName.requestFocus();
            return;
        }

        if (mID.getText().toString().isEmpty()){
            mID.setError("Name is empty");
            mID.requestFocus();
            return;
        }
        try {
            createPdf();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        mPresenter.onClickSelectPdf();
    }

    @OnClick(R.id.buttonList)
    protected void onClickPdfList() {
        mPresenter.onClickPdfList();
    }


    @Override
    public void showUploadSuccess() {
        Toast.makeText(UploadPdfActivity.this, R.string.upload_success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showUploadError(String message) {
        Toast.makeText(UploadPdfActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_pdf)), PICK_PDF_REQUEST);
    }

    @Override
    public void showPdfList() {
        startActivity(new Intent(this, PdfListActivity.class));
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mFilePath = FilePath.getPath(this, data.getData());
        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
            }
        }
    }

        private void createPdf() throws FileNotFoundException, DocumentException, JSONException {
        pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        if (!pdfFolder.exists()) {
        pdfFolder.mkdir();
        Log.i(LOG_TAG, "Pdf Directory created");
        }

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        myFile = new File(pdfFolder + timeStamp + ".pdf");

        fileName = timeStamp;

        OutputStream output = new FileOutputStream(myFile);

        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        //Step 4 Add content
        String pdfName =  mName.getText().toString();
        String pdfDob =  mDob.getText().toString();
        String pdfAddress = mAddress.getText().toString();
        String pdfId = mID.getText().toString();
        String pdfEmergencyContactName =  mEmergencyContactName.getText().toString();
        String pdfEmergencyContactNumber = mEmergencyContactMobile.getText().toString();

        JSONObject json = makJsonObject(pdfName,
        pdfDob,
        pdfAddress,
        pdfId,
        pdfEmergencyContactName,
        pdfEmergencyContactNumber);

        String jsonString = json.toString();


        document.add(new Paragraph(jsonString));
        viewPdf();

        //Step 5: Close the document
        document.close();
        }

    private void viewPdf(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public JSONObject makJsonObject(String name,
                                    String dob,
                                    String address,
                                    String id,
                                    String emergencyContactName,
                                    String emergencyContactNumber)
            throws JSONException {
        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();
        obj = new JSONObject();
        try {
            obj.put("Name", name);
            obj.put("DOB", dob);
            obj.put("Address", address);
            obj.put("Id", id);
            obj.put("Emergency Name", emergencyContactName);
            obj.put("Emergency Number", emergencyContactNumber);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        jsonArray.put(obj);


        JSONObject finalobject = new JSONObject();
        finalobject.put("pdfDetails", jsonArray);
        return finalobject;
    }


}