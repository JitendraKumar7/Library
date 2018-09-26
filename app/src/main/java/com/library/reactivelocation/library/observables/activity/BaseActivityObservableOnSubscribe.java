package com.library.reactivelocation.library.observables.activity;

import com.google.android.gms.location.ActivityRecognition;

import com.library.reactivelocation.library.observables.BaseObservableOnSubscribe;
import com.library.reactivelocation.library.observables.ObservableContext;

abstract class BaseActivityObservableOnSubscribe<T> extends BaseObservableOnSubscribe<T> {
    BaseActivityObservableOnSubscribe(ObservableContext ctx) {
        super(ctx, ActivityRecognition.API);
    }
}
