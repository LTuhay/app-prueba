package app.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entities.CategoryEntity;
import app.entities.NoteEntity;


public interface NoteRepository extends JpaRepository <NoteEntity,Long>{
	
	
	List<NoteEntity> findByArchivedTrue();
	
	List<NoteEntity> findByArchivedFalse();
	

}
