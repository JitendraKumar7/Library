package com.library.reactivelocation.library.observables.location;

import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import com.library.reactivelocation.library.observables.BaseLocationObservableOnSubscribe;
import com.library.reactivelocation.library.observables.ObservableContext;
import com.library.reactivelocation.library.observables.ObservableFactory;

@SuppressWarnings("MissingPermission")
public class LastKnownLocationObservableOnSubscribe extends BaseLocationObservableOnSubscribe<Location> {
    public static Observable<Location> createObservable(ObservableContext ctx, ObservableFactory factory) {
        return factory.createObservable(new LastKnownLocationObservableOnSubscribe(ctx));
    }

    private LastKnownLocationObservableOnSubscribe(ObservableContext ctx) {
        super(ctx);
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, ObservableEmitter<? super Location> emitter) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        if (emitter.isDisposed()) return;
        if (location != null) {
            emitter.onNext(location);
        }
        emitter.onComplete();
    }
}
