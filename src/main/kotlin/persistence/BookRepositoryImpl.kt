package persistence

import model.Book

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

    override fun checkoutBook(book: Book): Book {
        book.available = false
        return book
    }

    override fun checkinBook(book: Book): Book {
        book.available = true
        return book
    }

    override fun availableBooks(): List<Book> {
        return books.filter { book: Book -> book.available }
    }
}