package br.edu.infnet.libraryigor.unitary;

import br.edu.infnet.libraryigor.config.LocalConfig;
import br.edu.infnet.libraryigor.controller.UsersController;
import br.edu.infnet.libraryigor.model.entities.Library;
import br.edu.infnet.libraryigor.model.entities.client.Student;
import br.edu.infnet.libraryigor.model.entities.client.Users;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class) // setar contexto de testes para usar funcionalidades do spring boot
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // inicializar banco de dados
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerUnitTest {

    Library mockedLibrary = Mockito.mock(Library.class);

    @Mock // mockar o service
    private UsersService usersService;

    @InjectMocks // mockar as dependencias para não instanciar manualmente
    private UsersController usersController;

    @MockBean // mockar a configuração do bean para mockar o acesos ao banco de dados
    private UserRepository userRepository;

//    @MockBean
//    private LocalConfig localConfig;
//
//    @MockBean
//    private Library library;


    String userEmail;
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
    void findUserByEmail_WhenControllerIsCalledWithUserEmail_ThenReturnUserData(){
        // GIVEN
        initializeUserStudent();

        // Mockito: mockar library e setar id ao usuário
        Library mockedLibrary = Mockito.mock(Library.class);
        Mockito.when(mockedLibrary.getId()).thenReturn(libraryIdExpected);
        user.setLibrary(mockedLibrary);

        UserEmailRequest userEmailDTO = new UserEmailRequest(userEmail);
        // mockito: mockar banco de dados
        Mockito.when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        // mockito: mockar service
        Mockito.when(usersService.findByEmail(userEmail)).thenReturn(userDTO);

        // WHEN - mockito: mockar a resposta do controller (este é o teste)
        ResponseEntity<UsersDTO> response = usersController.findByEmail(userEmailDTO);


        // THEN
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(userEmail, response.getBody().getEmail());
        Assertions.assertEquals(idExpected, response.getBody().getId());
        Assertions.assertEquals(typeExpected, response.getBody().getType());
        Assertions.assertEquals(nameExpected, response.getBody().getName());
        Assertions.assertEquals(bindExpected, response.getBody().getBind());
    }

    private void initializeUserStudent(){
        userEmail = "igor@hotmail.com";
        idExpected = 1;
        typeExpected = "Student";
        nameExpected = "Igor";
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
        userRepository.save(user);

        userDTO = new UsersDTO(user); // possível null no library dentro de UsersDTO
    }
}
