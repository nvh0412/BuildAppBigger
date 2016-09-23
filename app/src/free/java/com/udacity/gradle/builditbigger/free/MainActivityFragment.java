package com.udacity.gradle.builditbigger.free;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.udacity.gradle.EndpointAsyncTask;
import com.udacity.gradle.builditbigger.R;
import com.vagabond.ajokelib.AJokeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {
  InterstitialAd mInterstitialAd;

  public MainActivityFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_main, container, false);

    mInterstitialAd = new InterstitialAd(getActivity());
    mInterstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id));

    mInterstitialAd.setAdListener(new AdListener() {
      @Override
      public void onAdClosed() {
        requestNewInterstitial();
        beginLoadJokeMessage();
      }
    });

    requestNewInterstitial();

    Button jokeBtn = (Button) root.findViewById(R.id.joke_button);
    jokeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mInterstitialAd.isLoaded()) {
          mInterstitialAd.show();
        } else {
          beginLoadJokeMessage();
        }
      }
    });

    return root;
  }

  private void beginLoadJokeMessage() {
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

  private void requestNewInterstitial() {
    AdRequest adRequest = new AdRequest.Builder()
      .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
      .build();

    mInterstitialAd.loadAd(adRequest);
  }
}
