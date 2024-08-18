package br.edu.infnet.libraryigor.model.entities.dto;


import br.edu.infnet.libraryigor.model.entities.Loan;
import br.edu.infnet.libraryigor.model.entities.LoanRecord;

import java.io.Serializable;
import java.util.Set;

public class AssociateDTO extends UsersDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String department;
    private String specialty;

    public AssociateDTO(String id, String name, String email, boolean active, Set<Loan> loans, String department, String specialty) {
        super(id, name, email, active, loans);
        this.department = department;
        this.specialty = specialty;
    }

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

}
