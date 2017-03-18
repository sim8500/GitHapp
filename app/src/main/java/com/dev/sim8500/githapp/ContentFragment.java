package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dev.sim8500.githapp.app_logic.AuthRequestsManager;
import com.dev.sim8500.githapp.app_logic.GitHappCurrents;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 06.02.16.
 */
public class ContentFragment extends Fragment {

    @Bind(R.id.recycler_view) protected RecyclerView recyclerView;
    @Bind(R.id.progress_bar) protected ProgressBar progressBar;
    @Nullable @Bind(R.id.header_layout) protected RelativeLayout headerLayout;
    @Nullable @Bind(R.id.footer_progress_bar) protected ProgressBar footerProgress;

    @Inject
    protected AuthRequestsManager authReqMngr;

    @Inject
    protected GitHappCurrents appCurrents;

    protected int layoutResId = R.layout.fragment_single_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(layoutResId, container, false);
        ButterKnife.bind(this, inflatedView);

        LinearLayoutManager lm = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);

        return inflatedView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GitHappApp.getInstance().inject(this);
    }
}
