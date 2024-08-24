package br.edu.infnet.libraryigor.unitary;

import br.edu.infnet.libraryigor.controller.UsersController;
import br.edu.infnet.libraryigor.model.entities.Book;
import br.edu.infnet.libraryigor.model.entities.client.Student;
import br.edu.infnet.libraryigor.model.entities.client.Users;
import br.edu.infnet.libraryigor.model.entities.dto.BookDTO;
import br.edu.infnet.libraryigor.model.entities.dto.UserEmailRequest;
import br.edu.infnet.libraryigor.model.entities.dto.UsersDTO;
import br.edu.infnet.libraryigor.model.repositories.UserRepository;
import br.edu.infnet.libraryigor.model.services.UsersService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class) // setar contexto de testes para usar funcionalidades do spring boot
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // inicializar banco de dados
@TestPropertySource(locations = "classpath:application-test.properties") //
public class BookEntityTest {

    Integer idExpected;
    String titleExpected;
    String authorExpected;
    LocalDate yearPublicationExpected;
    double priceExpected;
    Integer libraryIdExpected;

    Book book;
    BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        Integer idExpected = null;
        String titleExpected = null;
        String authorExpected = null;
        LocalDate yearPublicationExpected = null;
        double priceExpected = 0.0;
        Integer libraryIdExpected = null;
    }

    @Test
    void mapBookDTOtoBook(){
        // GIVEN
        initializeBook();

        // WHEN
        Book book = new Book(bookDTO);

        // THEN
        Assertions.assertNotNull(book);
        Assertions.assertEquals(idExpected, book.getId());
        Assertions.assertEquals(titleExpected, book.getTitle());
        Assertions.assertEquals(authorExpected, book.getAuthor());
        Assertions.assertEquals(yearPublicationExpected, book.getYearPublication());
        Assertions.assertEquals(priceExpected, book.getPrice());
    }

    private void initializeBook(){
        idExpected = 1;
        titleExpected = "Java: como programar";
        authorExpected = "Paul Deitel";
        yearPublicationExpected = LocalDate.of(2024, 6, 1);
        priceExpected = 4.0;

        bookDTO = new BookDTO(idExpected, titleExpected, authorExpected, yearPublicationExpected.toString(), priceExpected, libraryIdExpected); // mockito: mockar pa
    }
}