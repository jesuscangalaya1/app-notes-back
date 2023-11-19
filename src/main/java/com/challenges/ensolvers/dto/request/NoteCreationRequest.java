package com.challenges.ensolvers.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NoteCreationRequest {
    @Schema(description = "Datos de la nota")
    private NoteRequest noteRequest;

    @Schema(description = "Lista de IDs de categorías asociadas a la nota")
    private List<Long> categoryIds;  // Esta lista puede ser null o vacía

}
