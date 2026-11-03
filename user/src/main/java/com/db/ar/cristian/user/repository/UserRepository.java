package com.db.ar.cristian.user.repository;

import com.db.ar.cristian.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByDoc(String doc);
}
