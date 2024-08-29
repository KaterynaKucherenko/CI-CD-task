package com.mjc.school.repository.implementation;

import com.mjc.school.repository.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("authorRepository")
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);
    boolean existsByUsername(String username);
}
