package com.virtu.popularmovies.domain.interactor;

import com.virtu.popularmovies.domain.executor.PostExecutionThread;
import com.virtu.popularmovies.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by virtu on 25/06/2015.
 */
public abstract class UseCase {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    //Create subscription empty by default
    private Subscription mSubscription = Subscriptions.empty();

    protected UseCase(ThreadExecutor threadExecutor,PostExecutionThread postExecutionThread){
        this.mThreadExecutor = threadExecutor;
        this.mPostExecutionThread = postExecutionThread;
    }


    /**
     * Create a Observable object. It will be executed in the current {@Link UseCase}
     * @return
     */
    public abstract Observable buildUseCaseObservable();


    @SuppressWarnings("unchecked")
    public void execute(Subscriber UserCaseSubscriber){

        this.mSubscription = this.buildUseCaseObservable()
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.getScheduler())
                .subscribe(UserCaseSubscriber);

    }

    /**
     * Unsubscribes from current {@link rx.Subscription}.
     */
    public void unsubscribe() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
