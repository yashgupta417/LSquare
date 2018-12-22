package com.example.hp.lsquare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {
    EditText romeo,juliet,loveHandle,pass1,pass2;
    public void signup(View view){
        if(romeo.getText().toString().isEmpty() ||
                juliet.getText().toString().isEmpty() ||
                loveHandle.getText( ).toString().isEmpty() ||
                pass1.getText().toString().isEmpty() ||
                pass2.getText().toString().isEmpty()){
            Toast.makeText(this, "Fields can not remain empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!(pass1.getText().toString().equals(pass2.getText().toString()))){
            Toast.makeText(this, "Password not matched", Toast.LENGTH_SHORT).show();
            return;
        }
        ParseUser user=new ParseUser();
        user.setUsername(loveHandle.getText().toString());
        user.setPassword(pass1.getText().toString());
        user.put("Romeo",romeo.getText().toString());
        user.put("Juliet",juliet.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(SignupActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),newsfeedActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SignupActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        romeo=(EditText) findViewById(R.id.romeo);
        juliet=(EditText) findViewById(R.id.juliet);
        loveHandle=(EditText) findViewById(R.id.lovehandle);
        pass1=(EditText) findViewById(R.id.pass1);
        pass2=(EditText)findViewById(R.id.pass2);

    }
}
