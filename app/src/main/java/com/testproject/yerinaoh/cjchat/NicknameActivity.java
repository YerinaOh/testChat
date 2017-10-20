package com.testproject.yerinaoh.cjchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by yerinaoh on 2017. 10. 20..
 */

public class NicknameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nickname);
        Intent intent = getIntent();
        final EditText nicknameField = (EditText)findViewById(R.id.nicknameField);
        Button submitButton = (Button)findViewById(R.id.submitButton);

        String nickname = (String)intent.getStringExtra("value");
        nicknameField.setText(nickname);

        submitButton.setOnClickListener(new Button.OnClickListener() {
            @Override public void onClick(View view) {

                Intent intent = new Intent(NicknameActivity.this, MainActivity.class);
                intent.putExtra("value", nicknameField.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }
}
