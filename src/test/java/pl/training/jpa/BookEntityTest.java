package pl.training.jpa;

import java.util.UUID;

class BookEntityTest extends EntityTest<BookEntity> {

    @Override
    protected BookEntity initializeEntity() {
        return new BookEntity(UUID.randomUUID().toString(), "Jpa in action");
    }

}
