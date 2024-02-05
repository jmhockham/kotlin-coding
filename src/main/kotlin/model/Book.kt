package model

data class Book (
    val author: String,
    val title: String,
    val isbn: String,
    var available: Boolean = true,
    val type: BookType = BookType.NORMAL_BOOK,
    var checkedOutBy: User? = null
)