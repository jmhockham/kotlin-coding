package model

data class Book (
    val author: String,
    val title: String,
    val isbn: String,
    val available: Boolean = true,
    val referenceBook: Boolean = false
)