package com.project.junit_mockito.service;

import com.project.junit_mockito.exception.UserNotFoundException;
import com.project.junit_mockito.model.User;
import com.project.junit_mockito.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o ID: " + id));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
