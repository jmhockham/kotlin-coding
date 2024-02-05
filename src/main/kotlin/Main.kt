import model.Book
import model.BookType
import model.User
import model.UserType
import persistence.BookRepository
import persistence.BookRepositoryImpl

fun main() {
    println("Starting library stories demo...")

    val repository: BookRepository = BookRepositoryImpl()
    val initialBooksInRepo = listOf(
        Book("Bob Smith", "xxx", "000"),
        Book("Sam Smith", "xxx", "000"),
        Book("abc", "coolBook", "000"),
        Book("abc", "otherBook", "000"),
        Book("abc", "xxx", "1234"),
        Book("abc", "xxx", "4567"),
        Book("unavailableAuthor", "unavailableBookOne", "999", available = true, type = BookType.REFERENCE_BOOK),
        Book("unavailableAuthor", "unavailableBookTwo", "999", available = true, type = BookType.REFERENCE_BOOK)
    )
    repository.addBooks(initialBooksInRepo)

    val service: LibraryService = LibraryService(repository)

    val normalUser = User("normy", UserType.NORMAL_USER)
    User("owner", UserType.LIBRARY_OWNER)

    println("\n/***** Story 1 *****/\n")

    println("Story 1: Library user can find books by author")
    println("Finding books with author name 'bob'...")
    var bookList = service.findBooksByAuthor("bob")
    println("Found ${bookList.size} books matching 'bob': $bookList")

    println("Finding books with author name 'smith'...")
    bookList = service.findBooksByAuthor("smith")
    println("Found ${bookList.size} books matching 'smith': $bookList")

    println("\n/***** Story 2 *****/\n")

    println("Story 2: Library user can find books by title")
    println("Finding books with title 'book'...")
    bookList = service.findBooksByTitle("book")
    println("Found ${bookList.size} books matching 'book': $bookList")

    println("Finding books with title 'coolBook'...")
    bookList = service.findBooksByTitle("coolBook")
    println("Found ${bookList.size} books matching 'coolBook': $bookList")

    println("\n/***** Story 3 *****/\n")

    println("Story 3: Library user can find books by ISBN")
    println("Finding books with ISBN '4'...")
    bookList = service.findBooksByISBN("4")
    println("Found ${bookList.size} books matching '4': $bookList")

    println("Finding books with ISBN '1234'...")
    bookList = service.findBooksByISBN("1234")
    println("Found ${bookList.size} books matching '1234': $bookList")

    println("\n/***** Story 4 *****/\n")

    println("Story 4: Library user can borrow/checkout books")
    val bookToCheckout = service.findBooksByAuthor("bob").first()
    println("book to checkout: $bookToCheckout")
    println("is book available: ${bookToCheckout.available}")
    val checkoutBook = service.checkoutBook(bookToCheckout, normalUser)
    println("book after checkout: $checkoutBook")
    println("is book available: ${checkoutBook.available}")
    println("book checked out/borrowed: ${!checkoutBook.available}")

    println("\n/***** Story 5 *****/\n")

    println("Story 5: Library user cannot borrow/checkout reference books, so they're always available")
    val referenceBook = service.findBooksByTitle("unavailableBookOne").first()
    println("book to checkout: $referenceBook")
    println("is book available: ${referenceBook.available}")
    val checkoutReferenceBook = service.checkoutBook(referenceBook, normalUser)
    println("book after checkout: $checkoutReferenceBook")
    println("is book available: ${checkoutReferenceBook.available}")
    println("book checked out/borrowed: ${!checkoutReferenceBook.available}")
}