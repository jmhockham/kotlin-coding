package model

import java.time.OffsetDateTime

data class Book(
    val author: String,
    val title: String,
    val isbn: String,
    var available: Boolean = true,
    val type: BookType = BookType.NORMAL_BOOK,
    var checkedOutBy: User? = null,
    var checkoutDate: OffsetDateTime? = null,
    var checkinDate: OffsetDateTime? = null,
    var overdueDate: OffsetDateTime? = null
)