package com.library.reactivelocation.library.observables.geofence;

import android.app.PendingIntent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;

import io.reactivex.ObservableEmitter;
import com.library.reactivelocation.library.observables.ObservableContext;
import com.library.reactivelocation.library.observables.StatusException;


class RemoveGeofenceByPendingIntentObservableOnSubscribe extends RemoveGeofenceObservableOnSubscribe<Status> {
    private final PendingIntent pendingIntent;

    RemoveGeofenceByPendingIntentObservableOnSubscribe(ObservableContext ctx, PendingIntent pendingIntent) {
        super(ctx);
        this.pendingIntent = pendingIntent;
    }

    @Override
    protected void removeGeofences(GoogleApiClient locationClient, final ObservableEmitter<? super Status> emitter) {
        LocationServices.GeofencingApi.removeGeofences(locationClient, pendingIntent)
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
