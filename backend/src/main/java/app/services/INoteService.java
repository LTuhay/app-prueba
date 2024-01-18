package app.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import app.entities.CategoryEntity;
import app.entities.NoteEntity;

public interface INoteService {
	
	public List<NoteEntity> findAll();

	public Optional<NoteEntity> findById(Long id);

	public NoteEntity save(NoteEntity note);

	public void deleteById(Long id);

	public Optional<NoteEntity> findByCategory(String category);

	public void addCategory (NoteEntity note, CategoryEntity category);

	public Set<CategoryEntity> listCategories (NoteEntity note);
	
	public List<NoteEntity> findByArchivedTrue();
	
	public List<NoteEntity> findByArchivedFalse();

}
