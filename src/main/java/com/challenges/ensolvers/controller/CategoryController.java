package com.challenges.ensolvers.controller;

import com.challenges.ensolvers.dto.request.CategoryRequest;
import com.challenges.ensolvers.dto.response.CategoryResponse;
import com.challenges.ensolvers.dto.response.NoteResponse;
import com.challenges.ensolvers.dto.response.PageableResponse;
import com.challenges.ensolvers.dto.response.RestResponse;
import com.challenges.ensolvers.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.challenges.ensolvers.util.AppConstants.*;

@Validated
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "CATEGORIES", description = "Permitted operations on the entity Category")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;


    @Operation(summary = "Create a new category")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }


    @Operation(summary = "Add a category to a note")
    @PostMapping("/{categoryId}/notes/{noteId}/add")
    public ResponseEntity<Void> addCategoryToNote(@PathVariable Long categoryId,
                                                  @PathVariable Long noteId) {
        categoryService.addCategoryToNote(noteId, categoryId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove a category from a note")
    @DeleteMapping("/{categoryId}/notes/{noteId}/remove")
    public ResponseEntity<Void> removeCategoryFromNote(@PathVariable Long categoryId,
                                                       @PathVariable Long noteId) {
        categoryService.removeCategoryFromNote(noteId, categoryId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get a category by its ID")
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryWithNotes(@PathVariable Long categoryId) {
        CategoryResponse categoryResponse = categoryService.getCategoryWithNotes(categoryId);
        return ResponseEntity.ok(categoryResponse);
    }

    @Operation(summary = "Filter notes by category name")
    @GetMapping("/notes")
    public ResponseEntity<List<NoteResponse>> getNotesByCategoryName(@RequestParam String categoryName) {
        List<NoteResponse> notes = categoryService.getNotesByCategoryName(categoryName);
        return ResponseEntity.ok(notes);
    }


    @Operation(summary = "Get list about all paginated actives categories")
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<PageableResponse<CategoryResponse>> pageableCategories(
            @RequestParam(value = "pageNo", defaultValue = NUMERO_DE_PAGINA_POR_DEFECTO, required = false) int numeroDePagina,
            @RequestParam(value = "pageSize", defaultValue = MEDIDA_DE_PAGINA_POR_DEFECTO, required = false) int medidaDePagina,
            @RequestParam(value = "sortBy", defaultValue = ORDENAR_POR_DEFECTO, required = false) String ordenarPor,
            @RequestParam(value = "sortDir", defaultValue = ORDENAR_DIRECCION_POR_DEFECTO, required = false) String sortDir) {

        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                "CATEGORIES SUCCESSFULLY READED",
                categoryService.getAllCategories(numeroDePagina, medidaDePagina, ordenarPor, sortDir));
    }


    @Operation(summary = "Update a category by its ID")
    @PutMapping("/{categoryId}")
    public RestResponse<CategoryResponse> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryRequest categoryRequest) {
        return new RestResponse<>("SUCCESS",
                String.valueOf(HttpStatus.OK),
                "CATEGORY SUCCESSFULLY UPDATED",
                categoryService.updateCategory(categoryId, categoryRequest));
    }



}
