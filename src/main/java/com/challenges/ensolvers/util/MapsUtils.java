package com.challenges.ensolvers.util;

import com.challenges.ensolvers.dto.response.CategoryResponse;
import com.challenges.ensolvers.dto.response.NoteResponse;
import com.challenges.ensolvers.entity.CategoryEntity;
import com.challenges.ensolvers.entity.NoteEntity;

import java.util.List;
import java.util.stream.Collectors;

public class MapsUtils {

    public static NoteResponse mapNoteEntityToNoteResponse(NoteEntity noteEntity) {
        NoteResponse noteResponse = new NoteResponse();

        noteResponse.setId(noteEntity.getId());
        noteResponse.setTitle(noteEntity.getTitle());
        noteResponse.setDescription(noteEntity.getDescription());
        noteResponse.setArchived(noteEntity.isArchived());
        noteResponse.setCreationDate(noteEntity.getCreationDate());
        noteResponse.setLastModifiedDate(noteEntity.getLastModifiedDate());
        noteResponse.setDeleted(noteEntity.isDeleted());

        // Mapeo manual de las categorías
        if (noteEntity.getCategorias() != null) {
            List<CategoryResponse> categoryResponses = noteEntity.getCategorias().stream()
                    .map(MapsUtils::mapCategoryEntityToCategoryResponse)
                    .collect(Collectors.toList());
            noteResponse.setCategories(categoryResponses);
        }

        return noteResponse;
    }

    public static CategoryResponse mapCategoryEntityToCategoryResponse(CategoryEntity categoryEntity) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(categoryEntity.getId());
        categoryResponse.setName(categoryEntity.getName());
        return categoryResponse;
    }
}
