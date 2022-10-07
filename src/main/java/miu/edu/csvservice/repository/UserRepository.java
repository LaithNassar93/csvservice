package miu.edu.csvservice.repository;

import miu.edu.csvservice.domain.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Authentication, Long> {
    Optional<Authentication> getUserByUsername(String username);
}
