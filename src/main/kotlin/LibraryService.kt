import model.Book

class LibraryService {
    fun findBookByAuthor(authorName: String): Book {
        return Book("","", "")
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