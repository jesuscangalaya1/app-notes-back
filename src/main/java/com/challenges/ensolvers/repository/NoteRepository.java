package com.challenges.ensolvers.repository;


import com.challenges.ensolvers.entity.NoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {

    Page<NoteEntity> findAllByDeletedFalseAndArchivedFalse(Pageable pageable);

    Optional<NoteEntity> findByIdAndDeletedFalse(Long id);

    // Consulta para desactivar un producto por su ID
    @Modifying
    @Query("UPDATE NoteEntity p SET p.deleted = true WHERE p.id = :productId")
    void desactivarProduct(@Param("productId") Long productId);

    @Modifying
    @Query("UPDATE NoteEntity n SET n.archived = :archived WHERE n.id = :noteId")
    void changeNoteArchivedStatus(@Param("noteId") Long noteId, @Param("archived") boolean archived);

    Page<NoteEntity> findAllByArchivedTrueAndDeletedFalse(Pageable pageable);

    Page<NoteEntity> findAllByDeletedTrue(Pageable pageable);

    @Modifying
    @Query("UPDATE NoteEntity n SET n.deleted = false WHERE n.id = :noteId")
    void restoreNoteFromTrash(@Param("noteId") Long noteId);

    @Query("SELECT n FROM NoteEntity n JOIN n.categorias c WHERE c.name = :categoryName AND n.deleted = false")
    List<NoteEntity> findAllByCategoryNameAndDeletedFalse(@Param("categoryName") String categoryName);


}
