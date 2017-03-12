package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.interfaces.IRepoEntryListener;
import com.dev.sim8500.githapp.interfaces.IRepoVisitView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 25.02.2017.
 */

public class RepoVisitView extends RelativeLayout implements IRepoVisitView {

    @Bind(R.id.comment_txtView) protected TextView repoUrlTxtView;
    @Bind(R.id.author_txtView) protected TextView repoNameTxtView;
    @Bind(R.id.date_txtView) protected TextView dateTxtView;

    protected String ownerLogin;

    protected IRepoEntryListener listener;

    protected class RepoVisitViewListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if(RepoVisitView.this.listener != null) {
                RepoVisitView.this.listener.onRepoChosen();
            }
        }
    }

    public RepoVisitView(Context context) {
        super(context);

        init();
    }

    public RepoVisitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.row_commit, this);

        ButterKnife.bind(this, this);

        RepoVisitViewListener lsnr = new RepoVisitViewListener();

        repoUrlTxtView.setOnClickListener(lsnr);
        repoNameTxtView.setOnClickListener(lsnr);

    }

    public RepoVisitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setName(CharSequence text) {
        repoNameTxtView.setText(text);
    }

    @Override
    public void setOwner(CharSequence owner) {
        this.ownerLogin = owner.toString();
    }

    public void setUrl(CharSequence url) {
        repoUrlTxtView.setText(url);
    }

    public void setVisitDate(CharSequence date) {
        dateTxtView.setText(date);
    }

    @Override
    public void setListener(IRepoEntryListener listener) {
        this.listener = listener;
    }
}
