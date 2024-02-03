import model.Book
import model.User
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

    fun checkoutBook(book: Book, user: User): Book {
        if (!user.canCheckoutBook(book)) {
            println("Checkout failed: user permissions check failed")
        } else if (!book.available) {
            println("Checkout failed: book already checked out")
        } else {
            return bookRepository.checkoutBook(book)
        }
        return book
    }

    fun availableBooks(): List<Book> {
        return bookRepository.availableBooks()
    }
}