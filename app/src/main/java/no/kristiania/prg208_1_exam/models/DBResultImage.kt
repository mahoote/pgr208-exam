package no.kristiania.prg208_1_exam.models

data class DBResultImage(
    val id: Int?,
    val storeLink: String? = null,
    val name: String? = null,
    val domain: String? = null,
    val identifier: String? = null,
    val trackingID: String? = null,
    val thumbnailLink: String? = null,
    val description: String? = null,
    val imageLink: String? = null,
    val currentDate: String? = null,
    val originalImgID: Int
)
