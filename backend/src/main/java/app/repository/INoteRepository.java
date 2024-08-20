package app.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.entities.Category;
import app.entities.Note;


public interface INoteRepository extends JpaRepository <Note,Long>{
	
	
	List<Note> findByArchivedTrue();
	
	List<Note> findByArchivedFalse();
	
    @Query("SELECT n FROM Note n JOIN n.categories c WHERE c.id = :categoryId")
    List<Note> findByCategoryId(@Param("categoryId") Long categoryId);

}
