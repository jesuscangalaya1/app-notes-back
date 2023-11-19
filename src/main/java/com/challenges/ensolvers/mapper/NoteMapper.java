package com.challenges.ensolvers.mapper;


import com.challenges.ensolvers.dto.request.NoteRequest;
import com.challenges.ensolvers.dto.response.NoteResponse;
import com.challenges.ensolvers.entity.NoteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    NoteResponse toProductDTO(NoteEntity productEntity);

    @Mapping(target = "id", ignore = true) // Ignorar el mapeo del campo 'id'
    @Mapping(target = "deleted", ignore = true)
    NoteEntity toProductEntity(NoteRequest productRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateProductFromDto(NoteRequest productRequest, @MappingTarget NoteEntity productEntity);

    List<NoteResponse> toListProductsDTO(List<NoteEntity> productEntityList);

}
