package com.challenges.ensolvers.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class NoteResponse {

    private Long id;
    private String title;
    private String description;
    private boolean archived;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    private boolean deleted;

}
