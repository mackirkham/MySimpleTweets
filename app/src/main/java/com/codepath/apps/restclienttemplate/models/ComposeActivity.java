package com.codepath.apps.restclienttemplate.models;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;
    private Tweet tweet;
    private final int RESULT_OK = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //max length for the tweet and initializing new tweet
                int maxLength= 140;
                EditText newTweet = findViewById(R.id.newTweet);

                LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                newTweet.setLayoutParams(params);
                newTweet.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});


                //get tweet body
                final String tweetBody = newTweet.getText().toString();

                client = TwitterApp.getRestClient(ComposeActivity.this);

                client.sendTweet(tweetBody, new JsonHttpResponseHandler() {
                    //get data
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            tweet = Tweet.fromJSON(response);
                            Intent data = new Intent();
                            //putting what was just typed and then wrapping it to send back to the timeline
                            data.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                            //activity finished
                            setResult(RESULT_OK, data);
                            finish(); //closes activity
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
            }
        });
    }
}






