package com.example.hp.lsquare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static com.parse.Parse.getApplicationContext;


public class BlanksearchFragment extends Fragment {
    static String currprousername;
    static String currproid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v= inflater.inflate(R.layout.fragment_blanksearch, container, false);
        SearchView searchView=(SearchView)v.findViewById(R.id.searchView);
        final ImageView imageView=(ImageView)v.findViewById(R.id.userimageSearch);
        final TextView username=(TextView)v.findViewById(R.id.usernameSearch);
        final TextView names=(TextView)v.findViewById(R.id.namesSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ParseQuery<ParseUser> query1=ParseUser.getQuery();
                query1.whereEqualTo("username",query);
                query1.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        for(final ParseUser object:objects){
                            ParseFile file=(ParseFile)object.get("profileimage");
                            if(file!=null) {
                                file.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        imageView.setImageBitmap(bitmap);
                                    }
                                });
                            }else{
                                imageView.setImageResource(R.drawable.user);
                            }

                            username.setText(object.getUsername());
                            currprousername = object.getUsername();
                            currproid = object.getObjectId();
                            names.setText(object.get("Romeo").toString() + "," + object.get("Juliet").toString());
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

        return v;
    }

}
