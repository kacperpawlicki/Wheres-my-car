package com.example.wheresmycar

import android.app.Application
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //MapLibre initialization
        MapLibre.getInstance(
            this,
            null,
            WellKnownTileServer.MapLibre
        )
    }
}