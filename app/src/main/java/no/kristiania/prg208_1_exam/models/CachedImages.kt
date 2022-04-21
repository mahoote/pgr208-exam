package no.kristiania.prg208_1_exam.models

import java.util.*

data class CachedImages(
    val images: List<ResultImage?>,
    val created: Date
)
