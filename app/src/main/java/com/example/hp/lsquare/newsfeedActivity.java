package com.example.hp.lsquare;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import static com.parse.Parse.getApplicationContext;

public class newsfeedActivity extends AppCompatActivity {

    public void workother(View view){
        Intent intent=new Intent(getApplicationContext(),othersProfileActivity.class);
        startActivity(intent);
    }
    public void profile(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), profileActivity.class);
        startActivity(intent);
    }
    public void search(MenuItem item){
        BlanksearchFragment blanksearchFragment=new BlanksearchFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentcontainer,blanksearchFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    public void home(MenuItem item){
        BlanknewsfeedFragment blanknewsfeedFragment=new BlanknewsfeedFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentcontainer,blanknewsfeedFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void chatfrag(MenuItem item){
        ChatFragment chatFragment=new ChatFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentcontainer,chatFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void logout(MenuItem item){
        ParseUser.logOut();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        Toolbar toolbar=(Toolbar)findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("LSquare");


    }
    static View v;
    static String user;
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        ParseUser parseUser=ParseUser.getCurrentUser();
        if(requestCode==1){
            if(Integer.parseInt(v.getTag().toString())==1) {
                parseUser.getCurrentUser().put("romeoOnline", 0);


            }else if(Integer.parseInt(v.getTag().toString())==2){
                parseUser.getCurrentUser().put("julietOnline", 0);
            }
        }
    }

    public void onchat(View view){

        v=view;
        TextView textView=(TextView)view;
        final Intent intent=new Intent(newsfeedActivity.this,ChatActivity.class);
        ParseUser parseUser=ParseUser.getCurrentUser();
        Log.i("msg","checking online or not");
        if(Integer.parseInt(v.getTag().toString())==1) {


                parseUser.put("romeoOnline", 1);
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        user="romeo";
                        startActivityForResult(intent,1);
                    }
                });



        }else if(Integer.parseInt(v.getTag().toString())==2){

                parseUser.getCurrentUser().put("julietOnline", 1);
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        user="juliet";
                        startActivityForResult(intent,1);
                    }
                });



        }

    }

    public void commentdone(View view){
        final View parent=(View) view.getParent();
        final EditText editTextcomment=(EditText)parent.findViewById(R.id.back);
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Pictures");
        query.getInBackground(editTextcomment.getTag().toString(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                object.add("commentsArray", editTextcomment.getText().toString());
                object.add("commentsUsers",ParseUser.getCurrentUser().getUsername());
                object.saveInBackground();
                editTextcomment.setText("");
                InputMethodManager manager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(editTextcomment.getWindowToken(),0);
                //updating comments
                List list=object.getList("commentsArray");
                List list2=object.getList("commentsUsers");
                TextView textViewcomments=(TextView)parent.findViewById(R.id.comment);
                if(list.size()==0){
                    textViewcomments.setText("no comments till now");
                }

                else {
                    textViewcomments.setText("");
                    for (int i = 0; i < list.size(); i++) {
                        textViewcomments.append(list2.get(i).toString()+" "+list.get(i).toString() + "\n");
                    }
                }
                //updating ends here
            }
        });

    }
    public  void commentwork(final View view){
        final View parent=(View) view.getParent();
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Pictures");
        query.getInBackground(view.getTag().toString(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e==null){

                    List list=object.getList("commentsArray");
                    List list2=object.getList("commentsUsers");
                    TextView textViewcomments=(TextView)parent.findViewById(R.id.comment);
                    if(list.size()==0){
                        textViewcomments.setText("no comments till now");
                    }

                    else {
                        textViewcomments.setText("");
                        for (int i = 0; i < list.size(); i++) {

                            textViewcomments.append(list2.get(i).toString()+" "+list.get(i).toString() + "\n");

                        }
                    }

                    /*
                    editText.animate().alpha(1);
                    editText.setEnabled(true);
                    imageViewsend=(ImageView)parent.findViewById(R.id.sendcomment);
                    imageViewsend.setEnabled(true);
                    imageViewsend.animate().alpha(1);*/
                }
            }
        });
    }
    public void lovework(final View view){

        ParseQuery<ParseObject> query=ParseQuery.getQuery("Pictures");

        query.getInBackground(view.getTag().toString(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e==null) {
                    int index=BlanknewsfeedFragment.photoid.indexOf(view.getTag().toString());
                    int x = BlanknewsfeedFragment.loves.get(index);
                    if(object.getList("userslovethis") !=null && object.getList("userslovethis").contains(ParseUser.getCurrentUser().getUsername())){
                        object.put("loves",x-1);
                        BlanknewsfeedFragment.loves.set(index,x-1);
                        object.getList("userslovethis").remove(ParseUser.getCurrentUser().getUsername());
                        List tempusers=object.getList("userslovethis");
                        object.remove("userslovethis");
                        object.put("userslovethis",tempusers);
                    }
                    else {
                        object.put("loves", x + 1);
                        BlanknewsfeedFragment.loves.set(index, x + 1);
                        object.add("userslovethis",ParseUser.getCurrentUser().getUsername());
                    }
                    object.saveInBackground();
                    BlanknewsfeedFragment.customAdapter = new CustomAdapter(newsfeedActivity.this, BlanknewsfeedFragment.arrayList, BlanknewsfeedFragment.info, BlanknewsfeedFragment.loves, BlanknewsfeedFragment.comments,BlanknewsfeedFragment.photoid);
                    BlanknewsfeedFragment.listView.setAdapter(BlanknewsfeedFragment.customAdapter);
                }
                else{
                    Log.i("msg","error");
                }
            }
        });


    }
}
