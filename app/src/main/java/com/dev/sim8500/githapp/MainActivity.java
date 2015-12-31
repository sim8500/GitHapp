package com.dev.sim8500.githapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.sim8500.githapp.models.AuthData;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.models.TokenModel;
import com.dev.sim8500.githapp.models.UserModel;
import com.dev.sim8500.githapp.presentation.RepoView;
import com.dev.sim8500.githapp.presentation.UserView;
import com.dev.sim8500.githapp.services.GitHubAuthTokenService;
import com.dev.sim8500.githapp.services.GitHubUserReposService;
import com.dev.sim8500.githapp.services.GitHubUserService;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements AuthRequestsManager.AuthTokenRequestListener,
                                                                Button.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userPanel = (UserView)findViewById(R.id.user_panel);

        userPanel.setLogOutButtonListener(this);

        userPanel.getWebView().setWebViewClient(new WebViewClient()
        {
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                AuthRequestsManager.getInstance().obtainAuthToken(url, MainActivity.this, MainActivity.this);
            }

        });

        nextButton = (Button)findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReposScreen();
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        setUpUserUI();

        if(userModel != null)
        {
            return;
        }

        AuthRequestsManager authReq = AuthRequestsManager.getInstance();
        if(authReq.hasTokenStored(this))
        {
            onTokenReceived();
        }
        else
        {
            userPanel.loadLoginPage();
        }
    }

    @UiThread
    public void onTokenReceived()
    {
        AuthRequestsManager arqm = AuthRequestsManager.getInstance();

        if(userModel == null)
        {
            arqm.getService(GitHubUserService.class)
                    .getUser()
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Response<UserModel> response, Retrofit retrofit) {
                            onUserModelReceived(response.body());
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
        }
    }

    @UiThread
    public void onTokenFailed()
    {
        Toast.makeText(this, "Authorization has failed.", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    public void onUserModelReceived(UserModel user)
    {
        userModel = user;
        setUpUserUI();
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.log_out_button)
        {
            performUserLogOut();
        }
    }

    private void performUserLogOut()
    {
        AuthRequestsManager authReqMngr = AuthRequestsManager.getInstance();
        if(authReqMngr.logOutUser(this))
        {
            userModel = null;

            setUpUserUI();

            clearPreviousSession();
            userPanel.loadLoginPage();
        }
    }

    private void clearPreviousSession()
    {
        CookieManager cookieManager = CookieManager.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            cookieManager.removeAllCookies(null);
        }
        else
        {
            cookieManager.removeAllCookie();
        }
        userPanel.getWebView().clearCache(true);
    }

    private void setUpUserUI()
    {
        boolean hasUser = (userModel != null);
        userPanel.applyUser(userModel);
        nextButton.setVisibility(hasUser ? View.VISIBLE : View.GONE);
    }

    private void goToReposScreen()
    {
        Intent reposIntent = new Intent(this, RepoIssuesActivity.class);
        startActivity(reposIntent);
    }

    private UserModel userModel;
    private UserView userPanel;
    private Button nextButton;
}
