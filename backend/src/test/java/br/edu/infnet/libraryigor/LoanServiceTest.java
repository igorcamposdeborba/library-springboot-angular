package br.edu.infnet.libraryigor;

import br.edu.infnet.libraryigor.model.entities.Book;
import br.edu.infnet.libraryigor.model.entities.Loan;
import br.edu.infnet.libraryigor.model.entities.LoanRecord;
import br.edu.infnet.libraryigor.model.entities.client.Associate;
import br.edu.infnet.libraryigor.model.entities.client.Student;
import br.edu.infnet.libraryigor.model.entities.client.Users;
import br.edu.infnet.libraryigor.model.repositories.LoanRepository;
import br.edu.infnet.libraryigor.model.repositories.UserRepository;
import br.edu.infnet.libraryigor.model.services.LoanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class LoanServiceTest {

    @Autowired
    private Scheduler loanService;

    @MockBean // Mockar o repositório para simular dados de teste
    private LoanRepository loanRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testCalculateDailyPenalty_WithOverdueLoan_StudentUser() {
        Loan overdueLoan = createOverdueLoan(true); // Emprestimo em atraso para um estudante

        List<Loan> loans = new ArrayList<>();
        loans.add(overdueLoan);
        Mockito.when(loanRepository.findAll()).thenReturn(loans);

        Student user = (Student) overdueLoan.getUser();
        user.setPendingPenaltiesAmount(8.0);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        loanService.calculateDailyPenalty();

        Mockito.verify(loanRepository, Mockito.times(1)).findAll();
        Mockito.verify(userRepository, Mockito.times(1)).findById(user.getId());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);

        Assertions.assertEquals(8.0, user.getPendingPenaltiesAmount(), 0.001);
    }

    @Test
    public void testCalculateDailyPenalty_NoOverdueLoans() {
        List<Loan> noOverdueLoans = new ArrayList<>();
        noOverdueLoans.add(createNonOverdueLoan()); // Emprestimo NÃO em atraso

        Mockito.when(loanRepository.findAll()).thenReturn(noOverdueLoans);

        loanService.calculateDailyPenalty();

        Mockito.verify(loanRepository, Mockito.times(1)).findAll(); // Verifica se o findAll foi chamado 1 vez
    }

    private Loan createOverdueLoan(boolean isStudentUser) {
        // Criar um Loan simulado com dados de exemplo (em atraso e do tipo de usuário desejado)
        Loan loan = new Loan();
        loan.setEffectiveTo(LocalDate.now().minusDays(2)); // Data de devolução 2 dias atrás

        Users user;
        if (isStudentUser) {
            user = new Student();
        } else {
            user = new Associate();
        }
        user.setId(1);
        Book book = new Book();
        book.setPrice(4.0); //!todo: revisar se vai ser um preco padrao ou nao
        loan.setBook(book);
        loan.setLoanId(new LoanRecord(1, 1));
        loan.setUser(user);


        return loan;
    }

    private Loan createNonOverdueLoan() {
        Loan loan = new Loan();
        loan.setEffectiveTo(LocalDate.now().plusDays(2)); // Data de devolução 2 dias para frente
        loan.setLoanId(new LoanRecord(1, 1));

        return loan;
    }
}