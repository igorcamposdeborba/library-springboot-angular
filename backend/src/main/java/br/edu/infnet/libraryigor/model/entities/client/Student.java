package br.edu.infnet.libraryigor.model.entities.client;
import br.edu.infnet.libraryigor.Constants;
import br.edu.infnet.libraryigor.model.entities.dto.UsersDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "student")
@JsonIgnoreProperties(ignoreUnknown = true) // jackson ignora campos que ele não encontra na serializacao/deserializacao do json: serve para trocar o tipo de Student para Associate
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
