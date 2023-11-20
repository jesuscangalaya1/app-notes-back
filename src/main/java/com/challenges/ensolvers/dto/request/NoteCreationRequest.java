package com.challenges.ensolvers.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NoteCreationRequest {
    @Schema(description = "Note data")
    private NoteRequest noteRequest;

    @Schema(description = "List of category IDs associated with the note")
    private List<Long> categoryIds;

}
