package com.testproject.yerinaoh.cjchat;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static TextView userCount;
    static ListView listView;
    static EditText editText;
    static Button sendButton = null;
    static String userName = "";
    static ArrayAdapter adapter;
    final ArrayList<String> LIST_CONTENTS = new ArrayList<String>() ;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_main);

        userCount = (TextView) findViewById(R.id.userCount);
        listView = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.editText);
        sendButton = (Button) findViewById(R.id.button);

        String nickname = (String)intent.getStringExtra("value");

        userName = nickname;

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,LIST_CONTENTS);
        listView.setAdapter(adapter);

        sendButton.setOnClickListener(new Button.OnClickListener() {
            @Override public void onClick(View view) {
                System.out.println("*******CHILD등록******");
                ChatData chatData = new ChatData(userName, editText.getText().toString());
                databaseReference.child("message").push().setValue(chatData);
                editText.setText("");
            }
        });

        databaseReference.child("message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);

                if (adapter != null) {
                    System.out.println("****************** \n" + chatData.getUserName() + chatData.getMessage() + "\n******************");
                    Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");
                    userCount.setText(dataSnapshot.getChildrenCount() + "명");
                    adapter.add(chatData.getUserName() + ": " + chatData.getMessage());
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    Log.e(snap.getKey(),snap.getChildrenCount() + "");
                }
                System.out.println("****************** 호옹이 바뀜!*****************");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

}
