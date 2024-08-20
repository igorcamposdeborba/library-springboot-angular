package br.edu.infnet.libraryigor.model.entities;

import br.edu.infnet.libraryigor.model.entities.client.Users;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable // Chave composta
public class LoanRecord implements Serializable { // Serializable para trafegar em rede por bytes
    private static final long serialVersionUID = 1L; // versão para serializacao/deserializacao para dar match com o que está sendo trafegado

    private Integer bookId;
    private Integer userId;

    public LoanRecord(Book book, Users user){
        this.bookId = book.getId();
        this.userId = user.getId();
    }
    public LoanRecord(Integer bookId, Integer userId){
        this.bookId = bookId;
        this.userId = userId;
    }

    public LoanRecord() {} // JPA precisa de construtor vazio público para persistir no banco de dados

    public Integer getBookId() {
        return bookId;
    }
    public Integer getUserId(){ return userId; }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LOAN_RECORD{" +
                "userId: " + userId +
                "bookId:" + bookId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoanRecord that = (LoanRecord) o;

        if (!bookId.equals(that.bookId)) return false;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        int result = bookId.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }
}
