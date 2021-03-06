package com.library.reactivelocation.library.observables.geofence;

import android.app.PendingIntent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import com.library.reactivelocation.library.observables.BaseLocationObservableOnSubscribe;
import com.library.reactivelocation.library.observables.ObservableContext;
import com.library.reactivelocation.library.observables.ObservableFactory;
import com.library.reactivelocation.library.observables.StatusException;


@SuppressWarnings("MissingPermission")
public class AddGeofenceObservableOnSubscribe extends BaseLocationObservableOnSubscribe<Status> {
    private final GeofencingRequest request;
    private final PendingIntent geofenceTransitionPendingIntent;

    public static Observable<Status> createObservable(ObservableContext ctx, ObservableFactory factory, GeofencingRequest request, PendingIntent geofenceTransitionPendingIntent) {
        return factory.createObservable(new AddGeofenceObservableOnSubscribe(ctx, request, geofenceTransitionPendingIntent));
    }

    private AddGeofenceObservableOnSubscribe(ObservableContext ctx, GeofencingRequest request, PendingIntent geofenceTransitionPendingIntent) {
        super(ctx);
        this.request = request;
        this.geofenceTransitionPendingIntent = geofenceTransitionPendingIntent;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final ObservableEmitter<? super Status> emitter) {
        LocationServices.GeofencingApi.addGeofences(apiClient, request, geofenceTransitionPendingIntent)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (emitter.isDisposed()) return;
                        if (status.isSuccess()) {
                            emitter.onNext(status);
                            emitter.onComplete();

                        } else {
                            emitter.onError(new StatusException(status));
                        }
                    }
                });
    }

}
