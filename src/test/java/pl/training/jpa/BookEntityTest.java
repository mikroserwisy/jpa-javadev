package pl.training.jpa;

import pl.training.jpa.books.repository.BookEntity;

import java.util.UUID;

class BookEntityTest extends EntityTest<BookEntity> {

    @Override
    protected BookEntity initializeEntity() {
        return new BookEntity(UUID.randomUUID().toString(), "Jpa in action");
    }

}
