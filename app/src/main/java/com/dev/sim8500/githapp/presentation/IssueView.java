package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.SingleIssueActivity;
import com.dev.sim8500.githapp.models.IssueModel;

/**
 * Created by sbernad on 19.12.15.
 */
public class IssueView extends FrameLayout
{

    public IssueView(Context context)
    {
        super(context);
        init();
    }

    public IssueView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public IssueView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.row_issue, this);

        titleTxtView = (TextView)findViewById(R.id.titleTxtView);
        stateTxtView = (TextView)findViewById(R.id.stateTxtView);
        assigneeTxtView = (TextView)findViewById(R.id.author_txtView);
        moreButton = (Button)findViewById(R.id.button);

        moreButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(IssueView.this.model != null)
                {
                    Intent issueIntent = new Intent(IssueView.this.getContext(), SingleIssueActivity.class);
                    issueIntent.putExtra(SingleIssueActivity.ISSUE_MODEL, IssueView.this.model);
                    IssueView.this.getContext().startActivity(issueIntent);
                }
            }
        });
    }

    public void applyModel(IssueModel issue)
    {
        model = issue;

        assigneeTxtView.setText("none");
        titleTxtView.setText(model.title);
        stateTxtView.setText(model.state);

        if(model.assignee != null)
        {
            assigneeTxtView.setText(model.assignee.login);
        }
    }

    private IssueModel model;

    private TextView titleTxtView;
    private TextView stateTxtView;
    private TextView assigneeTxtView;
    private Button moreButton;
}
