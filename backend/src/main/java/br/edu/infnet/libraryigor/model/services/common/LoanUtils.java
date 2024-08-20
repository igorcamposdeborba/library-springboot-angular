package br.edu.infnet.libraryigor.model.services.common;
import br.edu.infnet.libraryigor.model.entities.Loan;
import br.edu.infnet.libraryigor.model.entities.dto.LoanDTO;

import java.time.LocalDate;

public class LoanUtils {

    public static boolean isOverlapping(LoanDTO loanDatabase, LoanDTO loanDTO) {
        LocalDate loanStartDate = loanDatabase.getEffectiveFrom();
        LocalDate loanEndDate = loanDatabase.getEffectiveTo();
        LocalDate newLoanStartDate = loanDTO.getEffectiveFrom();
        LocalDate newLoanEndDate = loanDTO.getEffectiveTo();

        return !(newLoanEndDate.isBefore(loanStartDate) || newLoanStartDate.isAfter(loanEndDate));
    }
}