package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.models.IssueModel;
import com.dev.sim8500.githapp.models.RepoModel;

/**
 * Created by sbernad on 19.12.15.
 */

public class RepoView extends FrameLayout
                      implements ModelView<RepoModel>
{

    public interface OnRepoChosenListener
    {
        void onRepoChosen(String repoName, String owner);
    }

    public RepoView(Context context)
    {
        super(context);
        init();
    }

    public RepoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public RepoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.row_repo, this);

        nameTxtView = (TextView)findViewById(R.id.nameTxtView);
        urlTxtView = (TextView)findViewById(R.id.urlTxtView);
        descTxtView = (TextView)findViewById(R.id.descTxtView);

        rowContainer = (LinearLayout)findViewById(R.id.row_container);
        rowContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (repoChosenListener != null)
                    repoChosenListener.onRepoChosen(model.name, model.owner.login);
            }
        });
    }

    public void setOnRepoChosenListener(OnRepoChosenListener listener)
    {
        this.repoChosenListener = listener;
    }

    @Override
    public void applyModel(RepoModel repo)
    {
        model = repo;

        nameTxtView.setText(model.name);
        urlTxtView.setText(model.url);
        descTxtView.setText(model.description);
    }

    private TextView nameTxtView;
    private TextView urlTxtView;
    private TextView descTxtView;
    private LinearLayout rowContainer;

    private RepoModel model;
    private OnRepoChosenListener repoChosenListener;
}
