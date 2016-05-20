package com.dev.sim8500.githapp.presentation;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
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
import butterknife.OnClick;

/**
 * Created by sbernad on 23.03.16.
 */
public class CommitEntryDialog implements IDetailedCommitView {

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

        setReadyState(false);

        dialogInstance = new AlertDialog.Builder(context).setView(dlgView)
                                                         .create();
    }

    @Override
    public void setStats(int additions, int deletions, int total) {

        String  contentString = String.format("added: %d, deleted: %d, total: %d",
                additions, deletions, total);
        int delIndex = contentString.indexOf("deleted");

        SpannableString detailsContent = new SpannableString(contentString);
        detailsContent.setSpan(new ForegroundColorSpan(detailsTxtView.getResources().getColor(R.color.accentGreen)),
                0, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        detailsContent.setSpan(new ForegroundColorSpan(detailsTxtView.getResources().getColor(R.color.accentRed)),
                delIndex, delIndex + 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        detailsTxtView.setText(detailsContent);
    }

    @Override
    public void setReadyState(boolean isReady) {
        readyState = isReady;

        buttonsContainer.setVisibility(readyState ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(readyState ? View.GONE : View.VISIBLE);
    }

    private int getColorFromId(View view, int id) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return view.getResources().getColor(id, view.getContext().getTheme());
        }
        else return view.getResources().getColor(id);
    }

    @Override
    public void setListener(ICommitEntryDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void show() {
        dialogInstance.show();
    }

    @OnClick(R.id.tree_button)
    public void onTreeButtonClicked() {
        listener.onViewTree();
        dialogInstance.dismiss();
    }

    @OnClick(R.id.files_button)
    public void onFilesButtonClicked() {
        listener.onViewFiles();
        dialogInstance.dismiss();
    }
}
