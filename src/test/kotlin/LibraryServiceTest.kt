import model.Book
import model.BookType
import model.User
import model.UserType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import persistence.BookRepository
import persistence.BookRepositoryImpl
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LibraryServiceTest {

    private val repository: BookRepository = BookRepositoryImpl()
    private val service: LibraryService = LibraryService(repository)

    //we use this collection as a quick way of getting access to the data in the repo
    //this lets our tests be atomic - we don't have to use the finder functions in the repo
    //we can also easily modify the state of the books, to simplify test setup
    private var initialBooksInRepo = mutableListOf<Book>()

    private val normalUser = User("normy", UserType.NORMAL_USER)
    private val libraryOwner = User("owner", UserType.LIBRARY_OWNER)

    @BeforeEach
    fun setup() {
        initialBooksInRepo.clear()
        repository.removeAllBooks()

        initialBooksInRepo = mutableListOf(
            Book("Bob Smith", "xxx", "000"),
            Book("Sam Smith", "xxx", "000"),
            Book("abc", "coolBook", "000"),
            Book("abc", "otherBook", "000"),
            Book("abc", "xxx", "1234", type = BookType.REFERENCE_BOOK),
            Book("abc", "xxx", "4567", type = BookType.REFERENCE_BOOK)
        )
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
        assertTrue(booksByISBN.contains(Book("abc", "xxx", "1234", type = BookType.REFERENCE_BOOK)))
        assertTrue(booksByISBN.contains(Book("abc", "xxx", "4567", type = BookType.REFERENCE_BOOK)))
    }

    @Test
    fun checkoutBook() {
        val bookToCheckout = initialBooksInRepo[0]
        val anotherBookToCheckout = initialBooksInRepo[1]

        val checkoutBookNormal = service.checkoutBook(bookToCheckout, normalUser)

        assertNotNull(checkoutBookNormal)
        assertFalse(checkoutBookNormal.available)
        assertEquals("Bob Smith", checkoutBookNormal.author)
        assertEquals("xxx", checkoutBookNormal.title)
        assertEquals("000", checkoutBookNormal.isbn)
        assertEquals(BookType.NORMAL_BOOK, checkoutBookNormal.type)
        assertEquals(normalUser, checkoutBookNormal.checkedOutBy)
        assertTrue(
            OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                .isEqual(checkoutBookNormal.checkoutDate!!.truncatedTo(ChronoUnit.MINUTES))
        )

        val checkoutBookOwner = service.checkoutBook(anotherBookToCheckout, libraryOwner)

        assertNotNull(checkoutBookOwner)
        assertFalse(checkoutBookOwner.available)
        assertEquals("Sam Smith", checkoutBookOwner.author)
        assertEquals("xxx", checkoutBookOwner.title)
        assertEquals("000", checkoutBookOwner.isbn)
        assertEquals(BookType.NORMAL_BOOK, checkoutBookOwner.type)
        assertEquals(libraryOwner, checkoutBookOwner.checkedOutBy)
        assertTrue(
            OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                .isEqual(checkoutBookOwner.checkoutDate!!.truncatedTo(ChronoUnit.MINUTES))
        )
    }

    @Test
    fun doNothingIfAlreadyCheckedOut() {
        val bookToCheckout = initialBooksInRepo[0]
        bookToCheckout.checkedOutBy = normalUser
        bookToCheckout.available = false
        val checkoutDatetime = OffsetDateTime.parse("2024-01-30T15:30:00+01:00")
        bookToCheckout.checkoutDate = checkoutDatetime

        val checkoutBook = service.checkoutBook(bookToCheckout, normalUser)

        assertNotNull(checkoutBook)
        assertFalse(checkoutBook.available)
        assertEquals("Bob Smith", checkoutBook.author)
        assertEquals("xxx", checkoutBook.title)
        assertEquals("000", checkoutBook.isbn)
        assertEquals(BookType.NORMAL_BOOK, checkoutBook.type)
        assertEquals(normalUser, checkoutBook.checkedOutBy)
        assertEquals(checkoutBook.checkoutDate, checkoutDatetime)

        val checkoutBookOwner = service.checkoutBook(bookToCheckout, libraryOwner)

        assertNotNull(checkoutBookOwner)
        assertFalse(checkoutBookOwner.available)
        assertEquals("Bob Smith", checkoutBookOwner.author)
        assertEquals("xxx", checkoutBookOwner.title)
        assertEquals("000", checkoutBookOwner.isbn)
        assertEquals(BookType.NORMAL_BOOK, checkoutBookOwner.type)
        assertEquals(normalUser, checkoutBookOwner.checkedOutBy)
        assertEquals(checkoutBookOwner.checkoutDate, checkoutDatetime)
    }

    @Test
    fun checkinBook() {
        val bookToCheckin = initialBooksInRepo[0]
        bookToCheckin.checkedOutBy = normalUser

        val checkinBook = service.checkinBook(bookToCheckin)

        assertNotNull(checkinBook)
        assertTrue(checkinBook.available)
        assertEquals("Bob Smith", checkinBook.author)
        assertEquals("xxx", checkinBook.title)
        assertEquals("000", checkinBook.isbn)
        assertEquals(BookType.NORMAL_BOOK, checkinBook.type)
        assertNull(checkinBook.checkedOutBy)
        assertTrue(
            OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                .isEqual(checkinBook.checkinDate!!.truncatedTo(ChronoUnit.MINUTES))
        )
    }

    @Test
    fun normalUserCannotCheckoutReferenceBook() {
        val bookToCheckout = initialBooksInRepo.last()

        val checkoutBook = service.checkoutBook(bookToCheckout, normalUser)

        assertNotNull(checkoutBook)
        assertTrue(checkoutBook.available)
        assertEquals("abc", checkoutBook.author)
        assertEquals("xxx", checkoutBook.title)
        assertEquals("4567", checkoutBook.isbn)
        assertEquals(BookType.REFERENCE_BOOK, checkoutBook.type)
        assertNull(checkoutBook.checkedOutBy)
        assertNull(checkoutBook.checkoutDate)
    }

    @Test
    fun ownerCanCheckoutReferenceBook() {
        val bookToCheckout = initialBooksInRepo.last()

        val checkoutBook = service.checkoutBook(bookToCheckout, libraryOwner)

        assertNotNull(checkoutBook)
        assertFalse(checkoutBook.available)
        assertEquals("abc", checkoutBook.author)
        assertEquals("xxx", checkoutBook.title)
        assertEquals("4567", checkoutBook.isbn)
        assertEquals(BookType.REFERENCE_BOOK, checkoutBook.type)
        assertEquals(libraryOwner, checkoutBook.checkedOutBy)
        assertTrue(
            OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                .isEqual(checkoutBook.checkoutDate!!.truncatedTo(ChronoUnit.MINUTES))
        )
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