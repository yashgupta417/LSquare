package com.example.hp.lsquare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class profileActivity extends AppCompatActivity {
    ImageView imageView;
    ListView listView;
    CustomAdapter customAdapter;
    ArrayList<Bitmap> arrayList=new ArrayList<Bitmap>();
    ArrayList<String> info=new ArrayList<String>();
    ArrayList<Integer> loves=new ArrayList<Integer>();
    ArrayList<String> comments=new ArrayList<String>();
    ArrayList<String> photoid=new ArrayList<String>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri=data.getData();
        if( resultCode==RESULT_OK && data!=null){

            Bitmap bitmap= null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);


                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] bytes=stream.toByteArray();
                ParseFile file=new ParseFile("profileimage.png",bytes);
                if(requestCode==1){
                    ParseUser.getCurrentUser().put("profileimage",file);
                    imageView.setImageBitmap(bitmap);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(profileActivity.this, "Profile picture Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                else if(requestCode==2){
                    ParseObject object=new ParseObject("Pictures");
                    object.put("uploadedimg",file);
                    object.put("username",ParseUser.getCurrentUser().getUsername());
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(profileActivity.this, "Photo uploaded successfully", Toast.LENGTH_SHORT).show();

                        }
                    });

                }




            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void newpic(View view){
        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }
    public void uploadimg(View view){
        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageView=(ImageView)findViewById(R.id.profilepic);
        TextView textView=(TextView) findViewById(R.id.handle);
        textView.setText(ParseUser.getCurrentUser().getUsername());

        ParseQuery<ParseObject> query3=new ParseQuery<ParseObject>("followers");
        query3.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query3.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for(ParseObject object:objects){
                    TextView followersNumber=(TextView)findViewById(R.id.followersNumber);
                    TextView followingsNumber=(TextView)findViewById(R.id.followingsNumber);
                    List fer=object.getList("follower");
                    List fing=object.getList("following");
                    if(fer!=null) {
                        followersNumber.setText(Integer.toString(fer.size()));
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




        final ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Pictures");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        arrayList.clear();
         info.clear();
         loves.clear();
         comments.clear();
         photoid.clear();
        listView=(ListView)findViewById(R.id.listView);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0) {

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

                                    info.add(ParseUser.getCurrentUser().getUsername());
                                    customAdapter = new CustomAdapter(profileActivity.this, arrayList, info, loves, comments,photoid);
                                    listView.setAdapter(customAdapter);

                                }
                            });
                        }

                    }


                    ParseFile file=(ParseFile)ParseUser.getCurrentUser().get("profileimage");

                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null && data != null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    imageView.setImageBitmap(bitmap);

                                } else {
                                    imageView.setImageResource(R.drawable.user);
                                }
                                ProgressBar progressBar = (ProgressBar) findViewById(R.id.load);
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                }
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
                        customAdapter = new CustomAdapter(profileActivity.this, arrayList, info, loves, comments,photoid);
                        listView.setAdapter(customAdapter);
                    }
                    else{
                        Log.i("msg","error");
                    }
                }
            });


    }
}
