package com.example.hp.lsquare;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {
    ArrayList<String> message=new ArrayList<String>();
    ArrayList<String> sender=new ArrayList<String>();
    EditText editText;
    ListView listView;
    public void updatechat(){
        listView=(ListView)findViewById(R.id.listchat);
        message.clear();
        sender.clear();
        List msg=ParseUser.getCurrentUser().getList("chatMessage");
        List senderlsit=ParseUser.getCurrentUser().getList("chatsender");
        if(msg!=null) {
            message.addAll(msg);
        }
        if(senderlsit!=null){
            sender.addAll(senderlsit);
        }


        CustomChatAdapter customChatAdapter=new CustomChatAdapter(ChatActivity.this,message,sender);
        listView.setAdapter(customChatAdapter);
        Log.i("msg","updated");
    }
    public void refresh(View view){
        updatechat();
        Toast.makeText(this, "updating..", Toast.LENGTH_SHORT).show();
    }
    public void chatwork(View view){
        String s=editText.getText().toString();
        editText.setText("");
        InputMethodManager manager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(),0);
        if(!s.isEmpty()) {
            Toast.makeText(this, "sending", Toast.LENGTH_SHORT).show();
            ImageView imageView = (ImageView) findViewById(R.id.sendchatmessage);
            ParseUser user = ParseUser.getCurrentUser();
            user.add("chatMessage",s);
            if (Integer.parseInt(newsfeedActivity.v.getTag().toString()) == 1) {
                user.add("chatsender", "romeo");
            } else {
                user.add("chatsender", "juliet");
            }

            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Toast.makeText(ChatActivity.this, "message sent", Toast.LENGTH_SHORT).show();
                    updatechat();
                }
            });

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        editText=(EditText)findViewById(R.id.msg);

       /* message.clear();
        sender.clear();
        CustomChatAdapter customChatAdapter=new CustomChatAdapter(ChatActivity.this,message,sender);
        listView.setAdapter(customChatAdapter);
        */
        updatechat();


    }
}
