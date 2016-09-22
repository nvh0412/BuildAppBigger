package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.vagabond.ajokelib.AJokeActivity;
import com.vagabond.buildappbigger.backend.myApi.MyApi;

import java.io.IOException;


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

    AdView mAdView = (AdView) root.findViewById(R.id.adView);
    // Create an ad request. Check logcat output for the hashed device ID to
    // get test ads on a physical device. e.g.
    // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
    AdRequest adRequest = new AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .build();
    mAdView.loadAd(adRequest);

    Button jokeBtn = (Button) root.findViewById(R.id.joke_button);
    jokeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new EndpointAsyncTask().execute(new Pair<Context, String>(getActivity(), "Manfred"));
      }
    });

    return root;
  }

  public static  class EndpointAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
      if(myApiService == null) {  // Only do this once
        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
          new AndroidJsonFactory(), null)
          // options for running against local devappserver
          // - 10.0.2.2 is localhost's IP address in Android emulator
          // - turn off compression when running against local devappserver
          .setRootUrl("http://10.248.222.87:8080/_ah/api/")
          .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
            @Override
            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
              abstractGoogleClientRequest.setDisableGZipContent(true);
            }
          });
        builder.setApplicationName("BuildAppBigger Server");
        // end options for devappserver

        myApiService = builder.build();
      }

      context = params[0].first;
      String name = params[0].second;

      try {
        return myApiService.sayHi(name).execute().getData();
      } catch (IOException e) {
        return e.getMessage();
      }
    }

    @Override
    protected void onPostExecute(String s) {
      Intent intent = new Intent(context, AJokeActivity.class);
      intent.putExtra(AJokeActivity.JOKE, s);
      context.startActivity(intent);
    }
  }

}
