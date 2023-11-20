package com.challenges.ensolvers.service.impl;

import com.challenges.ensolvers.dto.request.CategoryRequest;
import com.challenges.ensolvers.dto.response.CategoryResponse;
import com.challenges.ensolvers.dto.response.NoteResponse;
import com.challenges.ensolvers.dto.response.PageableResponse;
import com.challenges.ensolvers.entity.CategoryEntity;
import com.challenges.ensolvers.entity.NoteEntity;
import com.challenges.ensolvers.exceptions.BusinessException;
import com.challenges.ensolvers.mapper.NoteMapper;
import com.challenges.ensolvers.repository.CategoryRepository;
import com.challenges.ensolvers.repository.NoteRepository;
import com.challenges.ensolvers.service.CategoryService;
import com.challenges.ensolvers.util.MapsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static com.challenges.ensolvers.util.AppConstants.BAD_REQUEST;
import static com.challenges.ensolvers.util.AppConstants.BAD_REQUEST_NOTE;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;


    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName()).isEmpty()) {
            throw new IllegalArgumentException("The category already exists.");
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
                .orElseThrow(() -> new EntityNotFoundException("Note not found."));
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found."));

        note.getCategorias().add(category);
        noteRepository.save(note);
    }

    @Override
    public void removeCategoryFromNote(Long noteId, Long categoryId) {
        NoteEntity note = noteRepository.findById(noteId)
                .orElseThrow(() -> new EntityNotFoundException("Note not found."));
        note.getCategorias().removeIf(category -> category.getId().equals(categoryId));
        noteRepository.save(note);
    }

    @Override
    public CategoryResponse getCategoryWithNotes(Long categoryId) {
        CategoryEntity category = categoryRepository.findByIdAndDeletedFalse(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found."));

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());

        List<NoteResponse> noteSummaries = category.getNotas()
                .stream()
                .map(noteMapper::toNoteDTO)
                .collect(Collectors.toList());

        categoryResponse.setNotes(noteSummaries);

        return categoryResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public List<NoteResponse> getNotesByCategoryName(String categoryName) {
        List<NoteEntity> notes = noteRepository.findAllByCategoryNameAndDeletedFalse(categoryName);
        if (notes.isEmpty()) {
            throw new BusinessException(BAD_REQUEST_NOTE, HttpStatus.NOT_FOUND, "No notes found with the category: " + categoryName);
        }
        return notes.stream()
                .map(MapsUtils::mapNoteEntityToNoteResponse)
                .collect(Collectors.toList());
    }


    @Override
    public PageableResponse<CategoryResponse> getAllCategories(int numeroDePagina, int medidaDePagina, String ordenarPor, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(ordenarPor).ascending()
                : Sort.by(ordenarPor).descending();
        Pageable pageable = PageRequest.of(numeroDePagina - 1, medidaDePagina, sort);
        Page<CategoryEntity> categories = categoryRepository.findAllByDeletedFalse(pageable);
        List<CategoryResponse> categoryResponseList = categories.getContent().stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());

        if (categoryResponseList.isEmpty()) {
            throw new BusinessException("P-204", HttpStatus.NO_CONTENT, "List empty of categories.");
        }

        return PageableResponse.<CategoryResponse>builder()
                .content(categoryResponseList)
                .pageNumber(categories.getNumber() + 1)
                .pageSize(categories.getSize())
                .totalPages(categories.getTotalPages())
                .totalElements(categories.getTotalElements())
                .last(categories.isLast())
                .build();
    }

    @Override
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        CategoryEntity category = categoryRepository.findByIdAndDeletedFalse(categoryId)
                .orElseThrow(() -> new BusinessException(BAD_REQUEST, HttpStatus.NOT_FOUND, "Category not found with ID: " + categoryId));

        category.setName(categoryRequest.getName());

        CategoryEntity updatedCategory = categoryRepository.save(category);
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(updatedCategory.getId());
        categoryResponse.setName(updatedCategory.getName());
        return categoryResponse;
    }

    // Helper method to map to CategoryResponse
    private CategoryResponse mapToCategoryResponse(CategoryEntity category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        return categoryResponse;
    }


}
