package com.challenges.ensolvers.controller;

import com.challenges.ensolvers.dto.request.NoteCreationRequest;
import com.challenges.ensolvers.dto.request.NoteRequest;
import com.challenges.ensolvers.dto.response.NoteResponse;
import com.challenges.ensolvers.dto.response.PageableResponse;
import com.challenges.ensolvers.dto.response.RestResponse;
import com.challenges.ensolvers.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static com.challenges.ensolvers.util.AppConstants.*;

@Validated
@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@Tag(name = "NOTES", description = "Permitted operations on the entity Note")
@CrossOrigin(origins = "*")
public class NoteController {

    private final NoteService productService;

    @Operation(summary = "Get list about all paginated actives notes")
    @GetMapping(value = "/pagination", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<PageableResponse<NoteResponse>> pageableNotes(
            @RequestParam(value = "pageNo", defaultValue = NUMERO_DE_PAGINA_POR_DEFECTO, required = false) int numeroDePagina,
            @RequestParam(value = "pageSize", defaultValue = MEDIDA_DE_PAGINA_POR_DEFECTO, required = false) int medidaDePagina,
            @RequestParam(value = "sortBy", defaultValue = ORDENAR_POR_DEFECTO, required = false) String ordenarPor,
            @RequestParam(value = "sortDir", defaultValue = ORDENAR_DIRECCION_POR_DEFECTO, required = false) String sortDir) {

        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                "NOTE SUCCESSFULLY READED",
                productService.paginationNotes(numeroDePagina, medidaDePagina, ordenarPor, sortDir));
    }

    @Operation(summary = "create a new note")
    @ApiResponse(responseCode = "201", description = "Note created successfully")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteResponse> createNote(@RequestBody @Valid NoteCreationRequest noteCreationRequest) {
        NoteResponse noteResponse = productService.createNote(noteCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteResponse);
    }

    @Operation(summary = "Update an existing note by its ID")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<NoteResponse> updateNote(@Positive(message = "The ID only accepts positive numbers")
            @PathVariable  Long id , @RequestBody @Valid NoteRequest productRequest) {

        return new RestResponse<>("SUCCESS",
                String.valueOf(HttpStatus.OK),
                "NOTE SUCCESSFULLY UPDATED",
                productService.updateNote(id, productRequest));
    }

    @Operation(summary = "Get information about a note by its ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<NoteResponse> getByIdProduct(@Positive(message = "The ID only accepts positive numbers")
            @PathVariable Long id) {

        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_NOTE + id + " SUCCESSFULLY READED",
                productService.getByIdNote(id));
    }


    @Operation(summary = "send the note to the trash")
    @PatchMapping(value = "/{id}")
    public RestResponse<String> deleteNoteById(@Positive(message = "The ID only accepts positive numbers")
            @PathVariable Long id) {

        productService.deleteNote(id);
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_NOTE + id + " SUCCESSFULLY DELETED",
                "null"); // Data null.
    }

    @Operation(summary = "Archive and unarchive a note")
    @PatchMapping("/{id}/archive")
    public RestResponse<String > archiveUnarchiveNote(@PathVariable Long id,
                                                     @RequestParam boolean archived) {
        productService.archiveUnarchiveNote(id, archived);
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_NOTE + id + " SUCCESSFULLY ARCHIVED",
                "null"); // Data null.
    }

    @Operation(summary = "Get the list of all paginated archived notes")
    @GetMapping(value = "/archived", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<PageableResponse<NoteResponse>> getAllArchivedNotes(
            @RequestParam(value = "pageNo", defaultValue = NUMERO_DE_PAGINA_POR_DEFECTO, required = false) int numeroDePagina,
            @RequestParam(value = "pageSize", defaultValue = MEDIDA_DE_PAGINA_POR_DEFECTO, required = false) int medidaDePagina,
            @RequestParam(value = "sortBy", defaultValue = ORDENAR_POR_DEFECTO, required = false) String ordenarPor,
            @RequestParam(value = "sortDir", defaultValue = ORDENAR_DIRECCION_POR_DEFECTO, required = false) String sortDir) {

        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                "NOTE SUCCESSFULLY READED",
                productService.getAllArchivedNotes(numeroDePagina, medidaDePagina, ordenarPor, sortDir));
    }


    @Operation(summary = "Get the list of all deleted paginated notes")
    @GetMapping(value = "/deleted", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<PageableResponse<NoteResponse>> getAllDeletedByTrue(
            @RequestParam(value = "pageNo", defaultValue = NUMERO_DE_PAGINA_POR_DEFECTO, required = false) int numeroDePagina,
            @RequestParam(value = "pageSize", defaultValue = MEDIDA_DE_PAGINA_POR_DEFECTO, required = false) int medidaDePagina,
            @RequestParam(value = "sortBy", defaultValue = ORDENAR_POR_DEFECTO, required = false) String ordenarPor,
            @RequestParam(value = "sortDir", defaultValue = ORDENAR_DIRECCION_POR_DEFECTO, required = false) String sortDir) {

        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                "NOTE SUCCESSFULLY READED",
                productService.getAllDeletedByTrue(numeroDePagina, medidaDePagina, ordenarPor, sortDir));
    }

    @Operation(summary = "restore a note by its ID")
    @PatchMapping(value = "/{id}/restore")
    public RestResponse<String> restoreNote(@Positive(message = "The ID only accepts positive numbers")
                                            @PathVariable Long id) {

        productService.restoreNote(id);
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_NOTE + id + " SUCCESSFULLY RESTORE NOTE",
                "null"); // Data null.
    }

    @Operation(summary = "permanently delete a note by its ID")

    @DeleteMapping("/{id}")
    public RestResponse<String> deleteNote(@Positive(message = "The ID only accepts positive numbers")
                                            @PathVariable Long id) {

        productService.deleteNoteById(id);
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_NOTE + id + " SUCCESSFULLY DELETED PERMANENTLY",
                "null"); // Data null.
    }

}





