package app.dto;

import app.entities.Category;


public class ResponseCategoryDTO {

    private Long id;
    private String name;
    
    
	public ResponseCategoryDTO() {

	}
	
    public ResponseCategoryDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	@Override
	public String toString() {
		return "CategoryDTO [id=" + id + ", name=" + name + ", notes=" + "]";
	}

    
}