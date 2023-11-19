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
@Tag(name = "NOTES", description = "Operaciones permitidas sobre la entidad Producto")
@CrossOrigin(origins = "*")
public class NoteController {

    private final NoteService productService;

    @Operation(summary = "Obtener la información de todos los productos paginados")
    @GetMapping(value = "/pagination", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<PageableResponse<NoteResponse>> pageableProducts(
            @RequestParam(value = "pageNo", defaultValue = NUMERO_DE_PAGINA_POR_DEFECTO, required = false) int numeroDePagina,
            @RequestParam(value = "pageSize", defaultValue = MEDIDA_DE_PAGINA_POR_DEFECTO, required = false) int medidaDePagina,
            @RequestParam(value = "sortBy", defaultValue = ORDENAR_POR_DEFECTO, required = false) String ordenarPor,
            @RequestParam(value = "sortDir", defaultValue = ORDENAR_DIRECCION_POR_DEFECTO, required = false) String sortDir) {

        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                "PRODUCT SUCCESSFULLY READED",
                productService.paginationProducts(numeroDePagina, medidaDePagina, ordenarPor, sortDir));
    }

    @Operation(summary = "create Note")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteResponse> createNote(@RequestBody @Valid NoteCreationRequest noteCreationRequest) {
        NoteResponse noteResponse = productService.createNote(noteCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteResponse);
    }

    @Operation(summary = "Actualizar un producto existente por su ID")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<NoteResponse> updateProduct(@Positive(message = "el ID solo acepta numeros positivos")
            @PathVariable  Long id , @RequestBody @Valid NoteRequest productRequest) {

        return new RestResponse<>("SUCCESS",
                String.valueOf(HttpStatus.OK),
                "PRODUCT SUCCESSFULLY UPDATED",
                productService.updateProduct(id, productRequest));
    }

    @Operation(summary = "Obtener información de un producto por su ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<NoteResponse> getByIdProduct(@Positive(message = "el ID solo acepta numeros positivos")
            @PathVariable Long id) {

        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_PRODUCT + id + " SUCCESSFULLY READED",
                productService.getByIdProduct(id));
    }


    @Operation(summary = "Eliminar un producto por su ID")
    @PatchMapping(value = "/{id}")
    public RestResponse<String> deleteProduct(@Positive(message = "el ID solo acepta numeros positivos")
            @PathVariable Long id) {

        productService.deleteProduct(id);
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_PRODUCT + id + " SUCCESSFULLY DELETED",
                "null"); // Data null.
    }


    @PatchMapping("/{id}/archive")
    public RestResponse<String > archiveUnarchiveNote(@PathVariable Long id,
                                                     @RequestParam boolean archived) {
        productService.archiveUnarchiveNote(id, archived);
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_PRODUCT + id + " SUCCESSFULLY ARCHIVED",
                "null"); // Data null.
    }

    @Operation(summary = "Obtener la información de todos los productos paginados")
    @GetMapping(value = "/archived", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<PageableResponse<NoteResponse>> getAllArchivedNotes(
            @RequestParam(value = "pageNo", defaultValue = NUMERO_DE_PAGINA_POR_DEFECTO, required = false) int numeroDePagina,
            @RequestParam(value = "pageSize", defaultValue = MEDIDA_DE_PAGINA_POR_DEFECTO, required = false) int medidaDePagina,
            @RequestParam(value = "sortBy", defaultValue = ORDENAR_POR_DEFECTO, required = false) String ordenarPor,
            @RequestParam(value = "sortDir", defaultValue = ORDENAR_DIRECCION_POR_DEFECTO, required = false) String sortDir) {

        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                "PRODUCT SUCCESSFULLY READED",
                productService.getAllArchivedNotes(numeroDePagina, medidaDePagina, ordenarPor, sortDir));
    }

    @Operation(summary = "restoreNote un producto por su ID")
    @PatchMapping(value = "/{id}/restore")
    public RestResponse<String> restoreNote(@Positive(message = "el ID solo acepta numeros positivos")
                                              @PathVariable Long id) {

        productService.restoreNote(id);
        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                MESSAGE_ID_PRODUCT + id + " SUCCESSFULLY RESTORE NOTE",
                "null"); // Data null.
    }

    @Operation(summary = "Obtener la información de todos los productos paginados")
    @GetMapping(value = "/deleted", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse<PageableResponse<NoteResponse>> getAllDeletedByTrue(
            @RequestParam(value = "pageNo", defaultValue = NUMERO_DE_PAGINA_POR_DEFECTO, required = false) int numeroDePagina,
            @RequestParam(value = "pageSize", defaultValue = MEDIDA_DE_PAGINA_POR_DEFECTO, required = false) int medidaDePagina,
            @RequestParam(value = "sortBy", defaultValue = ORDENAR_POR_DEFECTO, required = false) String ordenarPor,
            @RequestParam(value = "sortDir", defaultValue = ORDENAR_DIRECCION_POR_DEFECTO, required = false) String sortDir) {

        return new RestResponse<>(SUCCESS,
                String.valueOf(HttpStatus.OK),
                "PRODUCT SUCCESSFULLY READED",
                productService.getAllDeletedByTrue(numeroDePagina, medidaDePagina, ordenarPor, sortDir));
    }
}





