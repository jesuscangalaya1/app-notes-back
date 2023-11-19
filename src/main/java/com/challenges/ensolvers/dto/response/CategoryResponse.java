package com.challenges.ensolvers.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryResponse {

    private Long id;
    private String name;
    private List<NoteResponse> notes = new ArrayList<>();

}
