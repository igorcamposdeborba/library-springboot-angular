package br.edu.infnet.libraryigor.model.repositories.api;

import br.edu.infnet.libraryigor.model.entities.dto.api.QuoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "bibleApi", url = "https://bible-api.com/") // OpenFeign para fazer chamadas para APIS externas de terceiros
public interface Quote {
    @GetMapping("/?random=verse&translation=almeida")
    QuoteDTO getRandomVerse();
}
