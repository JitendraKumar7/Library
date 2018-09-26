package com.library.reactivelocation.library.observables.geofence;

import android.app.PendingIntent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import com.library.reactivelocation.library.observables.BaseLocationObservableOnSubscribe;
import com.library.reactivelocation.library.observables.ObservableContext;
import com.library.reactivelocation.library.observables.ObservableFactory;


public abstract class RemoveGeofenceObservableOnSubscribe<T> extends BaseLocationObservableOnSubscribe<T> {

    public static Observable<Status> createObservable(ObservableContext ctx, ObservableFactory factory, PendingIntent pendingIntent) {
        return factory.createObservable(new RemoveGeofenceByPendingIntentObservableOnSubscribe(ctx, pendingIntent));
    }

    public static Observable<Status> createObservable(ObservableContext ctx, ObservableFactory factory, List<String> requestIds) {
        return factory.createObservable(new RemoveGeofenceRequestIdsObservableOnSubscribe(ctx, requestIds));
    }

    RemoveGeofenceObservableOnSubscribe(ObservableContext ctx) {
        super(ctx);
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final ObservableEmitter<? super T> emitter) {
        removeGeofences(apiClient, emitter);
    }

    protected abstract void removeGeofences(GoogleApiClient locationClient, ObservableEmitter<? super T> emitter);

}
