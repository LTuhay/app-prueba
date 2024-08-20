package app.dto;

import app.entities.Category;


public class RequestCategoryDTO {

    private String name;
      
    
    public Category toEntity() {
        Category entity = new Category();
        entity.setName(this.name);
        return entity;
    }


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Override
	public String toString() {
		return "CategoryDTO [name=" + name + ", notes=" + "]";
	}

    
}