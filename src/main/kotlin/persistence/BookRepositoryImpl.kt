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
        return books.filter { book: Book -> book.author.contains(authorName) }
    }

    override fun findBooksByTitle(bookName: String): List<Book> {
        return books.filter { book: Book -> book.title.contains(bookName) }
    }

    override fun findBooksByISBN(isbn: String): List<Book> {
        TODO("Not yet implemented")
    }

    override fun checkoutBook(book: Book): Book {
        TODO("Not yet implemented")
    }

    override fun availableBooks(): List<Book> {
        TODO("Not yet implemented")
    }
}