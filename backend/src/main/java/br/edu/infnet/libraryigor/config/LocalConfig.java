package br.edu.infnet.libraryigor.config;

import br.edu.infnet.libraryigor.model.entities.Book;
import br.edu.infnet.libraryigor.model.entities.Library;
import br.edu.infnet.libraryigor.model.entities.Loan;
import br.edu.infnet.libraryigor.model.entities.LoanRecord;
import br.edu.infnet.libraryigor.model.entities.client.Associate;
import br.edu.infnet.libraryigor.model.entities.client.Student;
import br.edu.infnet.libraryigor.model.entities.client.Users;
import br.edu.infnet.libraryigor.model.repositories.BookRepository;
import br.edu.infnet.libraryigor.model.repositories.LibraryRepository;
import br.edu.infnet.libraryigor.model.repositories.LoanRepository;
import br.edu.infnet.libraryigor.model.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@Profile("test")
public class LocalConfig {
    @Autowired // injetar repository para usar os métodos para salvar no banco de dados
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    LibraryRepository libraryRepository;

    private Map<Integer, Book> bookMap = new HashMap<>();
    private Map<Integer, Users> userMap = new HashMap<>();
    private Set<LoanRecord> loansKey = new HashSet<>();
    Set<Loan> loans = new HashSet<>();
    Library library = new Library();

    @Bean // Spring gerencia metodo para ser instanciado/injetado (@Autowired) em qualquer classe
    @Order(0) // ordem de inicializacao (para garantir que mesmo que criem outros métodos futuramente, os dados do database seja inserido antes)
    @Transactional
    public Optional<?> startDB() {

        List<Loan> savedLoans = Collections.emptyList();
        List<Book> savedBooks = Collections.emptyList();
        List<Users> savedUsers = Collections.emptyList();


        try {
            // Instanciar classes
            readBooks();
            readAssociate();
            readStudent();
            readLoans();

            // Atualizar a biblioteca com todos os livros e usuários
//            library = new Library(bookMap, userMap);

            // Inserir no database
            List<Book> booksToSave = bookMap.values().stream()
                    .map(book -> bookRepository.findById(book.getId()).orElse(book))
                    .collect(Collectors.toList());
//            savedBooks = bookRepository.saveAll(booksToSave);

            List<Users> usersToSave = userMap.values().stream()
                    .map(user -> userRepository.findById(user.getId()).orElse(user))
                    .collect(Collectors.toList());
//            savedUsers = userRepository.saveAll(usersToSave);

            // atualizar biblioteca
            library = new Library(booksToSave, usersToSave);
            library.addBooks(savedBooks);
            library.addUsers(savedUsers);
//            library.setUsers(savedUsers);
//            libraryRepository.save(library);

            // atualizar livros para associar a biblioteca
            libraryRepository.save(library);

            // Associar os livros à biblioteca
            for (Book book : booksToSave) {
                book.setLibrary(library);
            }
            // Associar os users à biblioteca
            for (Users user : usersToSave) {
                user.setLibrary(library);
            }

            bookRepository.saveAll(booksToSave);

            // atualizar livros da biblioteca
//            savedBooks = bookRepository.saveAll(booksToSave);


//            savedLoans = loanRepository.saveAll(loans);
//            savedbooks = bookRepository.saveAll(bookMap.values().stream().map(book -> bookRepository.findById(book.getId()).orElse(book)).collect(Collectors.toList()));
//            savedUsers = userRepository.saveAll(userMap.values().stream().map(user -> userRepository.findById(user.getId()).orElse(user)).collect(Collectors.toList()));
//            libraryRepository.save(library);

            System.out.println(new ObjectMapper().writeValueAsString(library.toString()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.of(library);
    }
    @EventListener(ApplicationReadyEvent.class) // disparar evento apos aplicacao estiver totalemente iniciada
    public void printDB() {
        // Printar dados em tela
        try {
            Unirest.setTimeouts(3000, 3000);// espera pela conexao e resposta 3 segundos
            HttpResponse<String> bookResponse = Unirest.get("http://localhost:8080/book").asString();
            HttpResponse<String> userResponse = Unirest.get("http://localhost:8080/user").asString();
            HttpResponse<String> bookInsertResponse = Unirest.post("http://localhost:8080/book/single")
                    .header("Content-Type", "application/json")
                    .body("{\r\n    \"title\": \"O Senhor dos Anéis\",\r\n    \"author\": \"J.R.R. Tolkien\",\r\n    \"yearPublication\": \"1954-07-29\",\r\n    \"price\": 4.00,\r\n    \"libraryId\": 1\r\n}").asString();
            HttpResponse<String> loanResponse = Unirest.get("http://localhost:8080/loan").asString();

            System.out.println("BOOK findAll: " + bookResponse.getBody() + ". status " + bookResponse.getStatus());
            System.out.println("USER findAll: " + userResponse.getBody() + ". status " + userResponse.getStatus());
            System.out.println("BOOK insert: " + bookInsertResponse.getStatus());
            System.out.println("LOAN findAll: " + loanResponse.getBody() + ". status " + loanResponse.getStatus());

        } catch (UnirestException e){
            e.printStackTrace();
        }
    }
    @Transactional
    public void readBooks() throws IOException {
        String filePath = "src/main/resources/init/book.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                Integer id = Integer.parseInt(fields[0]);
                String title = fields[1];
                String author = fields[2];
                String yearPublicationStr = fields[3];
                double price = Double.parseDouble(fields[4]);

                Book book = new Book(
                        id,
                        title,
                        author,
                        yearPublicationStr,
                        price
                );
                bookMap.put(id, book);
            }
            bookRepository.saveAll(bookMap.values());
        }
    }

    @Transactional
    public void readAssociate() throws IOException {
        String filePath = "src/main/resources/init/associate.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                Integer id = Integer.parseInt(fields[0]);
                Users user = new Associate(
                        fields[0], // id
                        fields[1], // name
                        fields[2], // email
                        Boolean.parseBoolean(fields[3]), // active
                        fields[5], // department
                        fields[6]  // specialty
                );
                userMap.put(id, user);
            }
        }
    }

    @Transactional
    public void readStudent() throws IOException {
        String filePath = "src/main/resources/init/student.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                Integer id = Integer.parseInt(fields[0]);
                Users user = new Student(
                        fields[0], // id
                        fields[1], // name
                        fields[2], // email
                        Boolean.parseBoolean(fields[3]), // active
                        Double.parseDouble(fields[5]), // pendingPenaltiesAmount
                        fields[6]  // courseName
                );
                userMap.put(id, user);
            }
            userRepository.saveAll(userMap.values());
        }
    }

    @Transactional
    public void readLoans() throws IOException {
        String filePath = "src/main/resources/init/loan.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                Integer userId = Integer.parseInt(fields[0]);
                Integer bookId = Integer.parseInt(fields[1]);

                Users user = userMap.get(userId);
                Book book = bookMap.get(bookId);

                LocalDate effectiveFrom = LocalDate.parse(fields[2]);
                LocalDate effectiveTo = LocalDate.parse(fields[3]);

                Loan loan = new Loan(
                        user,
                        book,
                        effectiveFrom,
                        effectiveTo
                );
                LoanRecord loanRecord = new LoanRecord(book, user);
                loansKey.add(loanRecord);

                loanRepository.save(loan);
            }
        }

    }
    public Map<Integer, Book> getBookMap() {
        return bookMap;
    }
}