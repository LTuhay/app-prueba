package app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entities.Category;

public interface ICategoryRepository extends JpaRepository <Category,Long>{
	
	Optional<Category> findByName(String name);
	
}
