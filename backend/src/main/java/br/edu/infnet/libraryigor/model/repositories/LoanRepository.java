package br.edu.infnet.libraryigor.model.repositories;

import br.edu.infnet.libraryigor.model.entities.Loan;
import br.edu.infnet.libraryigor.model.entities.LoanRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
            // Repository com métodos pré-implementados que se comunicam com o banco de dados
@Repository // permite injeção de dependência para implementar os métodos de acesso ao banco de dados
public interface LoanRepository extends JpaRepository<Loan, LoanRecord> { // classe e primaryKey
}
