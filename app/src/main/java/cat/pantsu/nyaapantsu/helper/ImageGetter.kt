package cat.pantsu.nyaapantsu.helper

import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.R.attr.x
import android.app.Activity
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.Bitmap
import android.graphics.Point
import android.widget.TextView
import android.graphics.drawable.LevelListDrawable
import android.os.AsyncTask
import android.util.Log
import com.bumptech.glide.Glide


/**
 * Created by akuma06 on 13/07/2017.
 */
class ImageGetterAsyncTask(private val context: Context, private val source: String, private val levelListDrawable: LevelListDrawable) : AsyncTask<TextView, Void, Bitmap>() {
    private var t: TextView? = null
    val LOG_CAT = "TORRENT_IMG"
    override fun doInBackground(vararg params: TextView): Bitmap? {
        t = params[0]
        try {
            Log.d(LOG_CAT, "Downloading the image from: " + source)
            return Glide.with(context).asBitmap().load(source).submit().get()
        } catch (e: Exception) {
            return null
        }

    }

    override fun onPostExecute(bitmap: Bitmap) {
        try {
            val d = BitmapDrawable(context.getResources(), bitmap)
            val size = Point()
            (context as Activity).windowManager.defaultDisplay.getSize(size)
            // Lets calculate the ratio according to the screen width in px
            val multiplier = size.x / bitmap.width
            Log.d(LOG_CAT, "multiplier: " + multiplier)
            levelListDrawable.addLevel(1, 1, d)
            // Set bounds width  and height according to the bitmap resized size
            levelListDrawable.setBounds(0, 0, bitmap.width * multiplier, bitmap.height * multiplier)
            levelListDrawable.level = 1
            t!!.text = t!!.text // invalidate() doesn't work correctly...
        } catch (e: Exception) { /* Like a null bitmap, etc. */
        }

    }
}