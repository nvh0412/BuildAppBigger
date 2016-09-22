package com.udacity.gradle.builditbigger;

import android.support.test.filters.MediumTest;
import android.test.ActivityInstrumentationTestCase2;

import java.util.concurrent.CountDownLatch;

/**
 * Created by HoaNV on 9/22/16.
 */
public class EndpointAsyncTaskTest extends ActivityInstrumentationTestCase2<MainActivity> {
  CountDownLatch signal = null;
  Exception mError;
  String mResult;

  public EndpointAsyncTaskTest() {
    super(MainActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    signal = new CountDownLatch(1);
  }

  @MediumTest
  public void testAsyncTaskTest() throws InterruptedException {
    MainActivityFragment.EndpointAsyncTask task = new MainActivityFragment.EndpointAsyncTask();
    task.setmListener(new MainActivityFragment.EndpointAsyncTask.EndpointAsyncTaskListener() {
      @Override
      public void onComplete(String result, Exception e) {
        signal.countDown();
        mError = e;
        mResult = result;
      }
    });
    task.execute(getActivity());
    signal.await();

    assertNull(mError);
    assertEquals(mResult, "This is joke message: This is a joke!!!!");
  }

}
