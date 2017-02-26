package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
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
    }

    public RepoVisitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setName(CharSequence text) {
        repoNameTxtView.setText(text);
    }

    public void setUrl(CharSequence url) {
        repoUrlTxtView.setText(url);
    }

    public void setVisitDate(CharSequence date) {
        dateTxtView.setText(date);
    }
}
