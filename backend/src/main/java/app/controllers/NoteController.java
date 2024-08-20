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

import app.dto.RequestNoteDTO;
import app.dto.ResponseCategoryDTO;
import app.dto.ResponseNoteDTO;
import app.entities.Category;
import app.entities.Note;
import app.services.ICategoryService;
import app.services.INoteService;

@RestController
@RequestMapping("/notes")
public class NoteController {

	@Autowired
	private INoteService noteService;
	@Autowired
	private ICategoryService categoryService;

	@GetMapping("/")
	public ResponseEntity<List<ResponseNoteDTO>> getAllNotes() {
		try {
			List<Note> notes = noteService.findAll();
			List<ResponseNoteDTO> noteDTOs = notes.stream().map(ResponseNoteDTO::new).toList();
			return new ResponseEntity<>(noteDTOs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getNoteById(@PathVariable Long id) {
		try {
			Optional<Note> noteOptional = noteService.findById(id);
			if (!noteOptional.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found");
			}
			Note note = noteOptional.get();
			return new ResponseEntity<>(new ResponseNoteDTO(note), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/")
	public ResponseEntity<?> createNote(@RequestBody RequestNoteDTO noteDTO) {
		try {
			Note savedNote = noteService.save(noteDTO.toEntity());
			return new ResponseEntity<>(new ResponseNoteDTO(savedNote), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateNote(@PathVariable Long id, @RequestBody RequestNoteDTO noteDTO) {
		try {
			Optional<Note> existingNoteOptional = noteService.findById(id);
			if (!existingNoteOptional.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found");
			}
			Note existingNote = existingNoteOptional.get();

			existingNote = updateNoteTitleAndContent(noteDTO, existingNote);

			Note updatedNote = noteService.save(existingNote);

			return new ResponseEntity<>(new ResponseNoteDTO(updatedNote), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Note updateNoteTitleAndContent(RequestNoteDTO noteDTO, Note existingNote) {
		if (noteDTO.getTitle() != null) {
			existingNote.setTitle(noteDTO.getTitle());
		}
		if (noteDTO.getContent() != null) {
			existingNote.setContent(noteDTO.getContent());
		}
		return existingNote;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable Long id) {
		try {
			if (!noteService.findById(id).isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found");
			}
			noteService.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{noteId}/category")
	public ResponseEntity<?> addCategoryToNote(@PathVariable Long noteId, @RequestParam String categoryName) {
		try {
			Optional<Note> optionalNote = noteService.findById(noteId);
			Optional<Category> optionalCategory = categoryService.findByName(categoryName);

			if (!optionalNote.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found");
			}

			if (!optionalCategory.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
			}

			Note note = optionalNote.get();
			Category category = optionalCategory.get();
			noteService.addCategory(note, category);
			return ResponseEntity.ok("Category added to note successfully");
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{noteId}/category")
	public ResponseEntity<?> removeCategoryFromNote(@PathVariable Long noteId, @RequestParam String categoryName) {
	    try {
	        Optional<Note> optionalNote = noteService.findById(noteId);
	        Optional<Category> optionalCategory = categoryService.findByName(categoryName);

	        if (!optionalNote.isPresent()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found");
	        }

	        if (!optionalCategory.isPresent()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
	        }

	        Note note = optionalNote.get();
	        Category category = optionalCategory.get();
	        Set<Category> categories = note.getCategories();

	        categories.remove(category);

	        noteService.save(note);

	        return new ResponseEntity<>(HttpStatus.OK);

	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	@GetMapping("/{id}/categories")
	public ResponseEntity<?> listCategoriesForNote(@PathVariable Long id) {
		try {
			Optional<Note> optionalNote = noteService.findById(id);

			if (optionalNote.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found");
			}

			Note note = optionalNote.get();
			Set<Category> categories = noteService.listCategories(note);
            List<ResponseCategoryDTO> categoryDTOs = categories.stream().map(ResponseCategoryDTO::new).toList();
			return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	 @GetMapping("/category/{categoryId}")
	    public ResponseEntity<?> getNotesByCategory(@PathVariable Long categoryId) {
	        try {
	        	if (categoryService.findById(categoryId) == null) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
	        	}
	            List<Note> notes = noteService.findByCategoryId(categoryId);
	            List<ResponseNoteDTO> noteDTOs = notes.stream().map(ResponseNoteDTO::new).collect(Collectors.toList());
	            return new ResponseEntity<>(noteDTOs, HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	
	@PatchMapping("/{id}/archive-status")
	public ResponseEntity<?> toggleArchiveStatus(@PathVariable Long id, @RequestParam boolean archived) {
	    try {
	        Optional<Note> noteOptional = noteService.findById(id);
	        if (noteOptional.isEmpty()) {
	        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found");
	        }
	        Note note = noteOptional.get();
	        note.setArchived(archived);
	        Note updatedNote = noteService.save(note);
	        return ResponseEntity.ok(new ResponseNoteDTO(updatedNote));
	    } catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	@GetMapping("/archived")
	public ResponseEntity<List<ResponseNoteDTO>> getArchivedNotes() {
		List<ResponseNoteDTO> archivedNotes = noteService.findByArchivedTrue().stream().map(ResponseNoteDTO::new).toList();
		return new ResponseEntity<>(archivedNotes, HttpStatus.OK);
	}

	@GetMapping("/not-archived")
	public ResponseEntity<List<ResponseNoteDTO>> getNotArchivedNotes() {
		List<ResponseNoteDTO> notArchivedNotes = noteService.findByArchivedFalse().stream().map(ResponseNoteDTO::new).toList();
		return new ResponseEntity<>(notArchivedNotes, HttpStatus.OK);
	}

}
