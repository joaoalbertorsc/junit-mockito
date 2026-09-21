package com.project.junit_mockito.service;

import com.project.junit_mockito.exception.UserNotFoundException;
import com.project.junit_mockito.model.User;
import com.project.junit_mockito.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve retornar usuário quando o ID existir")
    void getUserById_ShouldReturnUser_WhenIdExists() {
        Long idExistente = 1L;
        User usuarioFicticio = new User(idExistente, "Carlos Silva", "carlos@email.com");

        when(userRepository.findById(idExistente)).thenReturn(Optional.of(usuarioFicticio));

        User resultado = userService.getUserById(idExistente);

        assertEquals(idExistente, resultado.getId());
        assertEquals("Carlos Silva", resultado.getName());
        assertEquals("carlos@email.com", resultado.getEmail());
        verify(userRepository).findById(idExistente);
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando o ID não existir")
    void getUserById_ShouldThrowUserNotFoundException_WhenIdDoesNotExist() {
        Long idNaoExistente = 99L;

        when(userRepository.findById(idNaoExistente)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(idNaoExistente));
        verify(userRepository).findById(idNaoExistente);
    }

    @Test
    @DisplayName("Deve salvar e retornar o usuário com sucesso")
    void createUser_ShouldSaveAndReturnUser() {
        User usuarioParaSalvar = new User(null, "Novo Usuario", "novo@email.com");
        User usuarioSalvo = new User(1L, "Novo Usuario", "novo@email.com");

        when(userRepository.save(usuarioParaSalvar)).thenReturn(usuarioSalvo);

        User resultado = userService.createUser(usuarioParaSalvar);

        assertEquals(1L, resultado.getId());
        assertEquals("Novo Usuario", resultado.getName());
        assertEquals("novo@email.com", resultado.getEmail());
        verify(userRepository).save(usuarioParaSalvar);
    }
}
