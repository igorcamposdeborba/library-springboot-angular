package br.edu.infnet.libraryigor.model.services;

import br.edu.infnet.libraryigor.Constants;
import br.edu.infnet.libraryigor.model.entities.Book;
import br.edu.infnet.libraryigor.model.entities.Library;
import br.edu.infnet.libraryigor.model.entities.client.Associate;
import br.edu.infnet.libraryigor.model.entities.client.Student;
import br.edu.infnet.libraryigor.model.entities.client.Users;
import br.edu.infnet.libraryigor.model.entities.dto.BookDTO;
import br.edu.infnet.libraryigor.model.entities.dto.UsersDTO;
import br.edu.infnet.libraryigor.model.repositories.LibraryRepository;
import br.edu.infnet.libraryigor.model.repositories.LoanRepository;
import br.edu.infnet.libraryigor.model.repositories.UserRepository;
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
public class UsersService {


    @Autowired
    private UserRepository userRepository; // injetar instancia do repository para buscar do banco de dados via JPA
    @Autowired
    private LibraryRepository libraryRepository;
    @Autowired
    private LoanRepository loanRepository;

    public List<UsersDTO> findAll(){
        List<Users> usersList = userRepository.findAll(Sort.by("name")); // buscar no banco de dados e ordenar por nome
        // converter a lista de classe para DTO
        return usersList.stream().filter(Objects::nonNull).map((Users user) -> new UsersDTO(user)).collect(Collectors.toList());
    }
    public UsersDTO findById(Integer id) {
        Optional<Users> user = userRepository.findById(id);

        return new UsersDTO(user.orElseThrow(() -> new ObjectNotFoundException(id, "Usuário não encontrado pelo id")));
    }
    public UsersDTO findByEmail(String email) {
        Optional<Users> user = userRepository.findByEmail(email);
        return new UsersDTO(user.orElseThrow(() -> new ObjectNotFoundException(
                                                        Optional.ofNullable(email), "Usuário não encontrado pelo e-mail")));
    }

    @Transactional
    public UsersDTO insert(@Valid UsersDTO userDTO) { // @Valid: validar objeto (annotations e atributos)
        Optional<Users> user = userRepository.findByEmail(userDTO.getEmail());
        if (user.isPresent()) {
            throw new DataIntegrityViolationException("Já existe este usuario cadastrado com este e-mail");
        }

        Library library = libraryRepository.findById(userDTO.getLibraryId()).stream().findAny().get();

        // Mapear DTO para classe
        Users userEntity = null;
        switch (userDTO.getType()){
            case Constants.STUDENT -> {
                Student entity = new Student(userDTO);
                entity.setPendingPenaltiesAmount(Constants.ZERO);
                entity.setCourseName(userDTO.getCourseName());
                entity.setLibrary(library);

                userEntity = userRepository.save(entity); // salvar no banco de dados
            }
            case Constants.ASSOCIATE -> {
                Associate entity = new Associate(userDTO);
                entity.setDepartment(userDTO.getDepartment());
                entity.setSpecialty(userDTO.getSpecialty());
                entity.setLibrary(library);

                userEntity = userRepository.save(entity); // salvar no banco de dados
            }
        }

        return new UsersDTO(userEntity); // retornar o que foi salvo no banco de dados
    }

    @Transactional
    public UsersDTO update(String idInput, UsersDTO userDTO) {
        // Converter String para Integer id
        Integer id = Integer.parseInt(idInput);

        Optional<Users> userDatabase = Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        Constants.NOT_FOUND_BOOK, Optional.of(userDTO.getId()))));
        Library library = libraryRepository.findById(userDTO.getLibraryId()).stream().findAny().get();

        // Validar se o id passado é o mesmo que está no banco de dados para evitar que o usuário altere o id
        if (userDatabase.get().getId().equals(id)) {
            userDTO.setId(id);
        }

        // Mapear DTO para classe
        Users user = null;
        switch (userDTO.getType()){
            case Constants.STUDENT -> {
                Student entity = new Student(userDTO);
                // Todo: bug: esse casting faz com que o usuário não possa trocar de Associata <-> para Student
                entity.setPendingPenaltiesAmount(((Student) userDatabase.get()).getPendingPenaltiesAmount());
                entity.setCourseName(userDTO.getCourseName());
                entity.setLibrary(library);

                user = userRepository.save(entity); // salvar no banco de dados
            }
            case Constants.ASSOCIATE -> {
                Associate entity = new Associate(userDTO);
                entity.setDepartment(userDTO.getDepartment());
                entity.setSpecialty(userDTO.getSpecialty());
                entity.setLibrary(library);

                user = userRepository.save(entity); // salvar no banco de dados
            }
        }
        return new UsersDTO(user); // retornar o que foi salvo no banco de dados

    }

    @Transactional
    public void deleteById(Integer id) {
        // !todo: cria validacao para nao excluir livro alugado neste periodo atual
        // Deletar no banco de dados
        userRepository.deleteById(id);
    }
}
