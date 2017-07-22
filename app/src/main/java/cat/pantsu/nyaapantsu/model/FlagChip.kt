package cat.pantsu.nyaapantsu.model

import android.graphics.drawable.Drawable
import android.net.Uri
import com.pchmn.materialchips.model.ChipInterface

/**
 * Created by akuma06 on 18/07/2017.
 */
class FlagChip(val id: String, val flag: Uri, val name: String, val code: String) : ChipInterface {
    override fun getInfo(): String {
        return code
    }

    override fun getAvatarDrawable(): Drawable? {
        return null
    }

    override fun getLabel(): String {
        return name
    }

    override fun getId(): Any {
        return id
    }

    override fun getAvatarUri(): Uri {
        return flag
    }
}