package br.edu.infnet.libraryigor.model.entities;

import br.edu.infnet.libraryigor.Constants;
import br.edu.infnet.libraryigor.model.entities.client.Users;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

@Entity
public class Library implements Serializable { // Serializable para trafegar em rede por bytes
    private static final long serialVersionUID = 1L; // versão para serializacao/deserializacao para dar match com o que está sendo trafegado

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA: Banco de dados gera id sequencial
    private Integer id;
    @NotBlank // nao permite vazio ou null
    private String name;
    private String address;

    @JsonManagedReference // lado que gerancia o relacionamento de duas vias. Evita recursao infinita
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL) // cacade: aplicar tambem para as classes filhas
    private List<Book> books;                           // mappedBy: quem gerencia a chave estrangeira é Library

    @JsonManagedReference // lado que gerancia o relacionamento de duas vias. Evita recursao infinita
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL) // cacade: aplicar tambem para as classes filhas
    private List<Users> users;                          // mappedBy: quem gerencia a chave estrangeira é Library



    public Library() { // JPA precisa de construtor vazio público para persistir no banco de dados
        this.name = Constants.NAME;
        this.address = Constants.ADDRESS;
    }
    public Library(Map<Integer, Book> books, Map<Integer, Users> users) {
        this.name = Constants.NAME;
        this.address = Constants.ADDRESS;
    }
    public Library(List<Book> books, List<Users> users) {
        this.name = Constants.NAME;
        this.address = Constants.ADDRESS;
        this.books = books;
        this.users = users;
    }
    public Library(Library library){
        this.name = Constants.NAME;
        this.address = Constants.ADDRESS;
        this.books = library.getBooks();
        this.users = library.getUsers();
    }

    public Integer getId() {
        return id;
    }

    public List<Book> getBooks() {
        return books;
    }
    public List<Users> getUsers() {
        return users;
    }

    public void addBooks(List<Book> booksInput) {
        this.books.addAll(booksInput);
    }
    public void addUsers(List<Users> users) {
        this.users.addAll(users);
    }

    public void setBook(Book book) {
        this.books.add(book);
    }

    @Override
    public String toString() {
        return "LIBRARY{" +
                "id: " + id +
                ", name: " + name +
                ", address: " + address +
                ", books: " + books +
                ", users: " + users +
                '}';
    }
    // Nota: Empresa não precisa trocar o nome, endereco e id neste momento do projeto. Por isso não tem alguns set.
}
