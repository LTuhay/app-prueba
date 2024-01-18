package app.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import app.entities.CategoryEntity;
import app.entities.NoteEntity;

public class NoteDTO {

    private Long id;
    private String title;
    private String content;
    private boolean archived;
    private Set<String> categoryNames = new HashSet<>();
    
	public NoteDTO() {
		
	}
    
	
	
	public Set<String> getCategoryNames() {
		return categoryNames;
	}



	public void setCategoryNames(Set<String> categoryNames) {
		this.categoryNames = categoryNames;
	}



	public NoteDTO(NoteEntity noteEntity) {
	    this.id = noteEntity.getId();
	    this.title = noteEntity.getTitle();
	    this.content = noteEntity.getContent();
	    this.archived = noteEntity.isArchived();
        Set<String> categoryNames = noteEntity.getCategories().stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toSet());
        this.setCategoryNames(categoryNames);
	}


	public NoteEntity toEntity() {
	    NoteEntity noteEntity = new NoteEntity();
	    noteEntity.setId(this.id);
	    noteEntity.setTitle(this.title);
	    noteEntity.setContent(this.content);
	    noteEntity.setArchived(this.archived);
        Set<CategoryEntity> categoryEntities = this.categoryNames.stream()
                .map(categoryName -> {
                    CategoryEntity categoryEntity = new CategoryEntity();
                    categoryEntity.setName(categoryName);
                    return categoryEntity;
                })
                .collect(Collectors.toSet());
        noteEntity.setCategories(categoryEntities);
	    return noteEntity;
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

