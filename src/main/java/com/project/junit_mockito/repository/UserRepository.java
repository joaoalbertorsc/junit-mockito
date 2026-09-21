package com.project.junit_mockito.repository;

import com.project.junit_mockito.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
