package app.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import app.entities.Category;
import app.entities.Note;

public class ResponseNoteDTO {

    private Long id;
    private String title;
    private String content;
    private boolean archived;
    private Set<String> categoryNames = new HashSet<>();
    
	public ResponseNoteDTO() {
		
	}
    
	public ResponseNoteDTO(Note noteEntity) {
	    this.id = noteEntity.getId();
	    this.title = noteEntity.getTitle();
	    this.content = noteEntity.getContent();
	    this.archived = noteEntity.isArchived();
        Set<String> categoryNames = noteEntity.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toSet());
        this.setCategoryNames(categoryNames);
	}
	
	public Set<String> getCategoryNames() {
		return categoryNames;
	}



	public void setCategoryNames(Set<String> categoryNames) {
		this.categoryNames = categoryNames;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}





}

