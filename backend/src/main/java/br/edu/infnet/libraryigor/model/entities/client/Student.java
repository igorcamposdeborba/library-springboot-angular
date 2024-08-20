package br.edu.infnet.libraryigor.model.entities.client;
import br.edu.infnet.libraryigor.Constants;
import br.edu.infnet.libraryigor.model.entities.Loan;
import br.edu.infnet.libraryigor.model.entities.LoanRecord;
import br.edu.infnet.libraryigor.model.entities.dto.UsersDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "student")
public class Student extends Users {
    private Double pendingPenaltiesAmount;
    private String courseName;

    public Student(String id, String name, String email, boolean active, Double pendingPenaltiesAmount, String courseName) {
        super(id, name, email, active);
        this.pendingPenaltiesAmount = pendingPenaltiesAmount;
        this.courseName = courseName;
    }

    public Student(UsersDTO student){
        super(Objects.nonNull(student.getId()) ? student.getId().toString() : null,
              student.getName(),
              student.getEmail(),
              student.isActive());
        this.pendingPenaltiesAmount = Constants.ZERO;
        this.courseName = student.getCourseName();
//        this.pendingPenaltiesAmount = null; // todo: corrigir a lógica do processamento por fora. Talvez preencher com set via chamada para o banco
//        this.courseName = null; // todo: corrigir a lógica do processamento por fora. Talvez preencher com set via chamada para o banco
    }

    public Student() { super(); } // JPA precisa de construtor vazio público para persistir no banco de dados

    public Double getPendingPenaltiesAmount() {
        return pendingPenaltiesAmount;
    }
    public void setPendingPenaltiesAmount(Double pendingPenaltiesAmount) {
        this.pendingPenaltiesAmount = pendingPenaltiesAmount;
    }
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void changeCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "STUDENT{" +
                "id: " + this.getId() +
                ", name: "+ this.getName() +
                ", email: " + this.getEmail() +
                ", active: " + this.isActive() +
                ", pendingPenaltiesAmount:" + pendingPenaltiesAmount +
                ", courseName:'" + courseName + '\'' +
                '}';
    }
}
