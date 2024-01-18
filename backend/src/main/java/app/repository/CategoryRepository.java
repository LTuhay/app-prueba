package app.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entities.CategoryEntity;
import app.entities.NoteEntity;

public interface CategoryRepository extends JpaRepository <CategoryEntity,Long>{
	
	Optional<CategoryEntity> findByName(String name);
	
}
