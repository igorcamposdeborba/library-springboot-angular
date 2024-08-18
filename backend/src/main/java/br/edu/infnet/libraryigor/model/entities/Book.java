package br.edu.infnet.libraryigor.model.entities;

import br.edu.infnet.libraryigor.model.entities.dto.BookDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "book")
public class Book implements Serializable { // Serializable para trafegar em rede por bytes
    private static final long serialVersionUID = 1L; // versão para serializacao/deserializacao para dar match com o que está sendo trafegado

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA: Banco de dados gera id sequencial
    private Integer id;
    @NotBlank // nao permite vazio ou null
    private String title;
    @NotBlank // nao permite vazio ou null
    private String author;
    private LocalDate yearPublication;
    private double price;

    @JsonBackReference // lado gerenciado no relacionamento de duas vias. Evita recursao infinita
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id")
    private Library library;

    public Book(){}  // JPA precisa de construtor vazio público para persistir no banco de dados
    public Book(Integer id, String title, String author, String yearPublication, double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.yearPublication = LocalDate.parse(yearPublication.replaceAll("^\"|\"$","").trim());
        this.price = price;
    }
    public Book(BookDTO bookDTO) { // converter DTO para entity
//        this.id = bookDTO.getId();
        this.title = bookDTO.getTitle();
        this.author = bookDTO.getAuthor();
        this.yearPublication = LocalDate.parse(String.valueOf(bookDTO.getYearPublication()).replaceAll("^\"|\"$","").trim());
        this.price = bookDTO.getPrice();
    }

    public Integer getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public LocalDate getYearPublication() {
        return yearPublication;
    }
    public double getPrice() {
        return price;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setYearPublication(LocalDate yearPublication) {
        this.yearPublication = yearPublication;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public Library getLibrary() {
        return library;
    }
    public void setLibrary(Library library) {
        this.library = library;
    }

    @Override
    public String toString() {
        return "BOOK{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", yearPublication=" + yearPublication +
                ", price=" + price +
                '}';
    }
}
