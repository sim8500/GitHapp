package com.dev.sim8500.githapp.app_logic;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dev.sim8500.githapp.UserListFragment;
import com.dev.sim8500.githapp.services.GitHubUserService;

/**
 * Created by sbernad on 18/03/2017.
 */

public class RecyclerViewLoadMoreListener extends RecyclerView.OnScrollListener {


    public interface IRequestHandler {
        public void doLoadMore();
    }

    protected IRequestHandler requestHandler;

    public RecyclerViewLoadMoreListener(IRequestHandler handler) {
        requestHandler = handler;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

            LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visible = llm.findFirstVisibleItemPosition() + llm.getChildCount();
            int allItems = llm.getItemCount();

            if (visible >= allItems) {
                if(requestHandler != null) {
                    requestHandler.doLoadMore();
                }
            }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

    }
}
