package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.interfaces.IEntryViewListener;
import com.dev.sim8500.githapp.interfaces.IFileEntryView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sbernad on 02.04.16.
 */
public class FileEntryView extends RelativeLayout implements IFileEntryView{
    public FileEntryView(Context context) {

        super(context);

        init();
    }

    public FileEntryView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public FileEntryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.row_file, this);

        ButterKnife.bind(this, this);
    }

    @Bind(R.id.filename_txtView)
    protected TextView filenameTxtView;

    @Bind(R.id.status_txtView)
    protected TextView statusTxtView;

    @Bind(R.id.changes_txtView)
    protected TextView changesTxtView;

    public void setViewListener(IEntryViewListener listener) {
        this.listener = listener;
    }

    @Override
    public void setFilename(CharSequence filename) {
        filenameTxtView.setText(filename);
    }

    @Override
    public void setChangesCount(int count) {
        changesTxtView.setText(String.format("(%d)", count));
    }

    @Override
    public void setStatus(CharSequence status) {
        statusTxtView.setText(status);
    }

    @OnClick(R.id.relativeLayout)
    public void onEntryClicked() {
        if(listener != null) {
            listener.onEntryViewChosen();
        }
    }

    protected IEntryViewListener listener;
}
