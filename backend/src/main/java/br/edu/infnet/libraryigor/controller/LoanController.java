package br.edu.infnet.libraryigor.controller;

import br.edu.infnet.libraryigor.model.entities.LoanRecord;
import br.edu.infnet.libraryigor.model.entities.dto.LoanDTO;
import br.edu.infnet.libraryigor.model.entities.dto.LoanDeliverDTO;
import br.edu.infnet.libraryigor.model.services.LoanService;
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

@RestController
@RequestMapping(value = "/loan") // rota
@Tag(name = "Loan", description = "Manage Loan") // descricao no swagger
public class LoanController {
    private static final String ID = "/{id}";

    @Autowired
    private LoanService loanService;

    @Operation(
            description = "Get all loans",
            summary = "The loans are going to retrieved from Library repository",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200")
            }
    )
    @GetMapping(value = {"/", ""})
    public ResponseEntity<List<LoanDTO>> findAll(){

        List<LoanDTO> loanList = loanService.findAll();

        return ResponseEntity.ok().body(loanList);
    }

    @Operation(
            description = "Get Loan by Id",
            summary = "Get specific loan from Library repository",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @PostMapping(value = "/find", produces = "application/json") // POST porque vou usar o body para declarar a chave composta
    public ResponseEntity<LoanDTO> findById(@RequestBody LoanDTO loanDTO){
        LoanDTO loan = loanService.findById(loanDTO.getBookId(), loanDTO.getUserId());

        return ResponseEntity.ok().body(loan);
    }


    @Operation(
            description = "Insert a new loan",
            summary = "The loan is going to insert into Library repository",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
                    @ApiResponse(description = "Unprocessable content", responseCode = "422")
            }
    )
    @PostMapping(produces = "application/json") // produces especifica o formato de saída para o Swagger
    public ResponseEntity<LoanDTO> insert(@Valid @RequestBody LoanDTO loanDTO){
        // Inserir pelo service no banco de dados
        loanService.insert(loanDTO);

        LoanRecord loanRecordId = new LoanRecord(loanDTO.getBookId(), loanDTO.getUserId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(loanRecordId).toUri();

        return ResponseEntity.created(uri).build(); // retornar status created 201 com uri do objeto criado
    }

    @Operation(
            description = "Deliver a book",
            summary = "Deliver book loaned and register this on Library repository",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
                    @ApiResponse(description = "Unprocessable content", responseCode = "422")
            }
    )
    @PostMapping(value = "/deliver", produces = "application/json") // produces especifica o formato de saída para o Swagger
    public void deliverBook(@Valid @RequestBody LoanDeliverDTO loanDTO){
        loanService.deliverBook(loanDTO); // Devolver livro
    }
}
