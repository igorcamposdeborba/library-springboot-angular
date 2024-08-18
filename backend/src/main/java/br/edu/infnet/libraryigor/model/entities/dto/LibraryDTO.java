package br.edu.infnet.libraryigor.model.entities.dto;


import br.edu.infnet.libraryigor.Constants;
import br.edu.infnet.libraryigor.model.entities.Book;
import br.edu.infnet.libraryigor.model.entities.Library;
import br.edu.infnet.libraryigor.model.entities.Loan;
import br.edu.infnet.libraryigor.model.entities.client.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Database generate the Library id") // Anotacao para Swagger
    private Integer id;
    private String name;
    private String address;
    private List<Integer> booksIds;
    private List<Users> users;

    public LibraryDTO(Library library) {
        this.id = library.getId();
        this.name = Constants.NAME;
        this.address = Constants.ADDRESS;
        this.booksIds = library.getBooks().stream().map(book -> book.getId()).collect(Collectors.toList());
        this.users = library.getUsers();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Integer> getBooksIds() {
        return this.booksIds;
    }

    public void setBooks(List<Book> books) {
        this.booksIds.addAll(books.stream().map(book -> book.getId()).collect(Collectors.toList()));
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }
}
