import model.Book
import persistence.BookRepository

class LibraryService(private val bookRepository: BookRepository) {

    fun findBooksByAuthor(authorName: String): List<Book> {
        return bookRepository.findBooksByAuthor(authorName)
    }

    fun findBooksByTitle(bookName: String): List<Book> {
        return bookRepository.findBooksByTitle(bookName)
    }

    fun findBooksByISBN(isbn: String): List<Book> {
        return bookRepository.findBooksByISBN(isbn)
    }

    fun checkoutBook(book: Book): Book {
        if (book.referenceBook) {
            println("Checkout prohibited - cannot check out reference book")
        } else if (!book.available) {
            println("Checkout prohibited - book already checked out")
        } else {
            return bookRepository.checkoutBook(book)
        }
        return book
    }

    fun availableBooks(): List<Book> {
        return bookRepository.availableBooks()
    }
}