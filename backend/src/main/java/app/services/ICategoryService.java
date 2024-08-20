package app.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import app.entities.Category;
import app.entities.Note;

public interface ICategoryService {
	
	public List<Category> findAll();

	public Optional<Category> findById(Long id);

	public Category save(Category category);

	public void deleteById(Long id);
	
	public Optional<Category> findByName(String name);

}
