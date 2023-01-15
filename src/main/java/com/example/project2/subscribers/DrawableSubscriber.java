package com.example.project2.subscribers;

import com.example.project2.map.Drawable;

import java.util.concurrent.Flow;

public class DrawableSubscriber implements Flow.Subscriber<Drawable>{
    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public void onNext(Drawable item) {
        System.out.println("Data has been changed: "+ item.getName());
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
    }
}
