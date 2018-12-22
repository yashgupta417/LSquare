package com.example.hp.lsquare;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseACL;

public class StartActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Password---PhBe5lIbTLDz
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        //password=iHMczPqlGoI7
        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("d3878c1fd45492a4a6ed1e1f9c82ec7aab425ea9")
                .clientKey("b632caab5ac7593cd18ac10d1f0bb10511c2b548")
                .server("http://3.17.151.149:80/parse/")
                .build()
        );




        //ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}

