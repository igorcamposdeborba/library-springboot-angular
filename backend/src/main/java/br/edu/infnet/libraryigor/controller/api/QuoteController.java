package br.edu.infnet.libraryigor.controller.api;

import br.edu.infnet.libraryigor.model.entities.dto.api.QuoteDTO;
import br.edu.infnet.libraryigor.model.services.api.QuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    @Operation(
            description = "Get random quote",
            summary = "The random quoted from external API of bible",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200")
            }
    )
    @GetMapping(value = "/random-quote", produces = "application/json")
    public ResponseEntity<QuoteDTO> findRandomQuote() {

        QuoteDTO quoteDTO = quoteService.getRandomQuote();

        return ResponseEntity.ok().body(quoteDTO);
    }
}
