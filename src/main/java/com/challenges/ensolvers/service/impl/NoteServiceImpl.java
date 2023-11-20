package com.challenges.ensolvers.service.impl;

import com.challenges.ensolvers.dto.request.NoteCreationRequest;
import com.challenges.ensolvers.dto.request.NoteRequest;
import com.challenges.ensolvers.dto.response.NoteResponse;
import com.challenges.ensolvers.dto.response.PageableResponse;
import com.challenges.ensolvers.entity.CategoryEntity;
import com.challenges.ensolvers.entity.NoteEntity;
import com.challenges.ensolvers.exceptions.BusinessException;
import com.challenges.ensolvers.mapper.NoteMapper;
import com.challenges.ensolvers.repository.CategoryRepository;
import com.challenges.ensolvers.repository.NoteRepository;
import com.challenges.ensolvers.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import static com.challenges.ensolvers.util.AppConstants.BAD_REQUEST;
import static com.challenges.ensolvers.util.AppConstants.BAD_REQUEST_NOTE;


@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;
    private final NoteMapper noteMapper;

    @Transactional(readOnly = true)
    @Override
    public PageableResponse<NoteResponse> paginationNotes(int numeroDePagina, int medidaDePagina, String ordenarPor, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(ordenarPor).ascending()
                : Sort.by(ordenarPor).descending();
        Pageable pageable = PageRequest.of(numeroDePagina - 1, medidaDePagina, sort);

        Page<NoteEntity> notes = noteRepository.findAllByDeletedFalseAndArchivedFalseAndDeletedFromTrashFalse(pageable);

        List<NoteResponse> noteResponsePage = notes.getContent().stream()
                .map(noteEntity -> {
                    NoteResponse noteResponse = noteMapper.toNoteDTO(noteEntity);
                    noteResponse.setCategories(noteMapper.mapCategories(noteEntity.getCategorias()));
                    return noteResponse;
                }).collect(Collectors.toList());

        if (noteResponsePage.isEmpty()) {
            throw new BusinessException("P-204", HttpStatus.NO_CONTENT, "Empty List of Notes");
        }

        return PageableResponse.<NoteResponse>builder()
                .content(noteResponsePage)
                .pageNumber(notes.getNumber() + 1)
                .pageSize(notes.getSize())
                .totalPages(notes.getTotalPages())
                .totalElements(notes.getTotalElements())
                .last(notes.isLast())
                .build();
    }


    @Override
    public NoteResponse updateNote(Long id, NoteRequest productRequest) {
        NoteEntity note = noteRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(BAD_REQUEST, HttpStatus.NOT_FOUND, BAD_REQUEST_NOTE + id));
        noteMapper.updateNoteFromDto(productRequest, note);
        note = noteRepository.save(note);
        return noteMapper.toNoteDTO(note);
    }

    @Override
    @Transactional(readOnly = true)
    public NoteResponse getByIdNote(Long id) {
        NoteEntity note = noteRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(BAD_REQUEST, HttpStatus.NOT_FOUND, BAD_REQUEST_NOTE + id));

        return noteMapper.toNoteDTO(note);
    }

    @Transactional
    @Override
    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new BusinessException(BAD_REQUEST, HttpStatus.NOT_FOUND, BAD_REQUEST_NOTE + id);
        }
        noteRepository.desactivarProduct(id);
    }

    @Transactional
    @Override
    public void archiveUnarchiveNote(Long noteId, boolean archived) {
        if (!noteRepository.existsById(noteId)) {
            throw new BusinessException(BAD_REQUEST, HttpStatus.NOT_FOUND, "Note not found by id: " + noteId);
        }
        noteRepository.changeNoteArchivedStatus(noteId, archived);
    }

    @Transactional(readOnly = true)
    @Override
    public PageableResponse<NoteResponse> getAllArchivedNotes(int numeroDePagina, int medidaDePagina, String ordenarPor, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(ordenarPor).ascending()
                : Sort.by(ordenarPor).descending();
        Pageable pageable = PageRequest.of(numeroDePagina - 1, medidaDePagina, sort);

        Page<NoteEntity> note = noteRepository.findAllByArchivedTrueAndDeletedFalse(pageable);
        List<NoteResponse> noteResponsePage = noteMapper.toListNoteDTO(note.getContent());

        if (noteResponsePage.isEmpty()) {
            throw new BusinessException("P-204", HttpStatus.NO_CONTENT, "List empty of archived notes");
        }

        return PageableResponse.<NoteResponse>builder()
                .content(noteResponsePage)
                .pageNumber(note.getNumber() + 1)
                .pageSize(note.getSize())
                .totalPages(note.getTotalPages())
                .totalElements(note.getTotalElements())
                .last(note.isLast())
                .build();
    }
    @Transactional
    @Override
    public void restoreNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new BusinessException(BAD_REQUEST, HttpStatus.NOT_FOUND, "Note not found by id: " + id);
        }
        noteRepository.restoreNoteFromTrash(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PageableResponse<NoteResponse> getAllDeletedByTrue(int numeroDePagina, int medidaDePagina, String ordenarPor, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(ordenarPor).ascending()
                : Sort.by(ordenarPor).descending();
        Pageable pageable = PageRequest.of(numeroDePagina - 1, medidaDePagina, sort);
        Page<NoteEntity> notes = noteRepository.findAllByDeletedTrue(pageable);
        List<NoteResponse> noteResponsePage = noteMapper.toListNoteDTO(notes.getContent());
        if (noteResponsePage.isEmpty()) {
            throw new BusinessException("P-204", HttpStatus.NO_CONTENT, "List empty of deleted notes");
        }
        return PageableResponse.<NoteResponse>builder()
                .content(noteResponsePage)
                .pageNumber(notes.getNumber() + 1)
                .pageSize(notes.getSize())
                .totalPages(notes.getTotalPages())
                .totalElements(notes.getTotalElements())
                .last(notes.isLast())
                .build();
    }

    @Transactional
    @Override
    public NoteResponse createNote(NoteCreationRequest noteCreationRequest) {
        NoteEntity note = noteMapper.toNoteEntity(noteCreationRequest.getNoteRequest());
        if (noteCreationRequest.getCategoryIds() != null && !noteCreationRequest.getCategoryIds().isEmpty()) {
            List<CategoryEntity> categoriesList = categoryRepository.findAllById(noteCreationRequest.getCategoryIds());
            Set<CategoryEntity> categoriesSet = new HashSet<>(categoriesList);
            note.setCategorias(categoriesSet);
        }else {
            note.setCategorias(new HashSet<>());
        }
        NoteEntity savedNote = noteRepository.save(note);
        return noteMapper.toNoteDTO(savedNote);
    }

    @Override
    @Transactional
    public void deleteNoteById(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new BusinessException(BAD_REQUEST, HttpStatus.NOT_FOUND, "Note not found by id: " + id);
        }
        noteRepository.deleteFromTrash(id);
    }
}
