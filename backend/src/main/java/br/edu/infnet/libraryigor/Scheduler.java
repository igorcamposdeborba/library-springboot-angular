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
        System.out.println("second method Thread name = "+Thread.currentThread().getName() + " time = "+ LocalDateTime.now());
        List<Loan> loans = loanRepository.findAll();
        System.out.println("loanRepository " +loans);

        for (Loan loan : loans) {
            LocalDate today = LocalDate.now();
            if (loan.getEffectiveTo().isBefore(today) && !loan.isDelivered()) {
                System.out.println("loan.getEffectiveTo() " +loan.getEffectiveTo());
                System.out.println("today " +today);
                int daysInDelay = LoanUtils.calculateDaysInDelay(loan.getEffectiveTo(), today);
                double totalPenalty = loan.getBook().getPrice() * daysInDelay;

                Optional<Users> user = userRepository.findById(loan.getLoanId().getUserId());
                System.out.println("userRepository " +user);
                if (user.get() instanceof Student) {
                    System.out.println("daysInDelay " + daysInDelay);
                    ((Student) user.get()).setPendingPenaltiesAmount(Constants.FOUR * daysInDelay);
                    System.out.println("((Student) user.get()) " + ((Student) user.get()).getPendingPenaltiesAmount());
                    userRepository.save(user.get());
                    System.out.println("user.get() " + user.get());
                }
            }
        }
    }
}
