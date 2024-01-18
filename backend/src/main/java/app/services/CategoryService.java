package app.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.CategoryEntity;
import app.entities.NoteEntity;
import app.repository.CategoryRepository;

@Service
public class CategoryService implements ICategoryService {
	
	@Autowired
	private CategoryRepository categoryRepo;

	@Override
	public List<CategoryEntity> findAll() {		
		return categoryRepo.findAll();
	}

	@Override
	public Optional<CategoryEntity> findById(Long id) {		
		return categoryRepo.findById(id);
	}

	@Override
	public CategoryEntity save(CategoryEntity category) {
		return categoryRepo.save(category);
	}

	@Override
	public void deleteById(Long id) {
		categoryRepo.deleteById(id);
	}

	@Override
	public Optional<CategoryEntity> findByName(String name) {		
		return categoryRepo.findByName(name);
	}



}