package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 06.02.16.
 */
public class ContentFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_single_list, container, false);
        ButterKnife.bind(this, inflatedView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));

        return inflatedView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Bind(R.id.recycler_view) protected RecyclerView recyclerView;
    @Bind(R.id.progress_bar) protected ProgressBar progressBar;
    @Bind(R.id.header_txt_view) protected TextView headerView;
}
