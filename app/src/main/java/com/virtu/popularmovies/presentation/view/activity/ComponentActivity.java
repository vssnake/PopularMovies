package com.virtu.popularmovies.presentation.view.activity;

import com.virtu.popularmovies.presentation.injection.HasComponent;

/**
 * Created by virtu on 25/06/2015.
 * Generic Activity for components Injection
 */
public class ComponentActivity<C> extends BaseActivity implements HasComponent<C> {

    protected C component;

    @Override public C getComponent() { return component;}
}
