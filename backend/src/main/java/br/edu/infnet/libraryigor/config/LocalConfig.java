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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Configuration // gerencia os beans para configurar aplicação como o @bean para instanciar banco de dados
@Profile("dev") // perfil de teste para executar pelo application.properties instanciar banco de dados
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

        List<Book> savedBooks = Collections.emptyList();
        List<Users> savedUsers = Collections.emptyList();


        try {
            // Instanciar classes
            readBooks("src/main/resources/init/book.csv");
            readAssociate("src/main/resources/init/associate.csv");
            readStudent("src/main/resources/init/student.csv");
            readLoans("src/main/resources/init/loan.csv");

            // Inserir no database
            List<Book> booksToSave = bookMap.values().stream()
                    .map(book -> bookRepository.findById(book.getId()).orElse(book))
                    .collect(Collectors.toList());

            List<Users> usersToSave = userMap.values().stream()
                    .map(user -> userRepository.findById(user.getId()).orElse(user))
                    .collect(Collectors.toList());

            // atualizar biblioteca
            library = new Library(booksToSave, usersToSave);
            library.addBooks(savedBooks);
            library.addUsers(savedUsers);

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

            System.out.println(new ObjectMapper().writeValueAsString(library.toString()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.of(library);
    }

    @Transactional
    public void readBooks(String path) throws IOException {
        String filePath = path;
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
    public void readAssociate(String path) throws IOException {
        String filePath = path;
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
    public void readStudent(String path) throws IOException {
        String filePath = path;
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
    public void readLoans(String path) throws IOException {
        String filePath = path;
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