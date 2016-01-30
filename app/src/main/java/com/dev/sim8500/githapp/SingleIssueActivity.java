package com.dev.sim8500.githapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dev.sim8500.githapp.models.CommentModel;
import com.dev.sim8500.githapp.models.IssueModel;
import com.dev.sim8500.githapp.presentation.ResizeAnimation;
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

    protected enum CardState {
        Normal,
        Expanded,
        Expanding,
        Collapsing
    }

    protected Animation.AnimationListener animListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            if(cardViewState == CardState.Expanded) {
                container.setVisibility(View.GONE);
                cardViewState = CardState.Collapsing;
            }
            else if(cardViewState == CardState.Normal) {
                cardViewState = CardState.Expanding;
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(cardViewState == CardState.Expanding) {
                container.setVisibility(View.VISIBLE);
                cardViewState = CardState.Expanded;
            }
            else if(cardViewState == CardState.Collapsing) {
                cardViewState = CardState.Normal;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    protected CardState cardViewState = CardState.Normal;
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
        ovalButton = (ToggleButton)findViewById(R.id.oval_btn);
        cardView = (CardView)findViewById(R.id.card_view);
        container = (RecyclerView)findViewById(R.id.container);

        ovalButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (cardViewState == CardState.Normal && model != null && model.commentsCount > 0) {

                    showComments();

                    ResizeAnimation rszAnim = new ResizeAnimation(cardView, (int) (2.0f*cardView.getHeight()), cardView.getHeight());
                    rszAnim.setAnimationListener(animListener);
                    rszAnim.setDuration(350);
                    cardView.startAnimation(rszAnim);
                }
                else if(cardViewState == CardState.Expanded) {

                    ResizeAnimation rszAnim = new ResizeAnimation(cardView, (int) (0.5f*cardView.getHeight()), cardView.getHeight());
                    rszAnim.setAnimationListener(animListener);
                    rszAnim.setDuration(250);
                    cardView.startAnimation(rszAnim);
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

    private void showComments()
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
        if(container.getAdapter() == null) {
            CommentsAdapter adapter = new CommentsAdapter();
            adapter.initAdapter(this, comments);

            container.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            container.setAdapter(adapter);
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

    @Inject protected AuthRequestsManager authReqManager;
    private TextView titleTxtView;
    private TextView creatorTxtView;
    private TextView assigneeTxtView;
    private TextView bodyTxtView;
    private TextView commentsCounter;
    private ToggleButton ovalButton;
    private CardView cardView;
    private RecyclerView container;
    
    private IssueModel model;

    private static final String URL_CUTTING_PART = "/repos/";
}
