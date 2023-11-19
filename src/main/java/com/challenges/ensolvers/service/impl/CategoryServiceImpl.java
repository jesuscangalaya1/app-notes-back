package com.challenges.ensolvers.service.impl;

import com.challenges.ensolvers.dto.request.CategoryRequest;
import com.challenges.ensolvers.dto.response.CategoryResponse;
import com.challenges.ensolvers.dto.response.NoteResponse;
import com.challenges.ensolvers.entity.CategoryEntity;
import com.challenges.ensolvers.entity.NoteEntity;
import com.challenges.ensolvers.mapper.NoteMapper;
import com.challenges.ensolvers.repository.CategoryRepository;
import com.challenges.ensolvers.repository.NoteRepository;
import com.challenges.ensolvers.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper; // Asumiendo un mapeador para NoteEntity


    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (!categoryRepository.existsByName(categoryRequest.getName()).isPresent()) {
            throw new IllegalArgumentException("La categoría ya existe.");
        }
        CategoryEntity category = new CategoryEntity();
        category.setName(categoryRequest.getName());
        category = categoryRepository.save(category);

        return mapToCategoryResponse(category);
    }

    @Override
    @Transactional
    public void addCategoryToNote(Long noteId, Long categoryId) {
        NoteEntity note = noteRepository.findById(noteId)
                .orElseThrow(() -> new EntityNotFoundException("Nota no encontrada."));
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada."));

        note.getCategorias().add(category);
        noteRepository.save(note);
    }
    @Override
    public void removeCategoryFromNote(Long noteId, Long categoryId) {
        NoteEntity note = noteRepository.findById(noteId)
                .orElseThrow(() -> new EntityNotFoundException("Nota no encontrada."));
        note.getCategorias().removeIf(category -> category.getId().equals(categoryId));
        noteRepository.save(note);
    }

    @Override
    public CategoryResponse getCategoryWithNotes(Long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada."));

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());

        List<NoteResponse> noteSummaries = category.getNotas()
                .stream()
                .map(noteMapper::toProductDTO)
                .collect(Collectors.toList());

        categoryResponse.setNotes(noteSummaries);

        return categoryResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public List<NoteResponse> getNotesByCategoryName(String categoryName) {
        List<NoteEntity> notes = noteRepository.findAllByCategoryNameAndDeletedFalse(categoryName);
        return notes.stream().map(noteMapper::toProductDTO).collect(Collectors.toList());
    }

    // Método auxiliar para mapear a CategoryResponse
    private CategoryResponse mapToCategoryResponse(CategoryEntity category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        return categoryResponse;
    }
}
