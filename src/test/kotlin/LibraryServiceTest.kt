import model.Book
import model.User
import model.UserType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import persistence.BookRepository
import persistence.BookRepositoryImpl

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LibraryServiceTest {

    private val repository: BookRepository = BookRepositoryImpl()
    private val service: LibraryService = LibraryService(repository)

    private val initialBooksInRepo = listOf(
        Book("Bob Smith", "xxx", "000"),
        Book("Sam Smith", "xxx", "000"),
        Book("abc", "coolBook", "000"),
        Book("abc", "otherBook", "000"),
        Book("abc", "xxx", "1234"),
        Book("abc", "xxx", "4567")
    )

    private val normalUser = User("normy", UserType.NORMAL_USER)
    private val libraryOwner = User("owner", UserType.LIBRARY_OWNER)

    @BeforeEach
    fun setup() {
        repository.removeAllBooks()
        repository.addBooks(initialBooksInRepo)
    }

    @Test
    fun findBookByAuthor() {
        val authorName = "Bob Smith"

        val booksByAuthor = service.findBooksByAuthor(authorName)

        assertNotNull(booksByAuthor)
        assertTrue(booksByAuthor.size == 1)
        assertEquals(authorName, booksByAuthor.first().author)
    }

    @Test
    fun findMultipleBooksByAuthor() {
        val authorName = "abc"

        val booksByAuthor = service.findBooksByAuthor(authorName)

        assertNotNull(booksByAuthor)
        assertTrue(booksByAuthor.size == 4)
        assertEquals(authorName, booksByAuthor.first().author)
    }

    @Test
    fun findMultipleBooksByAuthorFuzzySearch() {
        val authorName = "smith"

        val booksByAuthor = service.findBooksByAuthor(authorName)

        assertNotNull(booksByAuthor)
        assertTrue(booksByAuthor.size == 2)
        assertTrue(booksByAuthor.contains(Book("Bob Smith", "xxx", "000")))
        assertTrue(booksByAuthor.contains(Book("Sam Smith", "xxx", "000")))
    }

    @Test
    fun findBookByTitle() {
        val bookTitle = "coolBook"

        val booksByTitle = service.findBooksByTitle(bookTitle)

        assertNotNull(booksByTitle)
        assertTrue(booksByTitle.size == 1)
        assertEquals(bookTitle, booksByTitle.first().title)
    }

    @Test
    fun findMultipleBooksByTitle() {
        val bookTitle = "xxx"

        val booksByTitle = service.findBooksByTitle(bookTitle)

        assertNotNull(booksByTitle)
        assertTrue(booksByTitle.size == 4)
        assertEquals(bookTitle, booksByTitle.first().title)
    }

    @Test
    fun findMultipleBooksByTitleFuzzySearch() {
        val bookTitle = "book"

        val booksByTitle = service.findBooksByTitle(bookTitle)

        assertNotNull(booksByTitle)
        assertTrue(booksByTitle.size == 2)
        assertTrue(booksByTitle.contains(Book("abc", "coolBook", "000")))
        assertTrue(booksByTitle.contains(Book("abc", "otherBook", "000")))
    }

    @Test
    fun findBookByISBN() {
        val bookISBN = "1234"

        val booksByISBN = service.findBooksByISBN(bookISBN)

        assertNotNull(booksByISBN)
        assertTrue(booksByISBN.size == 1)
        assertEquals(bookISBN, booksByISBN.first().isbn)
    }

    @Test
    fun findMultipleBooksByISBN() {
        val bookISBN = "000"

        val booksByISBN = service.findBooksByISBN(bookISBN)

        assertNotNull(booksByISBN)
        assertTrue(booksByISBN.size == 4)
        assertEquals(bookISBN, booksByISBN.first().isbn)
    }

    @Test
    fun findMultipleBooksByISBNFuzzySearch() {
        val bookISBN = "4"

        val booksByISBN = service.findBooksByISBN(bookISBN)

        assertNotNull(booksByISBN)
        assertTrue(booksByISBN.size == 2)
        assertTrue(booksByISBN.contains(Book("abc", "xxx", "1234")))
        assertTrue(booksByISBN.contains(Book("abc", "xxx", "4567")))
    }

    @Test
    fun checkoutBook() {
        val bookToCheckout = Book("Bob Smith", "xxx", "000", available = true, referenceBook = false)

        val checkoutBookNormal = service.checkoutBook(bookToCheckout, normalUser)

        assertNotNull(checkoutBookNormal)
        assertFalse(checkoutBookNormal.available)
        assertEquals("Bob Smith", checkoutBookNormal.author)
        assertEquals("xxx", checkoutBookNormal.title)
        assertEquals("000", checkoutBookNormal.isbn)
        assertFalse(checkoutBookNormal.referenceBook)

        val checkoutBookOwner = service.checkoutBook(bookToCheckout, libraryOwner)

        assertNotNull(checkoutBookOwner)
        assertFalse(checkoutBookOwner.available)
        assertEquals("Bob Smith", checkoutBookOwner.author)
        assertEquals("xxx", checkoutBookOwner.title)
        assertEquals("000", checkoutBookOwner.isbn)
        assertFalse(checkoutBookOwner.referenceBook)
    }

    @Test
    fun doNothingIfAlreadyCheckedOut() {
        val bookToCheckout = Book("Bob Smith", "xxx", "000", available = false, referenceBook = false)

        val checkoutBook = service.checkoutBook(bookToCheckout, normalUser)

        assertNotNull(checkoutBook)
        assertFalse(checkoutBook.available)
        assertEquals("Bob Smith", checkoutBook.author)
        assertEquals("xxx", checkoutBook.title)
        assertEquals("000", checkoutBook.isbn)
        assertFalse(checkoutBook.referenceBook)

        val checkoutBookOwner = service.checkoutBook(bookToCheckout, libraryOwner)

        assertNotNull(checkoutBookOwner)
        assertFalse(checkoutBookOwner.available)
        assertEquals("Bob Smith", checkoutBookOwner.author)
        assertEquals("xxx", checkoutBookOwner.title)
        assertEquals("000", checkoutBookOwner.isbn)
        assertFalse(checkoutBookOwner.referenceBook)
    }

    @Test
    fun normalUserCannotCheckoutReferenceBook() {
        val bookToCheckout = Book("Bob Smith", "xxx", "000", available = true, referenceBook = true)

        val checkoutBook = service.checkoutBook(bookToCheckout, normalUser)

        assertNotNull(checkoutBook)
        assertTrue(checkoutBook.available)
        assertEquals("Bob Smith", checkoutBook.author)
        assertEquals("xxx", checkoutBook.title)
        assertEquals("000", checkoutBook.isbn)
        assertTrue(checkoutBook.referenceBook)
    }

    @Test
    fun ownerCanCheckoutReferenceBook() {
        val bookToCheckout = Book("Bob Smith", "xxx", "000", available = true, referenceBook = true)

        val checkoutBook = service.checkoutBook(bookToCheckout, libraryOwner)

        assertNotNull(checkoutBook)
        assertFalse(checkoutBook.available)
        assertEquals("Bob Smith", checkoutBook.author)
        assertEquals("xxx", checkoutBook.title)
        assertEquals("000", checkoutBook.isbn)
        assertTrue(checkoutBook.referenceBook)
    }

    @Test
    fun availableBooks() {
        repository.addBooks(
            listOf(
                Book("unavailableAuthor", "unavailableBookOne", "999", available = false),
                Book("unavailableAuthor", "unavailableBookTwo", "999", available = false)
            )
        )

        val availableBooks = service.availableBooks()

        assertNotNull(availableBooks)
        assertEquals(initialBooksInRepo.size, availableBooks.size)
        assertFalse(availableBooks.contains(Book("unavailableAuthor", "unavailableBookOne", "999", available = false)))
        assertFalse(availableBooks.contains(Book("unavailableAuthor", "unavailableBookTwo", "999", available = false)))
        assertTrue(availableBooks.count { book: Book -> !book.available } == 0)
    }
}