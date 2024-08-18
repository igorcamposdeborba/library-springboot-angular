package br.edu.infnet.libraryigor.controller;

import br.edu.infnet.libraryigor.model.entities.dto.BookDTO;
import br.edu.infnet.libraryigor.model.entities.dto.LibraryDTO;
import br.edu.infnet.libraryigor.model.entities.dto.UsersDTO;
import br.edu.infnet.libraryigor.model.services.BookService;
import br.edu.infnet.libraryigor.model.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/book") // rota
@Tag(name = "Book", description = "Manage Books") // descricao no swagger
public class BookController {
    private static final String ID = "/{id}";
    @Autowired
    private BookService bookService;

    @Operation(
            description = "Get all books",
            summary = "The books are going to retrieved from Library repository",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200")
            }
    )
    @GetMapping(value = {"/", ""}, produces = "application/json")
    public ResponseEntity<List<BookDTO>> findAll(){

        List<BookDTO> bookList = bookService.findAll();

        return ResponseEntity.ok().body(bookList);
    }

    @Operation(
            description = "Get book by Id",
            summary = "Get specific book from Library repository",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @GetMapping(value = ID, produces = "application/json")
    public ResponseEntity<BookDTO> findById(@PathVariable Integer id){
        BookDTO userDTO = bookService.findById(id);

        return ResponseEntity.ok().body(userDTO);
    }

    @Operation(
            description = "Insert a new book",
            summary = "The book is going to insert into Library repository",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
                    @ApiResponse(description = "Unprocessable content", responseCode = "422")
            }
    )
    @PostMapping(value = "/single", produces = "application/json") // produces especifica o formato de saída para o Swagger
    public ResponseEntity<BookDTO> insert(@Valid @RequestBody BookDTO bookDTO){
        // Inserir pelo service no banco de dados
        BookDTO newBook = bookService.insert(bookDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newBook.getId()).toUri();

        return ResponseEntity.created(uri).build(); // retornar status created 201 com uri do objeto criado
    }

    @Operation(
            description = "Insert a list of book",
            summary = "The books are going to insert into Library repository",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
                    @ApiResponse(description = "Unprocessable content", responseCode = "422")
            }
    )
    @PostMapping(value = "/all", produces = "application/json")
    public ResponseEntity<BookDTO> insertAll(@RequestBody @Valid List<BookDTO> booksDTO){
        // Inserir pelo service no banco de dados
        List<BookDTO> newBooks = bookService.insertAll(booksDTO);

        List<URI> uris = newBooks.stream()
                                    .map(book -> ServletUriComponentsBuilder.fromCurrentRequest()
                                            .path("/{id}").buildAndExpand(book.getId()).toUri())
                                    .collect(Collectors.toList());

        return ResponseEntity.created(uris.get(0)).build(); // retornar status created 201 com uri do objeto criado
    }

    @Operation(
            description = "Delete a book by Id",
            summary = "The book is going to delete by Id from Library repository",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "204"),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
                    @ApiResponse(description = "Unprocessable content", responseCode = "422")
            }
    )
    @DeleteMapping (value = ID, produces = "application/json") // id no path da url
    public ResponseEntity<Void> delete(@PathVariable @Valid Integer id){
        bookService.deleteById(id);

        return ResponseEntity.noContent().build(); // retornar status created 201 com uri do objeto criado
    }
}
