package com.virtu.popularmovies.domain.executor;

import rx.Scheduler;

/**
 * Created by virtu on 25/06/2015.
 * Thread abstraction created to change the execution context from any thread to any other thread.
 */
public interface PostExecutionThread {
    Scheduler getScheduler();
}
