package cat.pantsu.nyaapantsu.model

import com.chibatching.kotpref.KotprefModel

/**
 * Created by akuma06 on 04/07/2017.
 */
object RecentlyPlayed : KotprefModel()  {
    var torrents by stringPref("")
}