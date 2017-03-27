package com.jd.jd158.pdfupload.pdflist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jd.jd158.pdfupload.R;
import com.jd.jd158.pdfupload.network.PdfResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class PdfListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<PdfResponse> mPdfs;

    PdfListAdapter(Context context, List<PdfResponse> pdfs) {
        mContext = context;
        mPdfs = pdfs;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf, parent, false);
        return new PdfLinkHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PdfResponse pdf = mPdfs.get(position);
        PdfLinkHolder viewHolder = (PdfLinkHolder) holder;
        viewHolder.pdfNameTxt.setText(pdf.name);
        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf.url));
                mContext.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPdfs.size();
    }

    class PdfLinkHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.pdfNameTxt) TextView pdfNameTxt;
        private View.OnClickListener mOnClickListener;

        PdfLinkHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            mOnClickListener = listener;
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v);
            }
        }
    }
}
