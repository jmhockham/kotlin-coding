package model

data class User(val name: String, val type: UserType) {
    fun canCheckoutBook(book: Book): Boolean {
        if (type == UserType.NORMAL_USER && book.referenceBook) {
            println("Permissions check failed: library user cannot check out reference book")
            return false
        } else {
            return true
        }
    }
}