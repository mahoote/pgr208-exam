package no.kristiania.prg208_1_exam.models

data class ResultImage (
    val store_link: String,
    val name: String,
    val domain: String,
    val identifier: String,
    val tracking_id: String,
    val thumbnail_link: String,
    val description: String,
    val image_link: String,
    val current_date: String
        )