package br.edu.infnet.libraryigor.model.entities.dto;


import br.edu.infnet.libraryigor.Constants;
import br.edu.infnet.libraryigor.model.entities.Loan;
import br.edu.infnet.libraryigor.model.entities.LoanRecord;
import br.edu.infnet.libraryigor.model.entities.client.Associate;
import br.edu.infnet.libraryigor.model.entities.client.Student;
import br.edu.infnet.libraryigor.model.entities.client.Users;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import org.apache.commons.lang3.RegExUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL) // ignora atributos nulos
public class UsersDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Database generate the User id") // Anotacao para Swagger
    private Integer id;
    private String type;
    private String name;
    private String email;
    private boolean active;
    private String bind;

    public UsersDTO(Users user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.active = user.isActive();

        identifySpecificAttribute(user);
    }

    private void identifySpecificAttribute(Users user) {
        if (user instanceof Student){
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

            this.bind = Constants.PENALTY + decimalFormat.format((new ObjectMapper().convertValue(user, Student.class))
                                                                .getPendingPenaltiesAmount());
            this.type = user.getClass().getSimpleName();

        } else if (user instanceof Associate){
            this.bind = Constants.DEPARTMENT + (new ObjectMapper().convertValue(user, Associate.class)).getDepartment();

            this.type = user.getClass().getSimpleName();
        }
    }

    public UsersDTO(String id, String name, String email, boolean active, Set<Loan> loans) {
        this.id = Integer.parseInt(id);
        this.name = name;
        this.email = email;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public boolean isActive() {
        return active;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public String getType() {
        return type;
    }

    public String getBind() {
        return bind;
    }
}
