package app.controllers;

import java.util.List;
import java.util.Set;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.dto.CategoryDTO;
import app.entities.CategoryEntity;
import app.entities.NoteEntity;
import app.services.ICategoryService;
import app.services.INoteService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;


    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        try {
            List<CategoryEntity> categories = categoryService.findAll();
            List<CategoryDTO> categoryDTOs = categories.stream().map(CategoryDTO::new).toList();
            return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        try {
            return categoryService.findById(id)
                    .map(category -> new ResponseEntity<>(new CategoryDTO(category), HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryEntity savedCategory = categoryService.save(categoryDTO.toEntity());
            return new ResponseEntity<>(new CategoryDTO(savedCategory), HttpStatus.CREATED);
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
