package br.edu.infnet.libraryigor.model.entities.dto.api;

public class QuoteDTO {
    private String reference;
    private String text;

    public QuoteDTO(String reference, String text) {
        this.reference = reference;
        this.text = text;
    }
    public QuoteDTO(){}

    public String getReference() {
        return reference;
    }

    public String getText() {
        return text;
    }
}
