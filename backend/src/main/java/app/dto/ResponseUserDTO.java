package app.dto;

import app.entities.User;

public class ResponseUserDTO {

    private Long id;
    private String username;
      
    
	public ResponseUserDTO() {
		super();
	}



	public ResponseUserDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}    
    

}

