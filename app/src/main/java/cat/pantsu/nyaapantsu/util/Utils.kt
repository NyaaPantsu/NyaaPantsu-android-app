package cat.pantsu.nyaapantsu.Util

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import cat.pantsu.nyaapantsu.R
import org.jetbrains.anko.toast

/**
 * Created by ltype on 2017/7/16.
 */
class Utils {
    companion object {
        fun download(activity: Activity, parent: View, url: String, name: String) {
            if (!mayRequestPermission(activity, parent, Manifest.permission.WRITE_EXTERNAL_STORAGE, 10)) {
                if (isExternalStorageWritable()) {
                    Log.d("download", "URL: " + url)
                    val request = DownloadManager.Request(Uri.parse(url))
                    request.setDescription("Download a torrent file")
                    request.setTitle(name + " - NyaaPantsu")
                    // in order for this if to run, you must use the android 3.2 to compile your app
                    request.allowScanningByMediaScanner()
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name + ".torrent")
                    Log.d("download", "request")
                    // get download service and enqueue file
                    val manager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    manager.enqueue(request)
                } else {
                    activity.toast(activity.getString(R.string.external_storage_not_available))
                }
            }
        }

        fun mayRequestPermission(activity: Activity, parent: View, permission: String, code: Int): Boolean {
            val c = parent.context
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return false
            }
            if (c.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                return false
            }
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                Snackbar.make(parent, R.string.permission_required, Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, { _ -> activity.requestPermissions(arrayOf(permission), code) })
                        .show()
            } else {
                activity.requestPermissions(arrayOf(permission), code)
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
    }
}