package com.ntechinedumvictor.slash_point.repository;

import com.ntechinedumvictor.slash_point.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();

    Optional<User> findByEmail(String email);

    User findUserByEmail(String email);
    Optional<User> findUserById(int id);

    User findUserByEmailAndPassword(String email, String password);

    void delete(User user);
}
