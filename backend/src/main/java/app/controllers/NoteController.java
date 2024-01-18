package app.controllers;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.dto.NoteDTO;
import app.entities.CategoryEntity;
import app.entities.NoteEntity;
import app.services.ICategoryService;
import app.services.INoteService;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private INoteService noteService;
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<NoteDTO>> getAllNotes() {
        try {
            List<NoteEntity> notes = noteService.findAll();
            List<NoteDTO> noteDTOs = notes.stream()
                    .map(NoteDTO::new) 
                    .toList();
            return new ResponseEntity<>(noteDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable Long id) {
        try {
            return noteService.findById(id)
                    .map(note -> new ResponseEntity<>(new NoteDTO(note), HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<NoteDTO> createNote(@RequestBody NoteDTO noteDTO) {
        try {
            NoteEntity savedNote = noteService.save(noteDTO.toEntity());
            return new ResponseEntity<>(new NoteDTO(savedNote), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Long id, @RequestBody NoteDTO noteDTO) {
        try {
            Optional<NoteEntity> existingNoteOptional = noteService.findById(id);
            if (!existingNoteOptional.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            NoteEntity existingNote = existingNoteOptional.get();

           
            existingNote.setTitle(noteDTO.getTitle());
            existingNote.setContent(noteDTO.getContent());
            existingNote.setArchived(noteDTO.isArchived());

            Set<CategoryEntity> updatedCategories = noteDTO.getCategoryNames().stream()
                .map(categoryName -> categoryService.findByName(categoryName)
                    .orElseGet(() -> {
                        CategoryEntity newCategoryEntity = new CategoryEntity();
                        newCategoryEntity.setName(categoryName);
                        return newCategoryEntity;
                    }))
                .collect(Collectors.toSet());

            existingNote.getCategories().addAll(updatedCategories);

            NoteEntity updatedNote = noteService.save(existingNote);

            return new ResponseEntity<>(new NoteDTO(updatedNote), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        try {
            if (!noteService.findById(id).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            noteService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-category")
    public ResponseEntity<Void> addCategoryToNote(@RequestBody Map<String, String> requestBody) {
        try {
            Long noteId = Long.valueOf(requestBody.get("noteId"));
            String categoryName = requestBody.get("categoryName");

            if (!noteService.findById(noteId).isPresent() || !categoryService.findByName(categoryName).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            NoteEntity note = noteService.findById(noteId).get();
            CategoryEntity category = categoryService.findByName(categoryName).get();
            noteService.addCategory(note, category);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/remove-category")
    public ResponseEntity<Void> removeCategoryFromNote(@RequestBody Map<String, String> requestBody) {
        try {
            Long noteId = Long.parseLong(requestBody.get("noteId"));
            String categoryName = requestBody.get("categoryName");

            Optional<NoteEntity> optionalNote = noteService.findById(noteId);

            if (optionalNote.isPresent()) {
                NoteEntity note = optionalNote.get();
                Set<CategoryEntity> categories = note.getCategories();

                categories.removeIf(category -> category.getName().equals(categoryName));

                noteService.save(note);

                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @GetMapping("/{id}/list-categories")
    public ResponseEntity<Set<CategoryEntity>> listCategoriesForNote(@PathVariable Long id) {
        try {
            Optional<NoteEntity> optionalNote = noteService.findById(id);

            if (optionalNote.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            NoteEntity note = optionalNote.get();
            Set<CategoryEntity> categories = noteService.listCategories(note);
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/archived")
    public ResponseEntity<List<NoteDTO>> getArchivedNotes() {
        List<NoteDTO> archivedNotes = noteService.findByArchivedTrue().stream().map(NoteDTO::new).toList();
        return new ResponseEntity<>(archivedNotes, HttpStatus.OK);
    }

    @GetMapping("/notArchived")
    public ResponseEntity<List<NoteDTO>> getNotArchivedNotes() {
        List<NoteDTO> notArchivedNotes = noteService.findByArchivedFalse().stream().map(NoteDTO::new).toList();
        return new ResponseEntity<>(notArchivedNotes, HttpStatus.OK);
    }

}
