package app.dto;

import app.entities.Note;

public class RequestNoteDTO {

    private String title;
    private String content;
    

	public Note toEntity() {
	    Note noteEntity = new Note();
	    noteEntity.setTitle(this.title);
	    noteEntity.setContent(this.content);
	    noteEntity.setArchived(false);
	    return noteEntity;
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


}

