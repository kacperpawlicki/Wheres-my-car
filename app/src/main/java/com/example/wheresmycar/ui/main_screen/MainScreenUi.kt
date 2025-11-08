package com.example.wheresmycar.ui.main_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wheresmycar.ui.main_screen.components.Map
import kotlinx.coroutines.launch
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenUi(
    modifier: Modifier
) {
    var mapLibreMap by remember { mutableStateOf<org.maplibre.android.maps.MapLibreMap?>(null) }
    var compassRotation by remember { mutableFloatStateOf(0f) }
    var northDirected by remember { mutableStateOf(true) }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = modifier
    ){ innerPadding ->
        val sheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
        val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
        val coroutineScope = rememberCoroutineScope()

        BottomSheetScaffold(
            modifier = Modifier
                .padding(innerPadding)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                }
            ),
            scaffoldState = scaffoldState,
            sheetPeekHeight = 140.dp,
            sheetContainerColor = Color.White,
            sheetDragHandle = {
                Box(
                    Modifier
                        .padding(vertical = 8.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .background(Color.LightGray, RoundedCornerShape(2.dp))
                )
            },
            sheetContent = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding()
                ) {
                    Text("Moje samochody", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                    Spacer(Modifier.height(16.dp))

                    repeat(10) {
                        Text("Element #$it")
                    }
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Map(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 100.dp),
                    onMapReady = { map ->
                        mapLibreMap = map
                        map.addOnCameraMoveListener {
                            compassRotation = -map.cameraPosition.bearing.toFloat()
                            northDirected = compassRotation == 0.toFloat()
                        }
                    }
                )


                AnimatedVisibility(
                    visible = !northDirected,
                    enter = fadeIn(
                        animationSpec = tween(200)
                    ),
                    exit = fadeOut(
                        animationSpec = tween(200, 100)
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .padding(bottom = 205.dp)
                ) {
                    FloatingActionButton(
                        onClick = {
                            mapLibreMap?.let { map ->
                                val currentPosition = map.cameraPosition
                                val newPosition = CameraPosition.Builder(currentPosition)
                                    .bearing(0.0)
                                    .build()

                                map.animateCamera(
                                    CameraUpdateFactory
                                        .newCameraPosition(newPosition),
                                    300
                                )
                            }
                        },
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = "Set to north",
                            modifier = Modifier
                                .size(35.dp)
                                .rotate(compassRotation - 45)
                        )
                    }
                }




                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .padding(bottom = 140.dp),
                    onClick = {
                        mapLibreMap?.let { map ->
                            val locationComponent = map.locationComponent
                            locationComponent.lastKnownLocation?.let { location ->
                                val userLocation = LatLng(location.latitude, location.longitude)
                                val cameraPosition = CameraPosition.Builder()
                                    .target(userLocation)
                                    .zoom(16.0)
                                    .build()

                                map.animateCamera(
                                    CameraUpdateFactory
                                        .newCameraPosition(cameraPosition),
                                    1000
                                )
                            }
                        }
                    },
                    containerColor = Color.White,
                    contentColor = Color.Black
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Focus camera on my location",
                        modifier = Modifier
                            .size(25.dp)
                    )
                }
            }
        }
    }
}