package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.AuthRequestsManager;
import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.models.UserModel;

/**
 * Created by sbernad on 19.12.15.
 */
public class UserView extends LinearLayout
{
    public UserView(Context context)
    {
        super(context);
        init();
    }

    public UserView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public UserView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.user_panel, this);

        infoTxtView = (TextView)findViewById(R.id.panel_info);
        nameTxtView = (TextView)findViewById(R.id.user_name);

        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

    }

    public void loadLoginPage()
    {
        webView.loadUrl(AuthRequestsManager.getInstance().getOAuthURL().toString());
    }

    @UiThread
    public void changeUserDataVisibility(boolean visible)
    {

        int visibility = visible ? View.VISIBLE : View.GONE;
        int webViewVisibility = visible ? View.GONE : View.VISIBLE;

        nameTxtView.setVisibility(visibility);
        infoTxtView.setText(getContext().getString(visible ? R.string.you_re_logged : R.string.log_in));

        webView.setVisibility(webViewVisibility);
    }

    public void applyUser(UserModel model)
    {
        this.user = model;
        nameTxtView.setText(model.name);

        changeUserDataVisibility(true);
    }

    private TextView infoTxtView;
    private TextView nameTxtView;
    public WebView webView;

    private UserModel user;
}
