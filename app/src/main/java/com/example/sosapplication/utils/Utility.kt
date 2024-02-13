package com.example.sosapplication.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.sosapplication.R
import com.example.sosapplication.databinding.AddAndUpdateLayoutBinding
import com.example.sosapplication.databinding.LoadingLayoutBinding
import com.example.sosapplication.model.ApiErrorResponse
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

object Utility {

    var currentLocation: Location? = null
    var isLocationAvailable: Boolean? = false

    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.CAMERA
    )

    fun hasPermission(context: Context) = EasyPermissions.hasPermissions(context, *permissions)

    fun requestPermission(context: Activity) {
        EasyPermissions.requestPermissions(
            context,
            "This Application will not work without location and Camera permissions.",
            REQUEST_CODE,
            *permissions
        )
    }


    fun showDialog(context: Activity, message: String, onCancel: () -> Unit) {
        val dialog = MaterialAlertDialogBuilder(context)
            .setMessage(message)
            .setPositiveButton("return") { _, _ ->
                onCancel.invoke()
            }
        dialog.show()
    }

    fun showLoadingDialog(context: Activity, message: String? = null): AlertDialog {
        val view = LoadingLayoutBinding.inflate(context.layoutInflater)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(view.root)
            .setCancelable(false)
            .show()
        if (message?.isEmpty() == false) {
            view.message.text = message
        }

        return dialog
    }

    fun showUpdateDialog(
        context: Activity,
        onView: (dialogView: AddAndUpdateLayoutBinding, dial: AlertDialog) -> Unit
    ): AlertDialog {
        val binding = AddAndUpdateLayoutBinding.inflate(context.layoutInflater)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(binding.root)
            .setCancelable(false)
            .show()
        onView(binding, dialog)
        return dialog
    }

    fun showMessage(message: String, context: View) {
        Snackbar.make(context, message, Snackbar.LENGTH_LONG).show()
    }

    fun View.startMoveUpAnimation(context: Context) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.move_up)
        startAnimation(animation)
    }

    fun View.startRotateAnimation(context: Context) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.rotate)
        startAnimation(animation)
    }

    fun View.startBounceAnimation(context: Context) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.btn_bounce)
        startAnimation(animation)
    }

    @SuppressLint("MissingPermission")
    fun requestLocation(
        context: Context
    ) {
        val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(context)
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000 // 10 seconds
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    currentLocation = location
                    isLocationAvailable = true
                    Log.d(
                        "XXXX last known location",
                        "Lat: ${location.latitude} Long: ${location.longitude}"
                    )
                    fusedLocationProvider.removeLocationUpdates(this)
                }
            }
        }

        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_CODE
            )
            return
        }

        fusedLocationProvider.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

    }


    fun <T> getCallResponseState(result: Response<T>): RepositoryResponse<T> {

        return when {
            result.isSuccessful && result.body() != null -> {
                RepositoryResponse.Success(result.body() as T)
            }

            (result.code() == HttpURLConnection.HTTP_FORBIDDEN || result.code() == HttpURLConnection.HTTP_UNAUTHORIZED) -> {
                RepositoryResponse.ApiError(result.message())
            }

            else -> {

                val error = result.errorBody()?.source()?.let {
                    val moshiAdapter = Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()
                        .adapter(ApiErrorResponse::class.java)
                    moshiAdapter.fromJson(it)
                }
                RepositoryResponse.Error(error?.message.toString())
            }

        }
    }

    fun <T> exceptionHandler(exception: Exception): RepositoryResponse<T> {
        return when (exception) {
            is IOException -> {
                RepositoryResponse.Error(NETWORK_EXCEPTION)
            }

            else -> {
                RepositoryResponse.Error(exception.message.toString())
            }
        }
    }

    const val REQUEST_CODE = 1
    const val NETWORK_EXCEPTION = "No Internet connection"
    const val BASE_URL = "https://dummy.restapiexample.com/api/"
}
