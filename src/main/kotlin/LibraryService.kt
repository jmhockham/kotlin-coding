import model.Book
import persistence.BookRepository

class LibraryService (private val bookRepository: BookRepository) {

    fun findBookByAuthor(authorName: String): Book {
        val booksByAuthor = bookRepository.findBooksByAuthor(authorName)
        return booksByAuthor.first()
    }

    fun findBookByTitle(bookName: String): Book {
        return Book("","", "")
    }

    fun findBookByISBN(isbn: String): Book {
        return Book("","", "")
    }

    fun checkoutBook(book: Book): Book {
        return book;
    }

    fun availableBooks(): List<Book> {
        return emptyList();
    }
}