package com.project.junit_mockito.controller;

import com.project.junit_mockito.model.User;
import com.project.junit_mockito.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar 200 OK e os dados corretos ao realizar GET /users/{id}")
    void getUserById_ShouldReturnUserAndStatus200_WhenIdExists() throws Exception {
        User usuarioFicticio = new User(null, "Maria Silva", "maria@email.com");
        User usuarioSalvo = userRepository.save(usuarioFicticio);

        mockMvc.perform(get("/users/" + usuarioSalvo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioSalvo.getId()))
                .andExpect(jsonPath("$.name").value("Maria Silva"))
                .andExpect(jsonPath("$.email").value("maria@email.com"));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found estruturado quando o ID não existir")
    void getUserById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {
        Long idNaoExistente = 999L;

        mockMvc.perform(get("/users/" + idNaoExistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/users/" + idNaoExistente));
    }

    @Test
    @DisplayName("Deve retornar 201 Created ao salvar usuário com dados válidos")
    void createUser_ShouldReturn201_WhenDataIsValid() throws Exception {
        String jsonValido = """
                {
                    "name": "Ana Costa",
                    "email": "ana@email.com"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Ana Costa"))
                .andExpect(jsonPath("$.email").value("ana@email.com"));
    }

    @Test
    @DisplayName("Deve retornar 409 Conflict estruturado ao tentar salvar e-mail duplicado")
    void createUser_ShouldReturn409_WhenEmailAlreadyExists() throws Exception {
        User usuarioExistente = new User(null, "João Costa", "joao@email.com");
        userRepository.save(usuarioExistente);

        String jsonDuplicado = """
                {
                    "name": "Novo João",
                    "email": "joao@email.com"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDuplicado))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Database Conflict"))
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request estruturado com lista de erros ao enviar dados inválidos")
    void createUser_ShouldReturn400_WhenDataIsInvalid() throws Exception {
        String jsonInvalido = """
                {
                    "name": "",
                    "email": "email-invalido"
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    @DisplayName("Deve retornar 500 Internal Server Error ao enviar requisição sem payload")
    void createUser_ShouldReturn500_WhenPayloadIsMissing() throws Exception {
        // Sem a configuração de um handler específico para HttpMessageNotReadableException,
        // a exceção cai no Exception.class genérico mapeado para 500 no GlobalExceptionHandler.
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value(500));
    }
}