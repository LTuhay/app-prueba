package app.dto;

import app.entities.User;

public class RequestUserDTO {

    private String username;
    private String password;
    
    
	public RequestUserDTO() {
		super();
	}


	public RequestUserDTO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}


	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



}
