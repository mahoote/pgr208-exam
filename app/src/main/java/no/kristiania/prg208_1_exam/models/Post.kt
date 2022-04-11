package no.kristiania.prg208_1_exam.models


data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String)