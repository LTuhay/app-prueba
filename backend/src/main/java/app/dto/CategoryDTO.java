package app.dto;

import app.entities.CategoryEntity;


public class CategoryDTO {

    private Long id;
    private String name;
    
    
	public CategoryDTO() {

	}
	
    public CategoryDTO(CategoryEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
    
    public CategoryEntity toEntity() {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(this.id);
        entity.setName(this.name);
        return entity;
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