package pl.training.jpa.books.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.training.jpa.commons.Identifiable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookEntity implements Identifiable<String> {

    @Id
    private String id;
    private String title;

}
