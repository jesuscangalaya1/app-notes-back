package com.challenges.ensolvers.service;

import com.challenges.ensolvers.dto.request.NoteCreationRequest;
import com.challenges.ensolvers.dto.request.NoteRequest;
import com.challenges.ensolvers.dto.response.NoteResponse;
import com.challenges.ensolvers.dto.response.PageableResponse;

public interface NoteService {

    PageableResponse<NoteResponse> paginationNotes(int numeroDePagina, int medidaDePagina,
                                                   String ordenarPor, String sortDir);


    NoteResponse updateNote(Long id, NoteRequest noteRequest);

    NoteResponse getByIdNote(Long id);

    void deleteNote(Long id);



    void archiveUnarchiveNote(Long noteId, boolean archived);

    PageableResponse<NoteResponse> getAllArchivedNotes(int numeroDePagina, int medidaDePagina,
                                                      String ordenarPor, String sortDir);


    void restoreNote(Long id);
    PageableResponse<NoteResponse> getAllDeletedByTrue(int numeroDePagina, int medidaDePagina,
                                                       String ordenarPor, String sortDir);



    // create note
    NoteResponse createNote(NoteCreationRequest noteCreationRequest);

    void deleteNoteById(Long id);
}
