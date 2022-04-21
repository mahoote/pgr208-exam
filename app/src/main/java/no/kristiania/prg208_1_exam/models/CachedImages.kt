package no.kristiania.prg208_1_exam.models

import android.net.Uri
import java.util.*

data class CachedImages(
    val imageUri: Uri?,
    val images: List<ResultImage?>,
    var created: Date
)
