package br.edu.infnet.libraryigor.controller;

import br.edu.infnet.libraryigor.model.entities.dto.BookDTO;
import br.edu.infnet.libraryigor.model.entities.dto.LoanDTO;
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
        LoanDTO loan = loanService.insert(loanDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(loan.getId()).toUri();

        return ResponseEntity.created(uri).build(); // retornar status created 201 com uri do objeto criado
    }
}
