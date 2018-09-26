package com.library.rxlocation;

import com.google.android.gms.location.FusedLocationProviderApi;


interface FusedLocationProviderFactory {

    FusedLocationProviderApi create();
}
