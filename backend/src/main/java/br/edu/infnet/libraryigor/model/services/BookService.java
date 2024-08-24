package br.edu.infnet.libraryigor.model.services;

import br.edu.infnet.libraryigor.Constants;
import br.edu.infnet.libraryigor.model.entities.Book;
import br.edu.infnet.libraryigor.model.entities.Library;
import br.edu.infnet.libraryigor.model.entities.Loan;
import br.edu.infnet.libraryigor.model.entities.dto.BookDTO;
import br.edu.infnet.libraryigor.model.repositories.BookRepository;
import br.edu.infnet.libraryigor.model.repositories.LibraryRepository;
import br.edu.infnet.libraryigor.model.repositories.LoanRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository; // injetar instancia do repository para buscar do banco de dados via JPA
    @Autowired
    private LibraryRepository libraryRepository;
    @Autowired
    private LoanRepository loanRepository;

    public List<BookDTO> findAll(){
        List<Book> bookList = bookRepository.findAll(Sort.by("title")); // buscar no banco de dados e ordenar por nome
        // converter a lista de classe para DTO
        return bookList.stream().filter(Objects::nonNull).map((Book book) -> new BookDTO(book)).collect(Collectors.toList());
    }

    @Transactional() // Transação sempre executa esta operação no banco de dados se for 100% de sucesso.
    public BookDTO findById(Integer id) {
        // Buscar no banco de dados
        Optional<Book> bookDTO = bookRepository.findById(id);

        // Exception de validação
        Book entity = bookDTO.orElseThrow(() -> new ObjectNotFoundException(Constants.NOT_FOUND_BOOK, id));

        return new BookDTO(entity); // retornar somente dados permitidos (mapeados) pelo DTO
    }

    @Transactional
    public BookDTO insert(@Valid BookDTO bookDTO) { // @Valid: validar objeto (annotations e atributos)

        Optional<Book> book = bookRepository.findByTitle(bookDTO.getTitle());
        if (book.isPresent()) {
            throw new DataIntegrityViolationException("Já existe um livro cadastrado com esse título");
        }

        Library library = libraryRepository.findById(bookDTO.getLibraryId()).stream().findAny().get();

        // Mapear DTO para classe
        Book entity = new Book(bookDTO);
        entity.setLibrary(library);

        entity = bookRepository.save(entity); // salvar no banco de dados
        return new BookDTO(entity); // retornar o que foi salvo no banco de dados
    }

    @Transactional
    public List<BookDTO> insertAll(@Valid List<BookDTO> bookDTO) {
        List<BookDTO> savedDTOs = bookDTO.stream().map(book -> insert(book)).collect(Collectors.toList());

        return savedDTOs; // retornar o que foi salvo no banco de dados
    }

    @Transactional
    public BookDTO update(String idInput, BookDTO bookDTO) {
        // Converter String para Integer id
        Integer id = Integer.parseInt(idInput);

        Optional<Book> bookDatabase = Optional.ofNullable(bookRepository.findById(id)
                                                    .orElseThrow(() -> new ObjectNotFoundException(
                                                     Constants.NOT_FOUND_BOOK, Optional.of(bookDTO.getId()))));
        Library library = libraryRepository.findById(bookDTO.getLibraryId()).stream().findAny().get();

        // Validar se o id passado é o mesmo que está no banco de dados para evitar que o usuário altere o id
        if (bookDatabase.get().getId().equals(id)) {
            bookDTO.setId(id);
        }

        // Mapear DTO para classe
        Book entity = new Book(bookDTO);
        entity.setLibrary(library);

        // Salvar no banco de dados
        bookRepository.save(entity);

        // Retornar para a requisição o User atualizado
        return new BookDTO(entity);
    }

    @Transactional
    public void deleteById(Integer id) {
        // Deletar no banco de dados
        bookRepository.deleteById(id);
    }

    @Transactional
    public void deleteByIdList(List<Integer> booksId) {
        validateDeletion(booksId);

        // Deletar no banco de dados
        bookRepository.deleteAllById(booksId);
    }

    private List<Book> validateDeletion(List<Integer> booksId){
        List<Book> bookDatabase = getBooksByIdDatabase(booksId); // buscar livros pelo id no banco de dados

        List<Loan> allLoansOfLibrary = loanRepository.findAll(); // buscar empréstimos de livros
        List<Integer> idBooksBorrowed = allLoansOfLibrary.stream().flatMap(loan -> booksId.stream()
                                                          .filter(bookId -> loan.getBook().getId().equals(bookId) &&
                                                                  loan.getEffectiveTo().isBefore(LocalDate.now()) &&
                                                                  ! loan.getEffectiveFrom().isAfter(LocalDate.now())))
                                                          .collect(Collectors.toList());

        if (idBooksBorrowed.isEmpty()){
            return bookDatabase;
        } else {
            throw new DataIntegrityViolationException("Há um empréstimo em aberto. Não é possível alterar ou deletar o livro: " + idBooksBorrowed);
        }
    }

    private List<Book> getBooksByIdDatabase(List<Integer> booksId) {
        List<Book> books = bookRepository.findAllById(booksId);

        List<Book> bookDatabase = Optional.ofNullable(books)
                                          .orElseThrow(() -> new ObjectNotFoundException(
                                           Constants.NOT_FOUND_BOOK, Optional.of(booksId)));
        return bookDatabase;
    }
}
