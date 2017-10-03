package com.example.harsh.kungfucat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private TwitterLoginButton twitterLoginButton;
    private CallbackManager callbackManager;
    public ShareDialog shareDialog;
    public EditText statusToPost;
    public Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());


        //for twitter
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.CONSUMER_KEY),getString(R.string.CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);


        setContentView(R.layout.activity_main);

        callbackManager=CallbackManager.Factory.create();
        shareDialog= new ShareDialog(this);
        loginButton=(LoginButton) findViewById(R.id.login_button);
        twitterLoginButton=(TwitterLoginButton) findViewById(R.id.twitterLoginButton);
        shareButton=(Button) findViewById(R.id.shareButton);
        statusToPost=(EditText) findViewById(R.id.statusPost);

        loginButton.setReadPermissions("email, publish_actions");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.i("Cancelled", "Cancel");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
        String CallBackUrl="https://kungfucat.com";
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(CallBackUrl)) {
            String access_token = uri.getQueryParameter("access_token");
        }


        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result){
                Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e("MainActivity","Error logging into twitter!");
            }
        });
    }

    public void readyToPost(View view){

        if(shareDialog.canShow(ShareLinkContent.class)){
            ShareLinkContent shareLinkContent=new ShareLinkContent.Builder()
                    .setContentDescription("Hello World")
                    .setContentTitle("Testing facebook integration")
                    .build();
            shareDialog.show(shareLinkContent);
        }

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // for twitter
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }
}
