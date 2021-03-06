package com.library.reactivelocation.library.observables.location;

import android.app.PendingIntent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import com.library.reactivelocation.library.observables.BaseLocationObservableOnSubscribe;
import com.library.reactivelocation.library.observables.ObservableContext;
import com.library.reactivelocation.library.observables.ObservableFactory;
import com.library.reactivelocation.library.observables.StatusException;


@SuppressWarnings("MissingPermission")
public class AddLocationIntentUpdatesObservableOnSubscribe extends BaseLocationObservableOnSubscribe<Status> {
    private final LocationRequest locationRequest;
    private final PendingIntent intent;

    public static Observable<Status> createObservable(ObservableContext ctx, ObservableFactory factory, LocationRequest locationRequest, PendingIntent intent) {
        return factory.createObservable(new AddLocationIntentUpdatesObservableOnSubscribe(ctx, locationRequest, intent));
    }

    private AddLocationIntentUpdatesObservableOnSubscribe(ObservableContext ctx, LocationRequest locationRequest, PendingIntent intent) {
        super(ctx);
        this.locationRequest = locationRequest;
        this.intent = intent;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final ObservableEmitter<? super Status> emitter) {
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, intent)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (emitter.isDisposed()) return;
                        if (!status.isSuccess()) {
                            emitter.onError(new StatusException(status));
                        } else {
                            emitter.onNext(status);
                            emitter.onComplete();
                        }
                    }
                });

    }
}
