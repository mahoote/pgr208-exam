package no.kristiania.prg208_1_exam.models

data class ApiImage (
    val storeLink: String,
    val name: String,
    val domain: String,
    val identifier: String,
    val trackingId: String,
    val thumbnailLink: String,
    val description: String,
    val imageLink: String,
    val currentDate: String
        )