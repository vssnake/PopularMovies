/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.virtu.popularmovies.presentation.view.fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.virtu.popularmovies.presentation.view.activity.ComponentActivity;


/**
 * Base {@link android.app.Fragment} class for every fragment in this application.
 */
public abstract class BaseFragment<T> extends android.support.v4.app.Fragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  /**
   * Shows a {@link android.widget.Toast} message.
   *
   * @param message An string representing a message to be shown.
   */
  protected void showToastMessage(String message) {
    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  }

  /**
   * Gets a component for dependency injection by its type.
   */
  protected <C> C getComponent(Class<C> componentType) {
    return componentType.cast(((ComponentActivity)getActivity()).getComponent());
      //return componentType.cast(((HasComponent<C>)getActivity()).getComponent());

  }


  /**
   * Get Activity
   *
   *
   * @return
   */
   T getAttachActivity() {
    return (T)getActivity();
  }
}
