package br.edu.infnet.libraryigor.model.repositories;

import br.edu.infnet.libraryigor.model.entities.Loan;
import br.edu.infnet.libraryigor.model.entities.LoanRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Repository com métodos pré-implementados que se comunicam com o banco de dados
@Repository // permite injeção de dependência para implementar os métodos de acesso ao banco de dados
public interface LoanRepository extends JpaRepository<Loan, LoanRecord> { // classe e primaryKey

    // JPQL para consulta personalizada no banco de dados de períodos sobrepostos de um emprestimo de livro
    @Query("SELECT l FROM Loan l WHERE l.book.id = :bookId AND l.effectiveFrom <= :effectiveTo AND l.effectiveTo >= :effectiveFrom")
    List<Loan> findBookByIdAndPeriod(
        @Param("bookId") Integer bookId,
        @Param("effectiveTo") LocalDate effectiveTo,
        @Param("effectiveFrom") LocalDate effectiveFrom);
}
