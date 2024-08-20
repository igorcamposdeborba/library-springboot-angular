package br.edu.infnet.libraryigor.controller;

import br.edu.infnet.libraryigor.model.entities.dto.BookDTO;
import br.edu.infnet.libraryigor.model.entities.dto.UserEmailRequest;
import br.edu.infnet.libraryigor.model.entities.dto.UsersDTO;
import br.edu.infnet.libraryigor.model.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/user") // rota
@Tag(name = "User", description = "Manage Users") // descricao no swagger
public class UsersController {
    @Autowired
    private UsersService userService;

    @Operation(
            description = "Get all Users",
            summary = "The users are going to retrieved from Library repository",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200")
            }
    )
    @GetMapping(value = {"/", ""}) // endpoint
    public ResponseEntity<List<UsersDTO>> findAll(){

        List<UsersDTO> userList = userService.findAll();

        return ResponseEntity.ok().body(userList);
    }
    @Operation(
            description = "Get user by Id",
            summary = "Get specific user from Library repository",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<UsersDTO> findById(@PathVariable @Valid Integer id){ // controla resposta http do servidor (eu especifico header e body) retorna o DTO

        UsersDTO user = this.userService.findById(id); // buscar objeto no banco de dados

        return ResponseEntity.ok().body(user); // RESPONSE para o usuário do objeto pego do banco de dados
    }

    @Operation(
            description = "Get user by e-mail",
            summary = "Get specific user from Library repository",
            responses = {
                    @ApiResponse(description = "Ok", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @PostMapping(value = "/email") //! get do http seria um possivel problema de compliance/LGPD por expor dado sensivel a qualquer pessoa que escuta a conexao porque nao tem encriptacao no get
    public ResponseEntity<UsersDTO> findByEmail(@RequestBody @Valid UserEmailRequest request){

        UsersDTO user = this.userService.findByEmail(request.getEmail()); // metodo personalizado no repository para consultar pelo email

        return ResponseEntity.ok().body(user);
    }

    @PostMapping(value = "/single", produces = "application/json") // produces especifica o formato de saída para o Swagger
    public ResponseEntity<BookDTO> insert(@Valid @RequestBody UsersDTO userDTO){
        // Inserir pelo service no banco de dados
        UsersDTO user = userService.insert(userDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).build(); // retornar status created 201 com uri do objeto criado
    }
}
