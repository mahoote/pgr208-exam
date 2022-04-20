package no.kristiania.prg208_1_exam.models

data class ResultImage(
    val current_date: String,
    val description: String,
    val domain: String,
    val identifier: String,
    val image_link: String,
    val name: String,
    val store_link: String,
    val thumbnail_link: String,
    val tracking_id: String
)