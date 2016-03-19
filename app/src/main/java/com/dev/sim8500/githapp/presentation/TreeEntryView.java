package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.app_logic.IEntryViewListener;
import com.dev.sim8500.githapp.app_logic.ITreeEntryView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sbernad on 16.03.16.
 */
public class TreeEntryView extends RelativeLayout implements ITreeEntryView {


    public TreeEntryView(Context context, int layoutId) {
        super(context);
        init(layoutId);
    }

    public TreeEntryView(Context context, AttributeSet attrs, int layoutId) {
        super(context, attrs);
        init(layoutId);
    }

    public TreeEntryView(Context context, AttributeSet attrs, int defStyleAttr, int layoutId) {
        super(context, attrs, defStyleAttr);
        init(layoutId);
    }

    private void init(int layoutId) {
        inflate(getContext(), layoutId, this);

        ButterKnife.bind(this, this);
    }

    public void setTitle(CharSequence text) {
        nameTxtView.setText(text);
    }

    public void setType(CharSequence text) {

    }

    @OnClick(R.id.main_container)
    public void onViewClicked() {

        if(listener != null) {
            listener.onEntryViewChosen();
        }
    }

    public void setViewListener(IEntryViewListener entryListener) {
        listener = entryListener;
    }

    protected IEntryViewListener listener;

    @Bind(R.id.name_txtView) protected TextView nameTxtView;
}
