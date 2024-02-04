package model

data class Book (
    val author: String,
    val title: String,
    val isbn: String,
    var available: Boolean = true,
    val referenceBook: Boolean = false,
    var checkedOutBy: User? = null
)