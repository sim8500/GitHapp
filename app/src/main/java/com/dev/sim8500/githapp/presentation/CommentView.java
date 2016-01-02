package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.models.CommentModel;

/**
 * Created by Szymon on 2015-12-30.
 */
public class CommentView extends FrameLayout
                         implements ModelView
{
    public CommentView(Context context)
    {
        super(context);
        init();
    }

    public CommentView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public CommentView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.row_comment, this);

        bodyTxtView = (TextView)findViewById(R.id.comment_txtView);
        authorTxtView = (TextView)findViewById(R.id.author_txtView);
    }

    @Override
    public void applyModel(Object model)
    {
        if(model instanceof CommentModel)
        {
            applyModel((CommentModel)model);
        }
    }

    public void applyModel(CommentModel model)
    {
        bodyTxtView.setText(model.body);
        authorTxtView.setText(model.user.login);
    }

    private TextView bodyTxtView;
    private TextView authorTxtView;
}
