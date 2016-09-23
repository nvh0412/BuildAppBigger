package com.udacity.gradle;

import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.vagabond.buildappbigger.backend.myApi.MyApi;

import java.io.IOException;

/**
 * Created by HoaNV on 9/23/16.
 */
public class EndpointAsyncTask extends AsyncTask<Context, Void, String> {
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
  }

  public interface EndpointAsyncTaskListener {
    void onComplete(String result, Exception e);
  }

  public void setmListener(EndpointAsyncTaskListener mListener) {
    this.mListener = mListener;
  }
}
