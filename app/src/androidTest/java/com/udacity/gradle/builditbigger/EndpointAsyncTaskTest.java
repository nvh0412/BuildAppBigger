package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.support.test.filters.MediumTest;
import android.test.ApplicationTestCase;

import com.udacity.gradle.EndpointAsyncTask;

import java.util.concurrent.CountDownLatch;

/**
 * Created by HoaNV on 9/22/16.
 */
public class EndpointAsyncTaskTest extends ApplicationTestCase<Application> {
  CountDownLatch signal = null;
  Exception mError;
  String mResult;

  public EndpointAsyncTaskTest() {
    super(Application.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    signal = new CountDownLatch(1);
  }

  @MediumTest
  public void testAsyncTaskTest() throws InterruptedException {
    EndpointAsyncTask task = new EndpointAsyncTask();
    task.setmListener(new EndpointAsyncTask.EndpointAsyncTaskListener() {
      @Override
      public void onComplete(String result, Exception e) {
        signal.countDown();
        mError = e;
        mResult = result;
      }
    });
    task.execute(getContext());
    signal.await();

    assertNull(mError);
    assertEquals(mResult, "This is joke message: This is a joke!!!!");
  }

}
