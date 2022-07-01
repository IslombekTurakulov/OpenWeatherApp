package com.iuturakulov.openweatherapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import timber.log.Timber
import java.io.IOException
import java.util.*
import javax.inject.Inject


/**
 * @see https://stackoverflow.com/a/59663897
 */
class LocationUtils @Inject constructor(@ApplicationContext private val context: Context) {
    private val fusedLocationProvider: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val locationScope = Main + Job()
    private val _address = MutableLiveData<List<Address>>()
    val address: LiveData<List<Address>>
        get() = _address

    init {
        getAddress()
    }

    @SuppressLint("MissingPermission")
    fun getAddress() {
        val locationRequest: LocationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10_000
        locationRequest.fastestInterval = 5_000
        fusedLocationProvider.lastLocation.addOnCompleteListener {
            if (it.isSuccessful) {
                val location = it.result
                location?.let {
                    CoroutineScope(locationScope).launch {
                        try {
                            val geocoder = Geocoder(context, Locale.getDefault())
                            withContext(Default) {
                                val address = geocoder.getFromLocation(
                                    location.latitude, location.longitude,
                                    1
                                )
                                _address.postValue(address)
                                locationScope.cancel()
                            }
                        } catch (exception: IOException) {
                            Timber.e(exception)
                        } catch (exception: NullPointerException) {
                            Timber.e(exception)
                        }
                    }
                }
                Looper.myLooper()?.let { it1 ->
                    LocationServices.getFusedLocationProviderClient(context)
                        .requestLocationUpdates(locationRequest, locationCallback, it1)
                }
            }
        }

    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            p0.let {
                val location = it.lastLocation
                CoroutineScope(locationScope).launch {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    withContext(IO) {
                        try {
                            val address = location?.let { it1 ->
                                geocoder.getFromLocation(
                                    it1.latitude,
                                    location.longitude, 1
                                )
                            }
                            _address.postValue(address)
                        } catch (e : IOException) {
                            Timber.d(e)
                        } catch (e : NullPointerException) {
                            Timber.d(e)
                        }
                        locationScope.cancel()
                    }
                }
            }
        }
    }
}