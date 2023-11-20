package com.challenges.ensolvers.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NoteRequest {

    @Schema(
            description = "Título de la nota",
            example = "Reunión con el equipo de proyecto"
    )
    @NotBlank(message = "The title should not be blank")
    @Size(max = 100, message = "The title must have a maximum {max} characters.")
    private String title;

    @Schema(
            description = "Descripción o contenido de la nota",
            example = "Discutir los puntos clave del próximo lanzamiento del proyecto"
    )
    @Size(max = 1000, message = "The description must have at most {max} characters.")
    private String description;


}
