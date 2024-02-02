import model.Book
import persistence.BookRepository

class LibraryService (private val bookRepository: BookRepository) {

    fun findBookByAuthor(authorName: String): Book {
        return bookRepository.findBooksByAuthor(authorName).first()
    }

    fun findBookByTitle(bookName: String): Book {
        return bookRepository.findBooksByTitle(bookName).first()
    }

    fun findBookByISBN(isbn: String): Book {
        return bookRepository.findBooksByISBN(isbn).first()
    }

    fun checkoutBook(book: Book): Book {
        return book;
    }

    fun availableBooks(): List<Book> {
        return emptyList();
    }
}