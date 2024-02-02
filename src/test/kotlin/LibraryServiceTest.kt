import model.Book
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import persistence.BookRepository
import persistence.BookRepositoryImpl

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LibraryServiceTest {

    private val repository: BookRepository = BookRepositoryImpl()
    private val service: LibraryService = LibraryService(repository)

    @BeforeAll
    fun setup() {
        repository.removeAllBooks()
        val booksToAdd = listOf(
            Book("Bob Smith", "xxx", "000"),
            Book("Sam Smith", "xxx", "000"),
            Book("abc", "coolBook", "000"),
            Book("abc", "otherBook", "000"),
            Book("abc", "xxx", "123"),
            Book("abc", "xxx", "456")
        )
        repository.addBooks(booksToAdd)
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
        val bookISBN = "123"

        val booksByISBN = service.findBooksByISBN(bookISBN)

        assertNotNull(booksByISBN)
        assertTrue(booksByISBN.size == 1)
        assertEquals(bookISBN, booksByISBN.first().isbn)
    }

    @Test
    fun checkoutBook() {
    }

    @Test
    fun availableBooks() {
    }
}