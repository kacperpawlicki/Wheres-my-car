package com.example.wheresmycar.ui.main_screen.components

import android.Manifest
import android.R
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.modes.CameraMode
import org.maplibre.android.location.modes.RenderMode
import org.maplibre.android.maps.MapView
import org.maplibre.android.style.layers.PropertyFactory.*
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Map(
    modifier: Modifier = Modifier,
    onMapReady: (org.maplibre.android.maps.MapLibreMap) -> Unit = {}
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val mapView = remember {
        MapView(context).apply { onCreate(Bundle()) }
    }

    val mockMarkers = listOf(
        LatLng(52.2297, 21.0122), // Warszawa
        LatLng(50.0647, 19.9450), // Kraków
        LatLng(51.1079, 17.0385)  // Wrocław
    )

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { mapView },
        update = { view ->
            view.getMapAsync { mapLibreMap ->
                onMapReady(mapLibreMap)

                mapLibreMap.setStyle("https://tiles.openfreemap.org/styles/liberty") { style ->

                    if (locationPermissions.allPermissionsGranted) {
                        try {
                            val locationComponent = mapLibreMap.locationComponent
                            locationComponent.activateLocationComponent(
                                LocationComponentActivationOptions.builder(context, style)
                                    .useDefaultLocationEngine(true)
                                    .build()
                            )
                            locationComponent.isLocationComponentEnabled = true
                            locationComponent.renderMode = RenderMode.COMPASS
                            locationComponent.cameraMode = CameraMode.TRACKING

                            locationComponent.lastKnownLocation?.let { location ->
                                val userLocation = LatLng(location.latitude, location.longitude)
                                val cameraPosition = CameraPosition.Builder()
                                    .target(userLocation)
                                    .zoom(16.0)
                                    .build()
                                mapLibreMap.cameraPosition = cameraPosition
                            }

                            Log.d("MapLocation", "Location component enabled")
                        } catch (e: SecurityException) {
                            Log.e("MapLocation", "No location permission: ${e.message}")
                        }
                    }

                    val features = mockMarkers.map {
                        Feature.fromGeometry(Point.fromLngLat(it.longitude, it.latitude))
                    }
                    val featureCollection = FeatureCollection.fromFeatures(features)

                    val sourceId = "car-markers-source"
                    val layerId = "car-markers-layer"
                    val iconId = "car-marker-icon"

                    val markerBitmap = BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.ic_menu_compass
                    )

                    if (markerBitmap != null) {
                        style.addImage(iconId, markerBitmap)
                    }

                    val geoJsonSource = GeoJsonSource(sourceId, featureCollection)
                    style.addSource(geoJsonSource)

                    val symbolLayer = SymbolLayer(layerId, sourceId)
                        .withProperties(
                            iconImage(iconId),
                            iconAllowOverlap(true),
                            iconIgnorePlacement(true),
                            iconSize(1.2f)
                        )
                    style.addLayer(symbolLayer)
                }

                mapLibreMap.uiSettings.isCompassEnabled = false
            }
        }


    )

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            mapView.onDestroy()
        }
    }

    DisposableEffect(Unit) {
        if (!locationPermissions.allPermissionsGranted) {
            locationPermissions.launchMultiplePermissionRequest()
        }
        onDispose { }
    }
}
