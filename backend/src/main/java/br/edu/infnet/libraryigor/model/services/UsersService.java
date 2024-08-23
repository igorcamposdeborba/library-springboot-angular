package br.edu.infnet.libraryigor.model.services;

import br.edu.infnet.libraryigor.Constants;
import br.edu.infnet.libraryigor.model.entities.Library;
import br.edu.infnet.libraryigor.model.entities.Loan;
import br.edu.infnet.libraryigor.model.entities.client.Associate;
import br.edu.infnet.libraryigor.model.entities.client.Student;
import br.edu.infnet.libraryigor.model.entities.client.Users;
import br.edu.infnet.libraryigor.model.entities.dto.UsersDTO;
import br.edu.infnet.libraryigor.model.repositories.LibraryRepository;
import br.edu.infnet.libraryigor.model.repositories.LoanRepository;
import br.edu.infnet.libraryigor.model.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
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

    ObjectMapper objectMapper = new ObjectMapper();

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
    public UsersDTO update(String userIdInput, UsersDTO userDTO) {
        // Converter String para Integer id
        Integer id = Integer.parseInt(userIdInput);

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
                validatePenalty(userDatabase);

                Student entity = objectMapper.convertValue(userDatabase.get(), Student.class);
//                Student entity = (Student) userDatabase.get();

                if (userDatabase.get() instanceof Student){
                    entity.setPendingPenaltiesAmount(((Student) userDatabase.get()).getPendingPenaltiesAmount());
                }
                entity.setCourseName(userDTO.getCourseName());
                entity.setLibrary(library);

                deleteUserAssociateToChangeUserType(userDatabase); // deletar usuário se for uma mudança de tipo de usuário de Associate para Student.

                userRepository.save(entity); // salvar no banco de dados
//                user = userRepository.save(entity); // salvar no banco de dados
            }

            case Constants.ASSOCIATE -> {
                validatePenalty(userDatabase);

                Associate entity = objectMapper.convertValue(userDatabase.get(), Associate.class);
//                Associate entity = (Associate) userDatabase.get();

                entity.setDepartment(userDTO.getDepartment());
                entity.setSpecialty(userDTO.getSpecialty());
                entity.setLibrary(library);

                deleteUserStudentToChangeUserType(userDatabase); // deletar usuário se for uma mudança de tipo de usuário de Student para Associate.

                userRepository.save(entity); // salvar no banco de dados
//                user = userRepository.save(entity); // salvar no banco de dados
            }
        }
        return userDTO;
//        return new UsersDTO(user); // retornar o que foi salvo no banco de dados
    }

    private void deleteUserStudentToChangeUserType(Optional<Users> userDatabase) {
        validateDeletion(userDatabase);

        if (userDatabase.get() instanceof Student && ((Student) userDatabase.get()).getPendingPenaltiesAmount().equals(0.0)){
            userRepository.delete(userDatabase.get());
        }
    }
    private void deleteUserAssociateToChangeUserType(Optional<Users> userDatabase) {
        validateDeletion(userDatabase);

        if (userDatabase.get() instanceof Associate){
            userRepository.delete(userDatabase.get());
        }
    }

    private Optional<Users> validateDeletion(Optional<Users> userDatabase){
        Integer userId = userDatabase.get().getId();
        List<Loan> allLoansOfLibrary = loanRepository.findAll();

        Optional<Loan> emptyLoanForUser = allLoansOfLibrary.stream()
                .filter(loan -> loan.getUser().getId().equals(userId) &&
                        loan.getEffectiveTo().isBefore(LocalDate.now()) &&
                        ! loan.getEffectiveFrom().isAfter(LocalDate.now())).findAny();

        if (emptyLoanForUser.isEmpty()){
            return userDatabase;
        } else {
            throw new DataIntegrityViolationException("Há um empréstimo em aberto. Não é possível alterar ou deletar usuário");
        }
    }

    private static void validatePenalty(Optional<Users> userDatabase) {
        if (userDatabase.get() instanceof Student && ((Student) userDatabase.get()).getPendingPenaltiesAmount() > 0.0){
            throw new IllegalArgumentException("Você não pode trocar o tipo de usuário porque há multas a serem pagas no valor de R$ "
                    + ((Student) userDatabase.get()).getPendingPenaltiesAmount());
        }
    }

    @Transactional
    public void deleteById(Integer id) {
        // !todo: cria validacao para nao excluir livro alugado neste periodo atual
        // Deletar no banco de dados
        userRepository.deleteById(id);
    }
}
