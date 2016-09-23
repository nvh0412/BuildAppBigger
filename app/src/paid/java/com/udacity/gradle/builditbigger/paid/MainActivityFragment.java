package com.udacity.gradle.builditbigger.paid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.udacity.gradle.EndpointAsyncTask;
import com.udacity.gradle.builditbigger.R;
import com.vagabond.ajokelib.AJokeActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

  public MainActivityFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_main, container, false);

    Button jokeBtn = (Button) root.findViewById(R.id.joke_button);
    jokeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        EndpointAsyncTask task = new EndpointAsyncTask();
        task.setmListener(new EndpointAsyncTask.EndpointAsyncTaskListener() {
          @Override
          public void onComplete(String result, Exception e) {
            Intent intent = new Intent(getActivity(), AJokeActivity.class);
            intent.putExtra(AJokeActivity.JOKE, result);
            getActivity().startActivity(intent);
          }
        });
        task.execute(getActivity());
      }
    });

    return root;
  }
}
