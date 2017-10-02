package github.com.raccok.dota2androidapp.utilities

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.support.v4.content.ContextCompat
import android.widget.Toast

fun appIsMissingPermissions(context: Context): Boolean {
    val prefix = "android.permission."
    if (!appHasPermission(context, prefix + "ACCESS_NETWORK_STATE")) {
        Toast.makeText(context,
                "Application needs permission to access the current network state!",
                Toast.LENGTH_LONG).show()
        return true
    }
    if (!appHasPermission(context, prefix + "INTERNET")) {
        Toast.makeText(context,
                "Application needs permission to connect to the internet!",
                Toast.LENGTH_LONG).show()
        return true
    }
    return false
}

private fun appHasPermission(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

fun deviceIsOnline(connectivityMgr: ConnectivityManager?): Boolean {
    val netInfo = connectivityMgr?.activeNetworkInfo
    return netInfo != null && netInfo.isConnectedOrConnecting
}
