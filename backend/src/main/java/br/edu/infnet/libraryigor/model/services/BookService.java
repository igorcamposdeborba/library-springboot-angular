package br.edu.infnet.libraryigor.model.services;

import br.edu.infnet.libraryigor.Constants;
import br.edu.infnet.libraryigor.model.entities.Book;
import br.edu.infnet.libraryigor.model.entities.Library;
import br.edu.infnet.libraryigor.model.entities.dto.BookDTO;
import br.edu.infnet.libraryigor.model.repositories.BookRepository;
import br.edu.infnet.libraryigor.model.repositories.LibraryRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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

//    @Transactional
//    public List<BookDTO> insertAll(@Valid List<BookDTO> bookDTO) {
//        // Mapear DTO para classe
//        List<Book> entities = bookDTO.stream().map(book -> new Book(book)).collect(Collectors.toList());
//
//        entities = bookRepository.saveAll(entities); // salvar no banco de dados
//
//        // Mapear as entities salvas de volta para DTO
//        List<BookDTO> savedDTOs = entities.stream()
//                                            .map(entity -> new BookDTO(entity))
//                                            .collect(Collectors.toList());
//        return savedDTOs; // retornar o que foi salvo no banco de dados
//    }
    @Transactional
    public List<BookDTO> insertAll(@Valid List<BookDTO> bookDTO) {
        List<BookDTO> savedDTOs = bookDTO.stream().map(book -> insert(book)).collect(Collectors.toList());

        return savedDTOs; // retornar o que foi salvo no banco de dados
    }

    @Transactional
    public void deleteById(Integer id) {

        // Deletar no banco de dados
        bookRepository.deleteById(id);
    }
}
