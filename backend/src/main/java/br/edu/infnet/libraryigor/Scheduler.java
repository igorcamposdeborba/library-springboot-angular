package br.edu.infnet.libraryigor;

import br.edu.infnet.libraryigor.model.entities.Loan;
import br.edu.infnet.libraryigor.model.entities.client.Student;
import br.edu.infnet.libraryigor.model.entities.client.Users;
import br.edu.infnet.libraryigor.model.repositories.LoanRepository;
import br.edu.infnet.libraryigor.model.repositories.UserRepository;
import br.edu.infnet.libraryigor.model.services.utils.LoanUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class Scheduler {
    @Autowired
    private LoanRepository loanRepository; // injetar instancia do repository para buscar do banco de dados via JPA
    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Scheduled(cron = "0 0 3 * * ?") // Executa todos os dias as 3 da manha
    public void calculateDailyPenalty() {
        List<Loan> loans = loanRepository.findAll();

        for (Loan loan : loans) {
            LocalDate today = LocalDate.now();
            if (loan.getEffectiveTo().isBefore(today) && !loan.isDelivered()) {
                int daysInDelay = LoanUtils.calculateDaysInDelay(loan.getEffectiveTo(), today);

                Optional<Users> user = userRepository.findById(loan.getLoanId().getUserId());
                if (user.get() instanceof Student) {
                    ((Student) user.get()).setPendingPenaltiesAmount(Constants.FOUR * daysInDelay);
                    userRepository.save(user.get());
                }
            }
        }
    }
}
