package com.project.junit_mockito.repository;

import com.project.junit_mockito.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve persistir os dados corretamente e gerar um ID automático")
    void save_ShouldPersistDataAndGenerateId() {
        User user = new User(null, "Teste Repositorio", "repo@email.com");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("Teste Repositorio", savedUser.getName());
        assertEquals("repo@email.com", savedUser.getEmail());
    }
}
