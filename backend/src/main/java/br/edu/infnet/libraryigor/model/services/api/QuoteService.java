package br.edu.infnet.libraryigor.model.services.api;

import br.edu.infnet.libraryigor.model.entities.dto.api.QuoteDTO;
import br.edu.infnet.libraryigor.model.repositories.api.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {
    @Autowired
    private Quote quoteAPI;

    public QuoteDTO getRandomQuote() {
        return quoteAPI.getRandomVerse();
    }
}
