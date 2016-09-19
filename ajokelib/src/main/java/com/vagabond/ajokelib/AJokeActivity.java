package com.vagabond.ajokelib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AJokeActivity extends AppCompatActivity {

  public static final String JOKE = "JOKE";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ajoke);

    String jokeContent = getIntent().getStringExtra(JOKE);
    if (jokeContent != null) {
      TextView jokeTextview = (TextView) findViewById(R.id.joke_content);
      jokeTextview.setText(jokeContent);
    }
  }
}
