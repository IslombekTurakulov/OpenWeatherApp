package com.iuturakulov.openweatherapp.utils

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber

/**
 * @see https://stackoverflow.com/a/59663897
 */
class GpsUtils(@ApplicationContext private val context: Context) {
    private var settingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    private var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var locationSettingRequest: LocationSettingsRequest
    private var locationRequest: LocationRequest = LocationRequest.create()

    init {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10_000
        locationRequest.fastestInterval = 2_000
        val locationSettingsBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        locationSettingRequest = locationSettingsBuilder.build()
        locationSettingsBuilder.setAlwaysShow(true)
    }

    fun turnOnGps(gpsListener: OnGpsListener) {
        when {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> {
                gpsListener.gpsStatus(true)
            }
            else -> {
                settingsClient.checkLocationSettings(locationSettingRequest)
                    .addOnSuccessListener {
                        gpsListener.gpsStatus(true)
                    }.addOnFailureListener {
                        when ((it as (ApiException)).statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                                try {
                                    val rae = (it as (ResolvableApiException))
                                    (context as Activity?)?.let { it1 ->
                                        rae.startResolutionForResult(
                                            it1, 500)
                                    }

                                } catch (sie: IntentSender.SendIntentException) {
                                   Timber.i("PendingIntent unable to execute request.")
                                }
                            }

                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                val errorMessage =
                                    """Location settings are inadequate, and cannot be fixed here. Fix in Settings.""";
                                Timber.e(errorMessage);
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            }
        }
    }

    interface OnGpsListener {
        fun gpsStatus(isGPSEnabled: Boolean)
    }
}