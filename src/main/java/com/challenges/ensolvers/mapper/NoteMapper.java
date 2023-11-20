package com.challenges.ensolvers.mapper;


import com.challenges.ensolvers.dto.request.NoteRequest;
import com.challenges.ensolvers.dto.response.CategoryResponse;
import com.challenges.ensolvers.dto.response.NoteResponse;
import com.challenges.ensolvers.entity.CategoryEntity;
import com.challenges.ensolvers.entity.NoteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    default List<CategoryResponse> mapCategories(Set<CategoryEntity> categories) {
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }
        return categories.stream()
                .map(this::toCategoryDTO)
                .collect(Collectors.toList());
    }
    CategoryResponse toCategoryDTO(CategoryEntity categoryEntity);



    NoteResponse toNoteDTO(NoteEntity noteEntity);

    @Mapping(target = "id", ignore = true) // Ignorar el mapeo del campo 'id'
    @Mapping(target = "deleted", ignore = true)
    NoteEntity toNoteEntity(NoteRequest noteRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateNoteFromDto(NoteRequest noteRequest, @MappingTarget NoteEntity noteEntity);

    List<NoteResponse> toListNoteDTO(List<NoteEntity> noteEntityList);

}
