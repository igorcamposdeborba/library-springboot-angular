package br.edu.infnet.libraryigor.model.entities.dto;


import br.edu.infnet.libraryigor.Constants;
import br.edu.infnet.libraryigor.model.entities.Loan;
import br.edu.infnet.libraryigor.model.entities.client.Associate;
import br.edu.infnet.libraryigor.model.entities.client.Student;
import br.edu.infnet.libraryigor.model.entities.client.Users;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

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
    private String courseName;
    private String department;
    private String specialty;
    private Integer libraryId;
    private LocalDate yearPublication;

    public UsersDTO(Users user) {
        this.id = Objects.nonNull(user.getId()) ? user.getId() : null;
        this.name = user.getName();
        this.email = user.getEmail();
        this.active = user.isActive();
        this.libraryId = Objects.nonNull(user.getLibrary()) ? user.getLibrary().getId() : null;

        identifySpecificAttribute(user);
    }

    private void identifySpecificAttribute(Users user) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        if (user instanceof Student){
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            this.bind = Constants.PENALTY + decimalFormat.format((objectMapper.convertValue(user, Student.class))
                                                                .getPendingPenaltiesAmount());
            this.type = user.getClass().getSimpleName();

        } else if (user instanceof Associate){
            this.bind = Constants.DEPARTMENT + (objectMapper.convertValue(user, Associate.class)).getDepartment();

            this.type = user.getClass().getSimpleName();
        }
    }

    public UsersDTO(String id, String name, String email, boolean active, Set<Loan> loans, Integer libraryId) {
        this.id = Objects.nonNull(id) ? Integer.parseInt(id) : null;
        this.name = name;
        this.email = email;
        this.active = active;
        this.libraryId = libraryId;
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
    public Integer getLibraryId() {
        return libraryId;
    }
}
