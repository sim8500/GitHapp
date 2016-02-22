package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.models.CommitModel;

import java.security.InvalidParameterException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 22.02.16.
 */
public class CommitView extends FrameLayout implements ModelView {


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
        inflate(getContext(), R.layout.row_comment, this);

        ButterKnife.bind(this, this);

    }

    @Override
    public void applyModel(Object model) {

        if(model instanceof CommitModel) {
            applyModel((CommitModel)model);
            return;
        }
        throw new InvalidParameterException("model should be an instance of CommitModel");

    }

    private void applyModel(CommitModel commit) {
        this.model = commit;

        messageTxtView.setText(model.commit.message);
        authorTxtView.setText(model.commit.author.name);
    }

    @Bind(R.id.comment_txtView) protected TextView messageTxtView;
    @Bind(R.id.author_txtView) protected TextView authorTxtView;

    private CommitModel model;
}
