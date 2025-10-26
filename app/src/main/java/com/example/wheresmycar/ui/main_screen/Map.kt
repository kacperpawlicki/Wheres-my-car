package com.example.wheresmycar.ui.main_screen

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView


@Composable
fun Map(
    modifier: Modifier = Modifier
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            MapView(ctx).apply {
                onCreate(Bundle())

                getMapAsync { mapLibreMap ->
                    mapLibreMap.setStyle("https://tiles.openfreemap.org/styles/liberty") { style ->
                        val warsaw = LatLng(52.2297, 21.0122)

                        val cameraPosition = CameraPosition.Builder()
                            .target(warsaw)
                            .zoom(16.0)
                            .build()

                        mapLibreMap.cameraPosition = cameraPosition
                    }
                }
            }
        }
    )

    DisposableEffect(lifecycle) {
        onDispose { }
    }
}