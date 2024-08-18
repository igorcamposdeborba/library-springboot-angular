package br.edu.infnet.libraryigor;

import br.edu.infnet.libraryigor.config.LocalConfig;
import br.edu.infnet.libraryigor.model.entities.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@SpringBootTest
class LibraryIgorApplicationTests {

    @Test
    void loadBookEntity() {
        try {
            LocalConfig main = new LocalConfig();
            main.readBooks();
            Map<Integer, Book> entities = main.getBookMap();

            Assertions.assertEquals(3, entities.size());

            Book book1 = entities.get(0);
            Assertions.assertNotNull(book1);
            Assertions.assertEquals("1984", book1.getTitle());
            Assertions.assertEquals("George Orwell", book1.getAuthor());
            Assertions.assertEquals(LocalDate.of(1949, 1, 1), book1.getYearPublication());
            Assertions.assertEquals(4.00, book1.getPrice());

            Book book2 = entities.get(1);
            Assertions.assertEquals("Harry Potter", book2.getTitle());
            Assertions.assertEquals("J. K. Rowling", book2.getAuthor());
            Assertions.assertEquals(LocalDate.of(2000, 1, 1), book2.getYearPublication());
            Assertions.assertEquals(4.00, book2.getPrice());

        } catch (IOException e) {
            Assertions.fail("Failed to load the file: " + e.getMessage());
        }
    }
}
