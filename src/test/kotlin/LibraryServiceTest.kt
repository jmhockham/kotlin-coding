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
            Book("bob", "xxx", "000"),
            Book("sam", "xxx", "000"),
            Book("abc", "coolBook", "000"),
            Book("abc", "otherBook", "000"),
            Book("abc", "xxx", "123"),
            Book("abc", "xxx", "456")
        )
        repository.addBooks(booksToAdd)
    }

    @Test
    fun findBookByAuthor() {
        val authorName = "bob"

        val (authorFromBook) = service.findBookByAuthor(authorName)
        assertEquals(authorName, authorFromBook)
    }

    @Test
    fun findBookByTitle() {
        val bookTitle = "coolBook"

        val (_ , titleFromBook) = service.findBookByTitle(bookTitle)
        assertEquals(bookTitle, titleFromBook)
    }

    @Test
    fun findBookByISBN() {
    }

    @Test
    fun checkoutBook() {
    }

    @Test
    fun availableBooks() {
    }
}