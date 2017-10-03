package com.example.harsh.kungfucat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    public ShareDialog shareDialog;
    public EditText statusToPost;
    public Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        callbackManager=CallbackManager.Factory.create();
        shareDialog= new ShareDialog(this);
        loginButton=(LoginButton) findViewById(R.id.login_button);
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
    }

}
