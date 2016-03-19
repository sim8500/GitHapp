package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.app_logic.ICommitView;
import com.dev.sim8500.githapp.app_logic.IEntryViewListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sbernad on 22.02.16.
 */
public class CommitView extends RelativeLayout implements ICommitView {

    public CommitView(Context context) {
        super(context);
        init();
    }

    public CommitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.row_commit, this);

        ButterKnife.bind(this, this);
    }

    public void setMessage(CharSequence text) {
        messageTxtView.setText(text);
    }

    public void setAuthor(CharSequence author) {
        authorTxtView.setText(author);
    }

    public void setCommitDate(CharSequence date) {
        dateTxtView.setText(date);
    }

    public void setViewListener(IEntryViewListener listener) {
        this.listener = listener;
    }

    @Bind(R.id.comment_txtView) protected TextView messageTxtView;
    @Bind(R.id.author_txtView) protected TextView authorTxtView;
    @Bind(R.id.date_txtView) protected TextView dateTxtView;
    @Bind(R.id.main_container) protected LinearLayout mainContainer;

    @OnClick(R.id.main_container)
    public void onViewClicked() {

        if(listener != null) {
            listener.onEntryViewChosen();
        }
    }

    protected IEntryViewListener listener;
}
