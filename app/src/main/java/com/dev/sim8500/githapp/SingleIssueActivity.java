package com.dev.sim8500.githapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.dev.sim8500.githapp.models.IssueModel;

/**
 * Created by sbernad on 20.12.15.
 */
public class SingleIssueActivity extends AppCompatActivity
{
    public static final String ISSUE_MODEL = "com.dev.sim8500.GitHapp.ISSUE_MODEL";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_issue);

        titleTxtView = (TextView)findViewById(R.id.issue_title);
        creatorTxtView = (TextView)findViewById(R.id.userTxtView);
        assigneeTxtView = (TextView)findViewById(R.id.assigneeTxtView);
        bodyTxtView = (TextView)findViewById(R.id.bodyTxtView);
        commentsCounter = (TextView)findViewById(R.id.comment_counter);

        Intent intent = getIntent();
        if(intent.hasExtra(ISSUE_MODEL))
        {
            model = (IssueModel)intent.getParcelableExtra(ISSUE_MODEL);
            if(model != null)
            {
                applyModel();
            }
        }
    }

    private void applyModel()
    {
        titleTxtView.setText(model.title);
        creatorTxtView.setText(model.user.login);
        assigneeTxtView.setText((model.assignee != null) ? model.assignee.login : "none");
        bodyTxtView.setText(model.body);

        commentsCounter.setText(getResources().getQuantityString(R.plurals.comment_plural, model.commentsCount, model.commentsCount));
    }

    private TextView titleTxtView;
    private TextView creatorTxtView;
    private TextView assigneeTxtView;
    private TextView bodyTxtView;
    private TextView commentsCounter;
    
    private IssueModel model;
}
