package no.kristiania.prg208_1_exam.models

import java.io.Serializable

data class ResultImage (
    val current_date: String? = null,
    val description: String? = null,
    val domain: String? = null,
    val identifier: String? = null,
    val image_link: String? = null,
    val name: String? = null,
    val store_link: String? = null,
    val thumbnail_link: String? = null,
    val tracking_id: String? = null
) : Serializable