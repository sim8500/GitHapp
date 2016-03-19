package com.dev.sim8500.githapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.dev.sim8500.githapp.app_logic.AuthRequestsManager;
import com.dev.sim8500.githapp.app_logic.GitHappCurrents;
import com.dev.sim8500.githapp.models.UserModel;
import com.dev.sim8500.githapp.presentation.UserView;
import com.dev.sim8500.githapp.services.GitHubUserService;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements AuthRequestsManager.AuthTokenRequestListener,
                                                                Button.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        GitHappApp.getInstance().inject(this);

        setContentView(R.layout.activity_main);

        userPanel = (UserView)findViewById(R.id.user_panel);
        userPanel.setLogOutButtonListener(this);

        final AuthRequestsManager reqMngr = authReqMngr;

        userPanel.getWebView().setWebViewClient(new WebViewClient()
        {
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                reqMngr.obtainAuthToken(url, MainActivity.this, MainActivity.this);
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

        if(authReqMngr.hasTokenStored(this))
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
        if(userModel == null)
        {
            authReqMngr.getService(GitHubUserService.class)
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
        appCurrents.setCurrent("User", userModel);
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
        if(authReqMngr.logOutUser(this))
        {
            userModel = null;
            appCurrents.setCurrent("User", null);
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
        userPanel.applyModel(userModel);
        nextButton.setVisibility(hasUser ? View.VISIBLE : View.GONE);
    }

    private void goToReposScreen()
    {
        Intent reposIntent = new Intent(this, ContainerActivity.class);
        startActivity(reposIntent);
    }

    @Inject protected AuthRequestsManager authReqMngr;
    @Inject protected GitHappCurrents appCurrents;
    private UserModel userModel;
    private UserView userPanel;
    private Button nextButton;
}
