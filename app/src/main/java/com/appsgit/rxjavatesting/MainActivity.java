package com.appsgit.rxjavatesting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        }

        /*
            Example 1 is simple example of RxAndroid. Observable.just()  emits/pull out single value one by one.
            observer will receive the value one by one and onComplete() will be called at the end.
        */
        public void example1(View view) {
            Observable observable = Observable.fromArray("1", "2", "3","3", "4", "5");

            observable.observeOn(Schedulers.newThread());

            observable.subscribeOn(AndroidSchedulers.mainThread());

            observable.subscribe(new Observer<String>(){
                @Override
                public void onNext(@NonNull String s) {
                    //This is going to be running in Main thread.
                    Log.d(TAG, "onNext: " + s + "\n"); //this shold print the values one by one
                }

                @Override
                public void onSubscribe(@NonNull Disposable disposable) {
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "onComplete: called.");
                }
            });
        }


        /*
        Example 2 also same as like the above. Observable.fromArray()  emits/pull out single value one by one.
        observer will receive the value one by one and onComplete() will be called at the end.
        */
        public void example2(View view) {
            Observable observable = Observable.fromArray("10", "20", "30","40", "50", "60");

            observable.observeOn(Schedulers.newThread());

            observable.subscribeOn(AndroidSchedulers.mainThread());

            observable.subscribe(new Observer<String>(){
                @Override
                public void onNext(@NonNull String s) {
                    //This is going to be running in Main thread.
                    Log.d(TAG, "onNext: " + s + "\n"); //this shold print the values one by one
                }

                @Override
                public void onSubscribe(@NonNull Disposable disposable) {
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "onComplete: called.");
                }
            });
        }

        /*
        Example 3 will show you how to create Observable.fromCallable() Callable interface.
        inside the call() method you can run time consuming tasks..
        */
        public void example3(View view) {
            Observable observable = Observable.fromCallable(new Callable() {
                @Override
                public String call() throws Exception {
                    Log.d(TAG, "call: called in the Observable object..");
                    //This method can be your background thread. you can run any
                    return "test value";
                }
            });

            observable.observeOn(Schedulers.newThread());

            observable.subscribeOn(AndroidSchedulers.mainThread());

            observable.subscribe(new Observer<String>(){
                @Override
                public void onNext(@NonNull String s) {
                    //this will be your main thread.
                    //should receive "test value" here..
                    //This is going to be running in Main thread.
                    Log.d(TAG, "onNext: " + s + "\n"); //this shold print the values one by one
                }

                @Override
                public void onSubscribe(@NonNull Disposable disposable) {
                    Log.d(TAG, "onSubscribe: called..");
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "onComplete: called.");
                }
            });
        }

        /*
        Example 4 will show you how to create Observable.range() Observable will ommit 1 to 10 values..
        inside the call() method you can run time consuming tasks..
        */
        public void example4(View view) {
            Observable<Integer> observable = Observable.range(1, 10);

            observable.observeOn(Schedulers.newThread());

            observable.subscribeOn(AndroidSchedulers.mainThread());

            observable.subscribe(new Observer<Integer>(){
                @Override
                public void onNext(@NonNull Integer value) {
                    Log.d(TAG, "Observable.range onNext: " + value + "\n"); //this shold print the values one by one
                }

                @Override
                public void onSubscribe(@NonNull Disposable disposable) {
                    Log.d(TAG, "onSubscribe: called..");
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "onComplete: called.");
                }
            });
        }



    /*
        Example 5 is simple example of RxAndroid. Observable.defer()  returns an observable .
        observer will receive the value one by one and onComplete() will be called at the end.
    */
    public void example5(View view) {

        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(1, 2, 3, 4);
            }
        });
        observable.observeOn(Schedulers.newThread());

        observable.subscribeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<Integer>(){

            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "Observable.defer onNext: value is " + integer );
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: called.");
            }
        });
    }

    /*
       Observable.interval()  is really an interesting obserable. it can emmit value in every hour/minutes/seconds.
       observer will receive the value one by one and onComplete() will be called at the end.
       the below example will emit value every single minutes..
   */
    public void example6(View view) {

        //we can use any of the commented observables.
//        Observable<Long> observable = Observable.interval(1, 1, TimeUnit.HOURS);
//        Observable<Long> observable = Observable.interval(1, 1, TimeUnit.DAYS);
//        Observable<Long> observable = Observable.interval(1, 1, TimeUnit.MILLISECONDS);

        final Observable<Long> observable = Observable.interval(1, 1, TimeUnit.SECONDS);

        observable.observeOn(Schedulers.newThread());

        observable.subscribeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<Long>(){
            Disposable disposable = null;

            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                this.disposable = disposable;
            }

            @Override
            public void onNext(@NonNull Long value) {

                Log.d(TAG, "Observable.interval onNext: value is " + value );

                //dispose observer after 10 seconds. We have set to TimeUnit.SECONDS.
                if (disposable != null && value >= 10) {
                    disposable.dispose();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: called.");
            }
        });
    }
}
