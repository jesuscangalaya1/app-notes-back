package com.challenges.ensolvers.repository;

import com.challenges.ensolvers.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {


    Page<CategoryEntity> findAllByDeletedFalse(Pageable pageable);


    Optional<CategoryEntity> existsByName(String name);

    Optional<CategoryEntity> findByIdAndDeletedFalse(Long categoryId);
}

