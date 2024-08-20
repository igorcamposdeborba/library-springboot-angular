package br.edu.infnet.libraryigor;

import br.edu.infnet.libraryigor.config.LocalConfig;
import br.edu.infnet.libraryigor.model.entities.Book;
import br.edu.infnet.libraryigor.model.repositories.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
class LibraryIgorApplicationTests {

    @Autowired
    private LocalConfig localConfig = new LocalConfig();

    @Mock
    private BookRepository bookRepository;

    @Test
    void loadBookEntity() {
        try {
            MockitoAnnotations.openMocks(this);

            List<String> expectedBookData = Arrays.asList(
                    "1,1984,George Orwell,1949-01-01,4.0",
                    "2,Harry Potter,J. K. Rowling,2000-01-01,4.0",
                    "3,Java,Paul J. Deitel,2008-01-01,4.0"
            );

            localConfig.readBooks("src/main/resources/init/book.csv");
            Map<Integer, Book> entities = localConfig.getBookMap();
            List<Book> entitiesList = new ArrayList<>(entities.values());

            Assertions.assertEquals(3, entities.size());
            for (int i = 0; i < entities.values().size(); i++) {
                String[] bookData = expectedBookData.get(i).split(",");
                Assertions.assertEquals(Integer.parseInt(bookData[0]), entitiesList.get(i).getId());
                Assertions.assertEquals(bookData[1], entitiesList.get(i).getTitle());
                Assertions.assertEquals(bookData[2], entitiesList.get(i).getAuthor());
                Assertions.assertEquals(String.valueOf(bookData[3]), String.valueOf(entitiesList.get(i).getYearPublication()));
            }
        } catch (IOException e) {
            Assertions.fail("Failed to load the file: " + e.getMessage());
        }
    }
}
