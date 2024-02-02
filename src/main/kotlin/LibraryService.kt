import model.Book
import persistence.BookRepository

class LibraryService (private val bookRepository: BookRepository) {

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
        return book;
    }

    fun availableBooks(): List<Book> {
        return emptyList();
    }
}