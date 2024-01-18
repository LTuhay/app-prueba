package app.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import app.entities.CategoryEntity;
import app.entities.NoteEntity;

public interface ICategoryService {
	
	public List<CategoryEntity> findAll();
	
	public Optional<CategoryEntity> findByName(String name);

	public Optional<CategoryEntity> findById(Long id);

	public CategoryEntity save(CategoryEntity category);

	public void deleteById(Long id);


}
