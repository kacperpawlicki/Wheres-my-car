package com.example.wheresmycar

import android.app.Application
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Inicjalizacja MapLibre
        MapLibre.getInstance(
            this,
            null, // apiKey - null dla darmowych tile serverów
            WellKnownTileServer.MapLibre // Możesz też użyć MapTiler, Maptiler, etc.
        )
    }
}