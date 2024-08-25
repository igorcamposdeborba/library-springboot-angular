package br.edu.infnet.libraryigor.integration;

import br.edu.infnet.libraryigor.controller.UsersController;
import br.edu.infnet.libraryigor.model.entities.client.Student;
import br.edu.infnet.libraryigor.model.entities.client.Users;
import br.edu.infnet.libraryigor.model.entities.dto.UserEmailRequest;
import br.edu.infnet.libraryigor.model.entities.dto.UsersDTO;
import br.edu.infnet.libraryigor.model.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class) // setar contexto de testes para usar funcionalidades do spring boot
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // inicializar banco de dados
@TestPropertySource(locations = "classpath:application-test.properties") //chamar o application.properties do teste do banco de dados h2
public class UserControllerTest {

    @Autowired
    UsersController usersControllerIntegrationTest;
    @Autowired
    UserRepository userRepositoryIntegrationTest;

    private String userEmail;
    Integer idExpected;
    String typeExpected;
    String nameExpected;
    boolean isActiveExpected;
    String bindExpected;
    Integer libraryIdExpected;
    Users user = null;
    UsersDTO userDTO = null;

    @BeforeEach
    void setUp() {
        userEmail = null;
        idExpected = null;
        typeExpected = null;
        nameExpected = null;
        isActiveExpected = true;
        bindExpected = null;
        libraryIdExpected = null;
    }

    @Test
    void findUserByEmail(){
        initializeUserStudent();
        userRepositoryIntegrationTest.save(user);

        UserEmailRequest userEmailDTO = new UserEmailRequest(userEmail);
        ResponseEntity<UsersDTO> userResponse = usersControllerIntegrationTest.findByEmail(userEmailDTO);

        Assertions.assertNotNull(userResponse.getBody());
        Assertions.assertEquals(userEmail, userResponse.getBody().getEmail());
        Assertions.assertEquals(idExpected, userResponse.getBody().getId());
        Assertions.assertEquals(typeExpected, userResponse.getBody().getType());
        Assertions.assertEquals(nameExpected, userResponse.getBody().getName());
        Assertions.assertEquals(isActiveExpected, userResponse.getBody().isActive());
        Assertions.assertEquals(bindExpected, userResponse.getBody().getBind());
    }

    private void initializeUserStudent(){
        userEmail = "igor2@hotmail.com";
        idExpected = 1;
        typeExpected = "Student";
        nameExpected = "Igor Borba";
        isActiveExpected = true;
        bindExpected = "Multa: R$ 0,00";
        libraryIdExpected = 1;

        user = new Student();
        user.setEmail(userEmail);
        user.setId(idExpected);
        user.setName(nameExpected);
        user.setActive(isActiveExpected);
        ((Student) user).setPendingPenaltiesAmount(0.0);
        ((Student) user).setCourseName("TI");

        userDTO = new UsersDTO(user); // possível null no library dentro de UsersDTO
    }
}
