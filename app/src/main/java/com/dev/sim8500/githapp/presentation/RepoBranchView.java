package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.interfaces.IRepoBranchView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 30.04.16.
 */
public class RepoBranchView extends RelativeLayout implements IRepoBranchView {

    //@Bind(R.id.check_box) protected CheckBox checkedBox;
    @Bind(R.id.name_txtView) protected TextView nameTxtView;

    public RepoBranchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RepoBranchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RepoBranchView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.row_repo_branch, this);
        ButterKnife.bind(this, this);
    }

    //@Override
    //public void setChecked(boolean checked) {
    //    checkedBox.setChecked(checked);
    //}

    @Override
    public void setName(CharSequence name) {
        nameTxtView.setText(name);
    }

}
