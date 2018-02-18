package cat.pantsu.nyaapantsu.mvp.model

import com.chibatching.kotpref.KotprefModel


object UserModel : KotprefModel() {
    var id by intPref(default = 0)
    var name by stringPref(default = "れんちょん")
    var md5 by stringPref()
    var token by stringPref()
    var status by intPref(default = 0)
    var splash by booleanPref(default = false)
}