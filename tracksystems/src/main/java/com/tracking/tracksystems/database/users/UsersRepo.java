package com.tracking.tracksystems.database.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<User, String> {
    Optional<User> findByEmployeeIdOrEmail(String employeeId, String email);
    Optional<User> findByPhone(String phone);
}
