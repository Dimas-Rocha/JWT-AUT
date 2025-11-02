package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.jwt_aut.entity.User;

@Repository
public interface UserRepository {
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
	@Query("SELECT u FROM User u WHERE u.role = :role")
	Optional<User> findByRole(String role);

}
