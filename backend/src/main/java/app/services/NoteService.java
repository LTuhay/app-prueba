package app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.CategoryEntity;
import app.entities.NoteEntity;
import app.repository.CategoryRepository;
import app.repository.NoteRepository;

@Service
public class NoteService implements INoteService {
	
	@Autowired
	private NoteRepository noteRepo;
	@Autowired
	private CategoryRepository categoryRepo;

	@Override
	public List<NoteEntity> findAll() {
		return noteRepo.findAll();
	}

	@Override
	public Optional<NoteEntity> findById(Long id) {
		return noteRepo.findById(id);
	}

	@Override
	public NoteEntity save(NoteEntity note) {
		return noteRepo.save(note);
	}

	@Override
	public void deleteById(Long id) {
		noteRepo.deleteById(id);

	}

	@Override
	public Optional<NoteEntity> findByCategory(String category) {
	    List<NoteEntity> notesWithCategory = new ArrayList<>();
	    for (NoteEntity note : noteRepo.findAll()) {
	        for (CategoryEntity noteCategory : note.getCategories()) {
	            if (noteCategory.getName().equals(category)) {
	                notesWithCategory.add(note);
	                break;
	            }
	        }
	    }

	    if (!notesWithCategory.isEmpty()) {
	        return Optional.of(notesWithCategory.get(0));
	    } else {
	        return Optional.empty();
	    }
	}



	@Override
	public void addCategory(NoteEntity note, CategoryEntity cateogry) {
		Set<CategoryEntity> categories = note.getCategories();
		categories.add(cateogry);
		note.setCategories(categories);
		noteRepo.save(note);

	}

	@Override
	public Set<CategoryEntity> listCategories(NoteEntity note) {
		Set<CategoryEntity> categories = note.getCategories();
		return categories;
	}

	@Override
	public List<NoteEntity> findByArchivedTrue() {
		return noteRepo.findByArchivedTrue();
	}

	@Override
	public List<NoteEntity> findByArchivedFalse() {
		return noteRepo.findByArchivedFalse();
	}

}