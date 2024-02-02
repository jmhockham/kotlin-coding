## Test project for creating kotlin code

### Context

I have many books which I would like to share with my community. That sounds like a book-lending library. Please write some software to help me do that.


### Stories

- As a library user, I would like to be able to find books by my favourite author, so that I know if they are available in the library.

- As a library user, I would like to be able to find books by title, so that I know if they are available in the library.

- As a library user, I would like to be able to find books by ISBN, so that I know if they are available in the library.

- As a library user, I would like to be able to borrow a book, so I can read it at home.

- As the library owner, I would like to know how many books are being borrowed, so I can see how many are outstanding.

- As a library user, I should be to prevented from borrowing reference books, so that they are always available.

### Not in scope

- Keeping track of who has borrowed the book
- Overdue books
- Multiple copies of the same book (although we'll still program defensively around this)

### Assumptions
- There are only two types of users: library user, and library owner
- The library owner is a superuser/admin; if a user can do something, the owner can do it. Ergo, we don't need to check who the 
  user is, if the story starts with "as a library user"
