package com.yogan.user_registration.repositories;

import com.yogan.user_registration.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<ApplicationUser,Integer> {

    Optional<ApplicationUser> findByEmail(String email);
}
