package cat.pantsu.nyaapantsu.util


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Toast
import cat.pantsu.nyaapantsu.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun download(context: Activity, parent: View, url: String, name: String) {
    if (!mayRequestPermission(context, parent, Manifest.permission.WRITE_EXTERNAL_STORAGE, 10)) {
        if (isExternalStorageWritable()) {
            Log.d("download", "URL: $url")
            val request = DownloadManager.Request(Uri.parse(url))
            request.setDescription("Download a torrent file")
            request.setTitle("$name - NyaaPantsu")
            // in order for this if to run, you must use the android 3.2 to compile your app
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$name.torrent")
            Log.d("download", "request")
            // get download service and enqueue file

            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
        } else {
            Toast.makeText(context, R.string.external_storage_not_available, Toast.LENGTH_SHORT).show()
        }
    }
}


fun mayRequestPermission(context: Activity, parent: View, permission: String, code: Int): Boolean {
    val c = parent.context
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        return false
    }
    if (c.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
        return false
    }
    if (context.shouldShowRequestPermissionRationale(permission)) {
        Snackbar.make(parent, R.string.permission_required, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, { _ -> context.requestPermissions(arrayOf(permission), code) })
                .show()
    } else {
        context.requestPermissions(arrayOf(permission), code)

    }
    return true
}

fun isExternalStorageWritable(): Boolean {
    val state = Environment.getExternalStorageState()
    if (Environment.MEDIA_MOUNTED == state) {
        return true
    }
    return false
}

fun playVoice(c: Context) {
    //TODO support sukebei mode, maybe need a application context for hold player
    val player = MediaPlayer.create(c, R.raw.nyanpass)
    player.setOnCompletionListener {
        player.release()
    }
    player.start()
}


@SuppressLint("SimpleDateFormat")
fun String.formatDate(): String? {
    val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val destFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    var date: Date? = null
    try {
        date = sourceFormat.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return destFormat.format(date)
}