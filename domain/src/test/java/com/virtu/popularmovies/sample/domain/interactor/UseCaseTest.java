package com.virtu.popularmovies.sample.domain.interactor;

import com.virtu.popularmovies.domain.executor.PostExecutionThread;
import com.virtu.popularmovies.domain.executor.ThreadExecutor;
import com.virtu.popularmovies.domain.interactor.UseCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;

/**
 * Created by unai on 20/08/15.
 */
public class UseCaseTest {

    private UseCaseTestClasss useCase;

    @Mock
    private ThreadExecutor mockThreadExecutor;
    @Mock
    private PostExecutionThread mockPostExecutionThread;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.useCase = new UseCaseTestClasss(mockThreadExecutor,mockPostExecutionThread);
    }

    @Test
    public void testBuildUseCaseObservableReturnCorrectResult(){
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        TestScheduler testScheduler = new TestScheduler();
        given(mockPostExecutionThread.getScheduler()).willReturn(testScheduler);

        useCase.execute(testSubscriber);

        assertThat(testSubscriber.getOnNextEvents().size(),is(0));
    }

    @Test
    public void testSubscriptionWhenExecutingUseCase(){
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();

        useCase.execute(testSubscriber);
        useCase.unsubscribe();

        assertThat(testSubscriber.isUnsubscribed(),is(true));
    }




    private static class UseCaseTestClasss extends UseCase {
        protected UseCaseTestClasss(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread){
            super(threadExecutor,postExecutionThread);

        }

        @Override
        public Observable buildUseCaseObservable(){
            return Observable.empty();
        }
        @Override
        public void execute(Subscriber useCaseSubscriber){
            super.execute(useCaseSubscriber);
        }

    }
}
