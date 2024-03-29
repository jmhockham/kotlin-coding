package persistence

import model.Book
import model.User

interface BookRepository {
    fun findBooksByAuthor(authorName: String): List<Book>

    fun findBooksByTitle(bookName: String): List<Book>

    fun findBooksByISBN(isbn: String): List<Book>

    fun checkoutBook(book: Book, user: User): Book

    fun checkinBook(book: Book): Book

    fun availableBooks(): List<Book>

    fun addBooks(bookList: List<Book>): List<Book>

    fun removeAllBooks(): List<Book>

    fun getOverdueDaysByUser(user: User): Int
}