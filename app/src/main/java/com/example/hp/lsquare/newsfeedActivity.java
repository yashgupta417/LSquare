package com.example.hp.lsquare;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class newsfeedActivity extends AppCompatActivity {
    SearchView searchView;

    public void profile(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), profileActivity.class);
        startActivity(intent);
    }
    public void back(MenuItem item){
        ParseUser.logOut();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        searchView=(SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ParseQuery<ParseUser> query1=ParseUser.getQuery();
                query1.whereEqualTo("username",query);
                query1.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        ParseFile file =(ParseFile)ParseUser.getCurrentUser().get("profileimage");
                        for (final ParseUser object:objects) {
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView imageView = (ImageView) findViewById(R.id.imgresult);
                                    imageView.setImageBitmap(bitmap);
                                    TextView textView = (TextView) findViewById(R.id.res);
                                    textView.setText(object.get("Romeo") + "," + object.get("Juliet"));
                                    textView.setBackgroundResource(R.drawable.button_custom);
                                }
                            });
                        }

                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
