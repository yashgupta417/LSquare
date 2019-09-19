package com.example.hp.lsquare;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class othersProfileActivity extends AppCompatActivity {
    ImageView imageView;
    ListView listView;
    CustomAdapter customAdapter;
    ArrayList<Bitmap> arrayList=new ArrayList<Bitmap>();
    ArrayList<String> info=new ArrayList<String>();
    ArrayList<Integer> loves=new ArrayList<Integer>();
    ArrayList<String> comments=new ArrayList<String>();
    ArrayList<String> photoid=new ArrayList<String>();
    TextView followteller;

    public void doFollowOrUnFollow(View view){

         if(followteller.getText().toString().equals("Follow")) {
              ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("followers");
              query.whereEqualTo("username",BlanksearchFragment.currprousername);
              query.findInBackground(new FindCallback<ParseObject>() {
                  @Override
                  public void done(List<ParseObject> objects, ParseException e) {
                      for(final ParseObject object:objects){
                          object.add("follower",ParseUser.getCurrentUser().getUsername());
                          object.saveInBackground(new SaveCallback() {
                              @Override
                              public void done(ParseException e) {
                                  ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("followers");
                                  query1.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                                  query1.findInBackground(new FindCallback<ParseObject>() {
                                      @Override
                                      public void done(List<ParseObject> objects, ParseException e) {
                                          for (ParseObject object1:objects){
                                              object1.add("following",object.getString("username"));
                                              object1.saveInBackground(new SaveCallback() {
                                                  @Override
                                                  public void done(ParseException e) {
                                                      updatefollowinfo();
                                                  }
                                              });
                                          }
                                      }
                                  });
                              }
                          });

                      }

                  }
              });
                     followteller.setText("Following");





         }else{

             ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("followers");
             query.whereEqualTo("username",BlanksearchFragment.currprousername);
             query.findInBackground(new FindCallback<ParseObject>() {
                 @Override
                 public void done(List<ParseObject> objects, ParseException e) {
                     for(final ParseObject object:objects){
                         object.getList("follower").remove(ParseUser.getCurrentUser().getUsername());
                         List list=object.getList("follower");
                         object.remove("follower");
                         object.put("follower",list);
                         object.saveInBackground(new SaveCallback() {
                             @Override
                             public void done(ParseException e) {
                                 ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("followers");
                                 query1.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                                 query1.findInBackground(new FindCallback<ParseObject>() {
                                     @Override
                                     public void done(List<ParseObject> objects, ParseException e) {
                                         for (ParseObject object1:objects){
                                             object1.getList("following").remove(object.getString("username"));
                                             List list1=object1.getList("following");
                                             object1.remove("following");
                                             object1.put("follower",list1);
                                             object1.saveInBackground(new SaveCallback() {
                                                 @Override
                                                 public void done(ParseException e) {
                                                     updatefollowinfo();
                                                 }
                                             });
                                         }
                                     }
                                 });
                             }
                         });

                     }

                 }
             });


            followteller.setText("Follow");
         }

    }
    public void updatefollowinfo(){
        ParseQuery<ParseObject> queryuser=new ParseQuery<ParseObject>("followers");
        queryuser.whereEqualTo("username",BlanksearchFragment.currprousername);
        queryuser.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for(ParseObject object:objects){
                    TextView followersNumber=(TextView)findViewById(R.id.followersNumber1);
                    TextView followingsNumber=(TextView)findViewById(R.id.followingsNumber1);
                    List fer=object.getList("follower");
                    List fing=object.getList("following");
                    if(fer!=null) {
                        followersNumber.setText(Integer.toString(fer.size()));
                        Log.i("msg",Integer.toString(fer.size()));
                        if(fer.contains(ParseUser.getCurrentUser().getUsername())){
                            followteller.setText("Following");
                        }
                    }else{
                        followersNumber.setText("0");
                    }if(fing!=null) {
                        followingsNumber.setText(Integer.toString(fing.size()));
                    }else {
                        followingsNumber.setText("0");
                    }

                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);
        followteller=(TextView)findViewById(R.id.textView51);



        updatefollowinfo();


        imageView=(ImageView)findViewById(R.id.profilepic1);
        final TextView textView=(TextView) findViewById(R.id.handler1);





        final ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Pictures");
        query.whereEqualTo("username",BlanksearchFragment.currprousername);
        arrayList.clear();
        info.clear();
        loves.clear();
        comments.clear();
        photoid.clear();
        listView=(ListView)findViewById(R.id.listView1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if(e==null ) {

                    for (final ParseObject object : objects) {
                        ParseFile file = (ParseFile) object.get("uploadedimg");
                        if(file!=null) {
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {


                                    Log.i("msg", "inside");
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    arrayList.add(bitmap);
                                    loves.add(object.getInt("loves"));

                                    photoid.add(object.getObjectId());
                                    List list = object.getList("commentsArray");
                                    if(list!=null) {
                                        comments.addAll(list);
                                    }

                                    info.add(object.getString("username"));
                                    customAdapter = new CustomAdapter(othersProfileActivity.this, arrayList, info, loves, comments,photoid);
                                    listView.setAdapter(customAdapter);


                                }
                            });
                        }

                    }



                }
            }
        });
        ParseQuery<ParseUser> query1=ParseUser.getQuery();
        query1.getInBackground(BlanksearchFragment.currproid, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {


                textView.setText(object.getString("username"));

                ParseFile file = (ParseFile) object.get("profileimage");

                file.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if (e == null && data != null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            imageView.setImageBitmap(bitmap);
                        }
                        ProgressBar progressBar=(ProgressBar)findViewById(R.id.load1);
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });
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
                if(list==null){
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

                    if(list==null){
                        textViewcomments.setText("no comments till now");
                    }

                    else {
                        textViewcomments.setText("");
                        for (int i = 0; i < list.size(); i++) {

                            textViewcomments.append(list2.get(i).toString()+" "+list.get(i).toString() + "\n");

                        }
                    }

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
                    int index=photoid.indexOf(view.getTag().toString());
                    int x = loves.get(index);
                    if(object.getList("userslovethis") !=null && object.getList("userslovethis").contains(ParseUser.getCurrentUser().getUsername())){
                        object.put("loves",x-1);
                        loves.set(index,x-1);
                        object.getList("userslovethis").remove(ParseUser.getCurrentUser().getUsername());
                        List tempusers=object.getList("userslovethis");
                        object.remove("userslovethis");
                        object.put("userslovethis",tempusers);
                    }
                    else {
                        object.put("loves", x + 1);
                        loves.set(index, x + 1);
                        object.add("userslovethis",ParseUser.getCurrentUser().getUsername());
                    }
                    object.saveInBackground();
                    customAdapter = new CustomAdapter(othersProfileActivity.this, arrayList, info, loves, comments,photoid);
                    listView.setAdapter(customAdapter);
                }
                else{
                    Log.i("msg","error");
                }
            }
        });


    }
}
