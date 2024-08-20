package app.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import app.entities.Category;
import app.entities.Note;

public interface INoteService {
	
	public List<Note> findAll();

	public Optional<Note> findById(Long id);

	public Note save(Note note);

	public void deleteById(Long id);

	public Optional<Note> findByCategory(String category);

	public void addCategory (Note note, Category category);

	public Set<Category> listCategories (Note note);
	
	public List<Note> findByArchivedTrue();
	
	public List<Note> findByArchivedFalse();
	
	public List<Note> findByCategoryId(Long categoryId);

}
