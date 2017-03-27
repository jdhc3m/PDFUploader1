package com.jd.jd158.pdfupload.pdflist;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.jd.jd158.pdfupload.BaseActivity;
import com.jd.jd158.pdfupload.R;
import com.jd.jd158.pdfupload.network.PdfResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;

public class PdfListActivity extends BaseActivity implements PdfListContract.View {

    private PdfListContract.UserActionsListener mPresenter;

    @BindView(R.id.loading)
    ProgressBar mLoading;

    @BindView(R.id.contentLayout)
    View mContentLayout;

    @BindView(R.id.feedRv)
    RecyclerView mFeedRv;

    @BindView(R.id.errorLayout)
    View mErrorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);

        prepareToolbar(R.string.title_activity_pdf_list, false);

        mPresenter.showPdfList();
    }

    @Override
    public void dependencyInjection() {
        mPresenter = new PdfListPresenter(this);
    }

    @Override
    public void showLoading(boolean show) {
        mContentLayout.setVisibility(show ? GONE : View.VISIBLE);
        mLoading.setVisibility(show ? View.VISIBLE : GONE);
    }

    @Override
    public void showError(boolean show) {
        if (show) {
            mContentLayout.setVisibility(GONE);
            mErrorLayout.setVisibility(View.VISIBLE);
        } else {
            mErrorLayout.setVisibility(GONE);
        }
    }

    @Override
    public void showPdfList(List<PdfResponse> pdf){
        PdfListAdapter adapter = new PdfListAdapter(this, pdf);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mFeedRv.setLayoutManager(layoutManager);
        mFeedRv.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(mFeedRv.getContext(), layoutManager.getOrientation());
        mFeedRv.addItemDecoration(dividerItemDecoration);
    }

    @OnClick(R.id.tryAgainTxt)
    public void onTryAgainClick() {
        mPresenter.showPdfList();
    }

}
