package persistence

import model.Book
import model.User
import java.time.OffsetDateTime
import java.time.Period

class BookRepositoryImpl : BookRepository {

    private val books = mutableListOf<Book>()
    private val usersAndOverdueDays = mutableMapOf<User, Int>()

    override fun addBooks(bookList: List<Book>): List<Book> {
        books.addAll(bookList)
        return books
    }

    override fun removeAllBooks(): List<Book> {
        books.clear()
        return books
    }

    override fun getOverdueDaysByUser(user: User): Int {
        return usersAndOverdueDays[user] ?: 0
    }

    override fun findBooksByAuthor(authorName: String): List<Book> {
        return books.filter { book: Book -> book.author.lowercase().contains(authorName.lowercase()) }
    }

    override fun findBooksByTitle(bookName: String): List<Book> {
        return books.filter { book: Book -> book.title.lowercase().contains(bookName.lowercase()) }
    }

    override fun findBooksByISBN(isbn: String): List<Book> {
        return books.filter { book: Book -> book.isbn.lowercase().contains(isbn.lowercase()) }
    }

    override fun checkoutBook(book: Book, user: User): Book {
        book.available = false
        book.checkedOutBy = user
        val checkoutDate = OffsetDateTime.now()
        book.checkoutDate = checkoutDate
        book.overdueDate = checkoutDate.plusDays(7)
        return book
    }

    override fun checkinBook(book: Book): Book {
        val timeOfCheckin = OffsetDateTime.now()

        val overdueDate = book.overdueDate ?: timeOfCheckin
        val checkoutUser = book.checkedOutBy ?: throw RuntimeException("Checked out book doesn't have a user, cannot check in")

        if(overdueDate.isBefore(timeOfCheckin)){
            val daysDifference = Period.between(overdueDate.toLocalDate(), timeOfCheckin.toLocalDate()).days
            usersAndOverdueDays[checkoutUser] = daysDifference
        }

        book.available = true
        book.checkedOutBy = null
        book.checkinDate = timeOfCheckin
        book.checkoutDate = null
        book.overdueDate = null

        return book
    }

    override fun availableBooks(): List<Book> {
        return books.filter { book: Book -> book.available }
    }
}