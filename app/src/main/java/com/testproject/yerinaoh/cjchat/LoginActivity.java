package com.testproject.yerinaoh.cjchat;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;


/**
 * Created by yerinaoh on 2017. 10. 18..
 */

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        System.out.print("**********");

        LoginButton loginButton;
        Button shadowButton;

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        shadowButton = (Button) findViewById(R.id.shadowButton);

        //비회원접근
        shadowButton.setOnClickListener(new Button.OnClickListener() {
            @Override public void onClick(View view) {

                Toast.makeText(LoginActivity.this, "비회원으로 접속", Toast.LENGTH_LONG).show();
                //액티비티 이동
                Intent intent = new Intent(LoginActivity.this, NicknameActivity.class);
                intent.putExtra("value", "user" + new Random().nextInt(10000));
                startActivity(intent);
                finish();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult result) {
                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_LONG).show();

                GraphRequest request;
                request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        Toast.makeText(LoginActivity.this, "로그인 컴플릿", Toast.LENGTH_LONG).show();
                        if (response.getError() != null) {
                            System.out.print("error가있네");
                        } else {
                            try {
                                String userName = (String)user.get("name");
                                System.out.print("유저정보 :" + userName);
                                Log.i("TAG", "user: " + userName);
                                Log.i("TAG", "AccessToken: " + result.getAccessToken().getToken());
                                setResult(RESULT_OK);

                                //액티비티 이동
                                Intent intent = new Intent(LoginActivity.this, NicknameActivity.class);
                                intent.putExtra("value", userName);
                                startActivity(intent);
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("test", "Error: " + error);
                Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
