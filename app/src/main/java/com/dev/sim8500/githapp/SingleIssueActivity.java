package com.dev.sim8500.githapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dev.sim8500.githapp.models.CommentModel;
import com.dev.sim8500.githapp.models.IssueModel;
import com.dev.sim8500.githapp.services.GitHubRepoIssuesService;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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

        ((GitHappApp)getApplication()).inject(this);

        titleTxtView = (TextView)findViewById(R.id.issue_title);
        creatorTxtView = (TextView)findViewById(R.id.userTxtView);
        assigneeTxtView = (TextView)findViewById(R.id.author_txtView);
        bodyTxtView = (TextView)findViewById(R.id.bodyTxtView);
        commentsCounter = (TextView)findViewById(R.id.comment_counter);

        commentsCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (model != null && model.commentsCount > 0)
                {
                    showCommentsDialog();
                }
            }
        });

        if(!authReqManager.hasTokenStored(this))
        {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

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

    private void showCommentsDialog()
    {
        String[] pathParams = extractPathParams();

        authReqManager.getService(GitHubRepoIssuesService.class)
                      .getIssueComments(pathParams[0], pathParams[1], pathParams[2])
                      .enqueue(new Callback<List<CommentModel>>()
                    {
                        @Override
                        public void onResponse(Response<List<CommentModel>> response, Retrofit retrofit)
                        {
                            displayComments(response.body());
                        }

                        @Override
                        public void onFailure(Throwable t)
                        { }
                    });
    }

    private String[] extractPathParams()
    {
        String[] resUrlParts = new String[3];

        if (!TextUtils.isEmpty(model.url))
        {
            int cuttingIndex = model.url.indexOf(URL_CUTTING_PART) + URL_CUTTING_PART.length();
            String urlEnding = model.url.substring(cuttingIndex);

            String[] allParts = urlEnding.split("/");
            if(allParts.length == 4)
            {
                resUrlParts[0] = allParts[0];
                resUrlParts[1] = allParts[1];
                resUrlParts[2] = allParts[3];
            }
        }
        return resUrlParts;
    }

    @UiThread
    private void displayComments(List<CommentModel> comments)
    {
        CommentsAdapter adapter = new CommentsAdapter();
        adapter.initAdapter(this, comments);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dlgView = LayoutInflater.from(this).inflate(R.layout.dialog_comments, null);

        RecyclerView recyclerView = (RecyclerView)dlgView.findViewById(R.id.container);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        builder.setView(dlgView);
        builder.create().show();
    }

    private void applyModel()
    {
        titleTxtView.setText(model.title);
        creatorTxtView.setText(model.user.login);
        assigneeTxtView.setText((model.assignee != null) ? model.assignee.login : "none");
        bodyTxtView.setText(model.body);

        commentsCounter.setText(getResources().getQuantityString(R.plurals.comment_plural, model.commentsCount, model.commentsCount));
    }

    @Inject protected AuthRequestsManager authReqManager;
    private TextView titleTxtView;
    private TextView creatorTxtView;
    private TextView assigneeTxtView;
    private TextView bodyTxtView;
    private TextView commentsCounter;
    
    private IssueModel model;

    private static final String URL_CUTTING_PART = "/repos/";
}
