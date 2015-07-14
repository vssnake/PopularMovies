package com.virtu.popularmovies.domain.executor;

import com.virtu.popularmovies.domain.interactor.UseCase;

import java.util.concurrent.Executor;

/**
 * Created by virtu on 25/06/2015.
 * {@link UseCase} out of the UI thread.
 */
public interface ThreadExecutor extends Executor {}
