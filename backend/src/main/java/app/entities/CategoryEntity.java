package app.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<NoteEntity> notes = new HashSet<>();

	public CategoryEntity() {

	}

	public CategoryEntity(String name, Set<NoteEntity> notes) {
		this.name = name;
		this.notes = notes;
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

	public Set<NoteEntity> getNotes() {
		return notes;
	}

	public void setNotes(Set<NoteEntity> notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "CategoryEntity [id=" + id + ", name=" + name + ", notes=" + notes + "]";
	}

	

}