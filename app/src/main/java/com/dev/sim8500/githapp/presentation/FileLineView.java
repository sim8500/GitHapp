package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.interfaces.IFileLineView;
import com.dev.sim8500.githapp.models.FileLineModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 09.04.16.
 */
public class FileLineView extends LinearLayout implements IFileLineView {

    @Bind(R.id.order_txtView)
    protected TextView lineNumberTxtView;

    @Bind(R.id.line_txtView)
    protected TextView lineContentTxtView;

    public FileLineView(Context context) {
        super(context);
        init();
    }

    public FileLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FileLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.row_content_line, this);

        ButterKnife.bind(this, this);
    }

    @Override
    public void setLineNumber(CharSequence number) {
        lineNumberTxtView.setText(number);
    }

    @Override
    public void setLineContent(CharSequence lineContent) {
        lineContentTxtView.setText(lineContent);
        lineContentTxtView.requestLayout();
    }

    @Override
    public void setLineStatus(@FileLineModel.PatchStatus int lineStatus) {
        int bgColor = android.R.color.white;

        switch(lineStatus) {
            case FileLineModel.PATCH_STATUS_ADDED:
                bgColor = R.color.lightAccentGreen;
                break;

            case FileLineModel.PATCH_STATUS_DELETED:
                bgColor = R.color.accentRed;
                break;
        }

        lineContentTxtView.setBackgroundColor(ContextCompat.getColor(getContext(), bgColor));
        lineContentTxtView.refreshDrawableState();
    }
}
