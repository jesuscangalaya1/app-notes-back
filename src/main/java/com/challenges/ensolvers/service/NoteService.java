package com.challenges.ensolvers.service;

import com.challenges.ensolvers.dto.request.NoteCreationRequest;
import com.challenges.ensolvers.dto.request.NoteRequest;
import com.challenges.ensolvers.dto.response.NoteResponse;
import com.challenges.ensolvers.dto.response.PageableResponse;

import java.util.List;

public interface NoteService {

    PageableResponse<NoteResponse> paginationProducts(int numeroDePagina, int medidaDePagina,
                                                      String ordenarPor, String sortDir);

    NoteResponse createProduct(NoteRequest noteRequest);

    NoteResponse updateProduct(Long id, NoteRequest noteRequest);

    NoteResponse getByIdProduct(Long id);

    void deleteProduct(Long id);



    void archiveUnarchiveNote(Long noteId, boolean archived);

    PageableResponse<NoteResponse> getAllArchivedNotes(int numeroDePagina, int medidaDePagina,
                                                      String ordenarPor, String sortDir);


    void restoreNote(Long id);
    PageableResponse<NoteResponse> getAllDeletedByTrue(int numeroDePagina, int medidaDePagina,
                                                       String ordenarPor, String sortDir);



    // create note
    NoteResponse createNote(NoteCreationRequest noteCreationRequest);
}
