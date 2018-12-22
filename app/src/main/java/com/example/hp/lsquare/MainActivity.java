package com.example.hp.lsquare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    public void login(View view){
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user!=null){
                    Intent intent=new Intent(getApplicationContext(),newsfeedActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void signup(View view){
        Intent intent=new Intent(getApplicationContext(),SignupActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ParseUser.getCurrentUser()!=null){
            Intent intent=new Intent(getApplicationContext(),newsfeedActivity.class);
            startActivity(intent);
        }

        username=(EditText)findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}
