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
        Users entity = null;
        switch (userDTO.getType()){
            case Constants.STUDENT -> {
                entity = new Student(userDTO);
                ((Student) entity).setPendingPenaltiesAmount(Constants.ZERO);
                ((Student) entity).setCourseName(userDTO.getCourseName());
                entity.setLibrary(library);
                break;
            }
            case Constants.ASSOCIATE -> {
                entity = new Associate(userDTO);
                ((Associate) entity).setDepartment(userDTO.getDepartment());
                ((Associate) entity).setSpecialty(userDTO.getSpecialty());
                entity.setLibrary(library);
                break;
            }
        }

        entity = userRepository.save(entity); // salvar no banco de dados
        return new UsersDTO(entity); // retornar o que foi salvo no banco de dados
    }
}
