package com.example._post.Repository;

import com.example._post.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAuthRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
