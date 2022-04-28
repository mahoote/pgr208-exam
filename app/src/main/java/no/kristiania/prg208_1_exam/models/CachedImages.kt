package no.kristiania.prg208_1_exam.models

import android.net.Uri
import java.util.*

data class CachedImages(
    var imageUri: Uri,
    val images: ArrayList<DBResultImage?>,
    var created: Date
)
