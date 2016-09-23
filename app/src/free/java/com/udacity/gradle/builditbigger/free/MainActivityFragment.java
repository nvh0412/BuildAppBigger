package com.udacity.gradle.builditbigger.free;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.udacity.gradle.builditbigger.R;
import com.vagabond.ajokelib.AJokeActivity;
import com.vagabond.buildappbigger.backend.myApi.MyApi;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {


  public MainActivityFragment() {
    // Required empty public constructor
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
        new EndpointAsyncTask().execute(getActivity());
      }
    });

    return root;
  }

  public static  class EndpointAsyncTask extends AsyncTask<Context, Void, String> {
    private static MyApi myApiService = null;

    private EndpointAsyncTaskListener mListener = null;
    private Exception mError;
    private Context context;

    @Override
    protected String doInBackground(Context... params) {
      if(myApiService == null) {  // Only do this once
        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
          new AndroidJsonFactory(), null)
          // options for running against local devappserver
          // - 10.0.2.2 is localhost's IP address in Android emulator
          // - turn off compression when running against local devappserver
          .setRootUrl("http://10.0.2.2:8080/_ah/api/")
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

      context = params[0];

      try {
        return myApiService.getJoke().execute().getData();
      } catch (IOException e) {
        mError = e;
        return e.getMessage();
      }
    }

    @Override
    protected void onPostExecute(String s) {
      if (mListener != null) {
        mListener.onComplete(s, mError);
      }
      Intent intent = new Intent(context, AJokeActivity.class);
      intent.putExtra(AJokeActivity.JOKE, s);
      context.startActivity(intent);
    }

    public interface EndpointAsyncTaskListener {
      void onComplete(String result, Exception e);
    }

    public void setmListener(EndpointAsyncTaskListener mListener) {
      this.mListener = mListener;
    }
  }

}
