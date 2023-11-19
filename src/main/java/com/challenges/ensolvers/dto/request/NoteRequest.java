package com.challenges.ensolvers.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NoteRequest {

    @Schema(
            description = "Título de la nota",
            example = "Reunión con el equipo de proyecto"
    )
    @NotBlank(message = "El título no debe estar en blanco")
    @Size(max = 100, message = "El título debe tener como máximo {max} caracteres.")
    private String title;

    @Schema(
            description = "Descripción o contenido de la nota",
            example = "Discutir los puntos clave del próximo lanzamiento del proyecto"
    )
    @Size(max = 1000, message = "La descripción debe tener como máximo {max} caracteres.")
    private String description;


}
