package com.virtu.popularmovies.presentation;

import android.app.Application;

import com.virtu.popularmovies.presentation.injection.components.ApplicationComponent;
import com.virtu.popularmovies.presentation.injection.components.DaggerApplicationComponent;
import com.virtu.popularmovies.presentation.injection.modules.ApplicationModule;


/**
 * Created by virtu on 25/06/2015.
 * Android Main APP
 */
public class AndroidApp extends Application{

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate(){
        super.onCreate();
        this.initInjectors();
    }

    private void initInjectors(){
       this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }
    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

}
