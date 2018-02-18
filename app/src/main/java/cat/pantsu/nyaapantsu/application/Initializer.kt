package cat.pantsu.nyaapantsu.application

import android.app.Application
import android.preference.PreferenceManager
import cat.pantsu.nyaapantsu.BuildConfig
import cat.pantsu.nyaapantsu.model.User
import cat.pantsu.nyaapantsu.model.Utils
import com.facebook.drawee.backends.pipeline.Fresco
import com.github.kittinunf.fuel.core.FuelManager
import net.gotev.uploadservice.UploadService
import net.gotev.uploadservice.okhttp.OkHttpStack

/**
 * Created by akuma06 on 24/06/2017.
 */
class Initializer : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        // setup the broadcast action namespace string which will
        // be used to notify upload status.
        // Gradle automatically generates proper variable as below.
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID
        // Or, you can define it manually.
        UploadService.NAMESPACE = "cat.pantsu.nyaapantsu"
        UploadService.HTTP_STACK = OkHttpStack() // a new client will be automatically created

        // Fuel Manager init
        FuelManager.instance.basePath = "https://nyaa.pantsu.cat/api"

        val keepLogin = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("keep_login_switch", true)
        if (!keepLogin) {
            User.id =0
            User.name=""
            User.token=""
            User.status=0
            User.md5 = ""
        }

        val doubleBackToExit = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("doubleBackToExit", true)
        if (doubleBackToExit) {
            Utils.doubleBackToExit = true
        }
        else if(!doubleBackToExit){
            Utils.doubleBackToExit = false
        }

    }

}