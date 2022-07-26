package com.aaronr92.studentgradewebservice.repository;

import com.aaronr92.studentgradewebservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByEmailIgnoreCase(String email);

    boolean existsUserByPhone(String phone);

    boolean existsUserByUsername(String username);

    Optional<User> findUserByUsername(String username);
}
