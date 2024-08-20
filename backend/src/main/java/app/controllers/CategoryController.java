package app.controllers;

import java.util.List;
import java.util.Set;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.dto.RequestCategoryDTO;
import app.dto.ResponseCategoryDTO;
import app.dto.ResponseNoteDTO;
import app.entities.Category;
import app.entities.Note;
import app.services.ICategoryService;
import app.services.INoteService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;


    @GetMapping("/")
    public ResponseEntity<List<ResponseCategoryDTO>> getAllCategories() {
        try {
            List<Category> categories = categoryService.findAll();
            List<ResponseCategoryDTO> categoryDTOs = categories.stream().map(ResponseCategoryDTO::new).toList();
            return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
        	Optional<Category> categoryOptional = categoryService.findById(id);
        	if (!categoryOptional.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        	}
        	Category category = categoryOptional.get();
			return new ResponseEntity<>(new ResponseCategoryDTO(category), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<ResponseCategoryDTO> createCategory(@RequestBody RequestCategoryDTO categoryDTO) {
        try {
            Category savedCategory = categoryService.save(categoryDTO.toEntity());
            return new ResponseEntity<>(new ResponseCategoryDTO(savedCategory), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            if (!categoryService.findById(id).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            categoryService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
