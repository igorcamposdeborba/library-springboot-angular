package br.edu.infnet.libraryigor.model.entities.client;
import br.edu.infnet.libraryigor.model.entities.Loan;
import br.edu.infnet.libraryigor.model.entities.LoanRecord;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "associate")
public class Associate extends Users {
    private String department;
    private String specialty;

    public Associate(String id, String name, String email, boolean active, String department, String specialty) {
        super(id, name, email, active);
        this.department = department;
        this.specialty = specialty;
    }

    public Associate() { super(); } // JPA precisa de construtor vazio público para persistir no banco de dados

    public String getDepartment() {
        return department;
    }
    public String getSpecialty() {
        return specialty;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Override
    public String toString() {
        return  "ASSOCIATE{" +
                "id: " + this.getId() +
                ", name: "+ this.getName() +
                ", email: " + this.getEmail() +
                ", active: " + this.isActive() +
                ", department:'" + department + '\'' +
                ", specialty:'" + specialty + '\'' +
                '}';
    }
}
