package com.lcwd.electronic.store.ElectronicStore.repositories;

import com.lcwd.electronic.store.ElectronicStore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByUserEmail(String userEmail);

    User findByUserEmailAndUserPassword(String userEmail,String UserPassword);

    List<User> findByUserNameContaining(String keyword);
}
