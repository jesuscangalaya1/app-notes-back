package com.challenges.ensolvers.service;

import com.challenges.ensolvers.dto.request.CategoryRequest;
import com.challenges.ensolvers.dto.response.CategoryResponse;
import com.challenges.ensolvers.dto.response.NoteResponse;
import com.challenges.ensolvers.dto.response.PageableResponse;

import java.util.List;


public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest categoryRequest);

    void addCategoryToNote(Long noteId, Long categoryId);

    void removeCategoryFromNote(Long noteId, Long categoryId);

    CategoryResponse getCategoryWithNotes(Long categoryId);

    List<NoteResponse> getNotesByCategoryName(String categoryName);



    PageableResponse<CategoryResponse> getAllCategories(int numeroDePagina, int medidaDePagina,
                                                       String ordenarPor, String sortDir);

    CategoryResponse updateCategory(Long categoryId, CategoryRequest categoryRequest);
}
