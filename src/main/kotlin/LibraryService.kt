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

    /**
     * Allows a user to borrow a book from the library
     * @param book - the book to be borrowed
     * @param user - the person who's checking out the book
     * @return the book we passed in as a param, after it's finished the checkout process
     */
    fun checkoutBook(book: Book, user: User): Book {
        if (!user.canCheckoutBook(book)) {
            println("Checkout failed: user permissions check failed")
        } else if (!book.available) {
            println("Checkout failed: book already checked out")
        } else {
            return bookRepository.checkoutBook(book, user)
        }
        return book
    }

    /**
     * Allows a book to be returned to the library
     * @param book - the book to be returned
     * @return the book we passed in as a param, after it's finished the checkin process
     */
    fun checkinBook(book: Book): Book {
        return bookRepository.checkinBook(book)
    }

    /**
     * Shows which books are still available (ie not borrowed by users) in the library
     * @return a list of books available for borrowing
     */
    fun availableBooks(): List<Book> {
        return bookRepository.availableBooks()
    }
}