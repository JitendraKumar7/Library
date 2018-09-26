package com.library.rxlocation;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;


class FusedLocationProviderFactoryImpl implements FusedLocationProviderFactory {

    @Override
    public FusedLocationProviderApi create() {
        return LocationServices.FusedLocationApi;
    }
}
