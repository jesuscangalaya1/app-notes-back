package com.challenges.ensolvers.controller;

import com.challenges.ensolvers.dto.request.CategoryRequest;
import com.challenges.ensolvers.dto.response.CategoryResponse;
import com.challenges.ensolvers.dto.response.NoteResponse;
import com.challenges.ensolvers.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "CATEGORIES", description = "O")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }


    @PostMapping("/{categoryId}/notes/{noteId}/add")
    public ResponseEntity<Void> addCategoryToNote(@PathVariable Long categoryId,
                                                  @PathVariable Long noteId) {
        categoryService.addCategoryToNote(noteId, categoryId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{categoryId}/notes/{noteId}/remove")
    public ResponseEntity<Void> removeCategoryFromNote(@PathVariable Long categoryId,
                                                       @PathVariable Long noteId) {
        categoryService.removeCategoryFromNote(noteId, categoryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryWithNotes(@PathVariable Long categoryId) {
        CategoryResponse categoryResponse = categoryService.getCategoryWithNotes(categoryId);
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping("/notes")
    public ResponseEntity<List<NoteResponse>> getNotesByCategoryName(@RequestParam String categoryName) {
        List<NoteResponse> notes = categoryService.getNotesByCategoryName(categoryName);
        return ResponseEntity.ok(notes);
    }

}
