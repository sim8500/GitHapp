package com.dev.sim8500.githapp.presentation;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.interfaces.ICommitEntryDialogListener;
import com.dev.sim8500.githapp.interfaces.IDetailedCommitView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 23.03.16.
 */
public class CommitEntryDialog implements IDetailedCommitView, View.OnClickListener {

    protected AlertDialog dialogInstance = null;

    protected ICommitEntryDialogListener listener;

    @Bind(R.id.details_txtView)
    protected TextView detailsTxtView;

    @Bind(R.id.files_button)
    protected Button viewFilesButton;

    @Bind(R.id.tree_button)
    protected Button viewTreeButton;

    @Bind(R.id.buttons_container)
    protected LinearLayout buttonsContainer;

    @Bind(R.id.progressBar)
    protected ProgressBar progressBar;

    protected boolean readyState = false;

    public CommitEntryDialog(Context context) {
        prepareDialog(context);
    }

    protected void prepareDialog(Context context) {

        View dlgView = LayoutInflater.from(context).inflate(R.layout.dialog_commit, null);

        ButterKnife.bind(this, dlgView);
        viewFilesButton.setOnClickListener(this);
        viewTreeButton.setOnClickListener(this);

        setReadyState(false);

        dialogInstance = new AlertDialog.Builder(context).setView(dlgView)
                                                         .create();
    }


    @Override
    public void setStats(int additions, int deletions, int total) {
        detailsTxtView.setText(String.format("added: %d, deleted: %d, total: %d", additions, deletions, total));
    }

    @Override
    public void setReadyState(boolean isReady) {
        readyState = isReady;

        buttonsContainer.setVisibility(readyState ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(readyState ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setListener(ICommitEntryDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void show() {
        dialogInstance.show();
    }

    @Override
    public void onClick(View v) {
        if(listener == null) {
            return;
        }

        if(v == viewFilesButton) {
            listener.onViewFiles();
            dialogInstance.dismiss();
        }
        else if(v == viewTreeButton) {
            listener.onViewTree();
            dialogInstance.dismiss();
        }

    }
}
