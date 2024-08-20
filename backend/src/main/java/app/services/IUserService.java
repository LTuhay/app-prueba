package app.services;

import java.util.Optional;

import app.entities.User;

public interface IUserService {
	
	Optional<User> findById(Long id);
	Optional<User> findByUsername(String username);
	public void save (User user);
	public void delete (User user);
	public boolean existsByUsername(String username);

}
