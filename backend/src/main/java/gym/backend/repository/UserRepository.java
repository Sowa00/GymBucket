package gym.backend.repository;

import gym.backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> { // Zmieniono z Integer na Long
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // New methods for password reset and email verification
    Optional<User> findByResetPasswordToken(String resetPasswordToken);
    Optional<User> findByEmailVerificationToken(String emailVerificationToken);
}