package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.interfaces.IFileLineView;

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
    public void setLineNumber(int lineNum) {
        lineNumberTxtView.setText(String.valueOf(lineNum));
    }

    @Override
    public void setLineContent(CharSequence lineContent) {
        lineContentTxtView.setText(lineContent);
    }
}
