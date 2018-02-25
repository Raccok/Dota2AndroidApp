package github.com.rhacco.dotascoop.utilities

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.URLSpan
import android.widget.TextView
import github.com.rhacco.dotascoop.App
import github.com.rhacco.dotascoop.R
import xdroid.toaster.Toaster

fun checkPermissions(context: Context): Boolean {
    val prefix = "android.permission."
    if (!appHasPermission(context, prefix + "ACCESS_NETWORK_STATE")) {
        Toaster.toastLong(R.string.error_permission_network_state)
        return true
    }
    if (!appHasPermission(context, prefix + "INTERNET")) {
        Toaster.toastLong(R.string.error_permission_internet)
        return true
    }
    return false
}

private fun appHasPermission(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

fun deviceIsOnline(): Boolean {
    val connectivityMgr = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val netInfo = connectivityMgr?.activeNetworkInfo
    val isOnline = netInfo != null && netInfo.isConnectedOrConnecting
    if (!isOnline)
        Toaster.toastLong(R.string.error_no_internet)
    return isOnline
}

fun stripUnderlines(textView: TextView) {
    val s = SpannableString(textView.text)
    val spans = s.getSpans(0, s.length, URLSpan::class.java)
    for (span in spans) {
        val start = s.getSpanStart(span)
        val end = s.getSpanEnd(span)
        s.removeSpan(span)
        val spanNoUnderline = URLSpanNoUnderline(span.url)
        s.setSpan(spanNoUnderline, start, end, 0)
    }
    textView.text = s
}

private class URLSpanNoUnderline(url: String) : URLSpan(url) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}