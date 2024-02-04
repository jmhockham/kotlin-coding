package persistence

import model.Book
import model.User

class BookRepositoryImpl : BookRepository {

    private val books = mutableListOf<Book>()

    override fun addBooks(bookList: List<Book>): List<Book> {
        books.addAll(bookList)
        return books
    }

    override fun removeAllBooks(): List<Book> {
        books.clear()
        return books
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
        return book
    }

    override fun checkinBook(book: Book): Book {
        book.available = true
        book.checkedOutBy = null
        return book
    }

    override fun availableBooks(): List<Book> {
        return books.filter { book: Book -> book.available }
    }
}