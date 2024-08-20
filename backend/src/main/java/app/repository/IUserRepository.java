package app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entities.User;

public interface IUserRepository extends JpaRepository<User,Long> {
	
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
	
}
