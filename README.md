## Test project for kotlin code

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

- Overdue books
- Multiple copies of the same book
- Audit trail for book/user borrowing history
- Dates/times for borrowing

### Not asked for in stories, but added in
- Keeping track of who has borrowed the book

### Assumptions
- There are only two types of users: library user, and library owner
- The library owner is a superuser/admin; if a user can do something, the owner can do it. 
- Normal library users cannot check out reference books, but the owner can (it's their library after all)
- "Borrowing" implies checking a book in and out (two separate actions)

### Technical Notes/Reasoning
- **I haven't used mocks**: Mostly because it felt unnecessary for a small exercise like this; in the real world (especially with code that's
a bit involved) I'd write some mocks to force a particular set of return values. Unless you're dependency injecting
some test logic etc etc
- **There's an interface for the backend/repository**: As we don't have any proper data persistence here, the repo is 
just using a collection. If/when we wanted to switch this out for something else (eg a database), we should be able to 
do so fairly painlessly, as the logic is built around the interface contracts
- **Only one service, and one test class**: This is mostly because there isn't a lot of complexity or domain logic, 
which would necessitate other services + test classes
- **No date/time for book checkin/checkout**: Any date/times would presumably be useful for seeing how often a given 
book was borrowed, and for that you want to see the whole history, not just one datetime. This version would have been a step-up in complexity. As it wasn't asked for, I elected to keep it out.
- **Permissions check is on the User class, not a separate service**: As there wasn't much to do regarding user 
permissions, I've kept it simple, and just had the data class check things. It's probably not what a data class should
be doing, but pulling that logic out into something else would result in very anemic looking bit of code; hence, it 
lives in the User model for now. 
- **No usage of exceptions**: Exceptions are supposed to be for "exceptional" behaviour (usually things going wrong); 
for a lot of the behaviour outside the happy path, it doesn't seem to be exceptional - maybe more like validation 
issues. Which would ideally be caught/addressed in some kind of UI, rather that in our business logic.
- **Main class demos stories**: Whilst we're technically just repeating ourselves - the tests cover more behaviour - 
it seemed useful to have a quick way for other people to sanity check the stories were done (rather than go hunting for
the test classes).