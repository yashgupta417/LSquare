package com.example.hp.lsquare;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class BlanknewsfeedFragment extends Fragment {
    ImageView imageView;
    static ListView listView;
    static  CustomAdapter customAdapter;
    static ArrayList<Bitmap> arrayList=new ArrayList<Bitmap>();
    static ArrayList<String> info=new ArrayList<String>();
    static ArrayList<Integer> loves=new ArrayList<Integer>();
    static ArrayList<String> comments=new ArrayList<String>();
    static ArrayList<String> photoid=new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v=inflater.inflate(R.layout.fragment_blanknewsfeed, container, false);
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("followers");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()>0) {
                    for (ParseObject object : objects) {
                        List list=object.getList("following");
                        if(list!=null) {
                            if (list.size() > 0) {

                                List<ParseQuery<ParseObject>> querynewsfeed = new ArrayList<ParseQuery<ParseObject>>();
                                for (int i = 0; i < list.size(); i++) {
                                    ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Pictures");
                                    query1.whereEqualTo("username", list.get(i));
                                    querynewsfeed.add(query1);
                                }
                                ParseQuery<ParseObject> query1 = ParseQuery.or(querynewsfeed);
                                query1.orderByDescending("createdAt");

                                arrayList.clear();
                                info.clear();
                                loves.clear();
                                comments.clear();
                                photoid.clear();
                                listView = (ListView) v.findViewById(R.id.listnewsfeed);
                                query1.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if(objects.size()==0){
                                            TextView textView0=(TextView)v.findViewById(R.id.t);
                                            textView0.setText("No feeds");
                                        }
                                        for (final ParseObject object : objects) {
                                            ParseFile file = (ParseFile) object.get("uploadedimg");
                                            if (file != null) {
                                                file.getDataInBackground(new GetDataCallback() {
                                                    @Override
                                                    public void done(byte[] data, ParseException e) {


                                                        Log.i("msg", "inside");
                                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                        arrayList.add(bitmap);
                                                        loves.add(object.getInt("loves"));

                                                        photoid.add(object.getObjectId());
                                                        List list = object.getList("commentsArray");
                                                        if (list != null) {
                                                            comments.addAll(list);
                                                        }

                                                        info.add(object.getString("username"));
                                                        customAdapter = new CustomAdapter(getContext(), arrayList, info, loves, comments, photoid);
                                                        listView.setAdapter(customAdapter);
                                                        ProgressBar progressBar=(ProgressBar)v.findViewById(R.id.loading);
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                });
                                            }

                                        }
                                    }
                                });
                            }
                        }else{

                            TextView textView0=(TextView)v.findViewById(R.id.t);
                            textView0.setText("No feeds");
                            ProgressBar progressBar=(ProgressBar)v.findViewById(R.id.loading);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
        return v;
    }


}
