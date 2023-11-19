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


import static com.challenges.ensolvers.util.AppConstants.BAD_REQUEST;
import static com.challenges.ensolvers.util.AppConstants.BAD_REQUEST_PRODUCT;


@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final NoteMapper productMapper;

    @Transactional(readOnly = true)
    @Override
    public PageableResponse<NoteResponse> paginationProducts(int numeroDePagina, int medidaDePagina, String ordenarPor, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(ordenarPor).ascending()
                : Sort.by(ordenarPor).descending();
        Pageable pageable = PageRequest.of(numeroDePagina - 1, medidaDePagina, sort);

        // Obtener una página de productos desde el repositorio
        Page<NoteEntity> products = productRepository.findAllByDeletedFalseAndArchivedFalse(pageable);

        // Mapear la página de entidades a una página de DTOs
        List<NoteResponse> productResponsePage = productMapper.toListProductsDTO(products.getContent());

        if (productResponsePage.isEmpty()) {
            throw new BusinessException("P-204", HttpStatus.NO_CONTENT, "Lista Vaciá de Productos");
        }

        return PageableResponse.<NoteResponse>builder()
                .content(productResponsePage)
                .pageNumber(products.getNumber() + 1)
                .pageSize(products.getSize())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .last(products.isLast())
                .build();
    }

    @Transactional
    @Override
    public NoteResponse createProduct(NoteRequest productRequest) {
      /*  // Mapear la solicitud del producto a una entidad de producto
        NoteEntity product = productMapper.toProductEntity(productRequest);

        // Guardar la entidad del producto en la base de datos
        NoteEntity savedProduct = productRepository.save(product);
        // Mapear la entidad del producto guardado a un DTO de respuesta
        return productMapper.toProductDTO(savedProduct);*/
        return null;
    }

    @Override
    public NoteResponse updateProduct(Long id, NoteRequest productRequest) {
        // Buscar el producto existente por su ID
        NoteEntity product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(BAD_REQUEST, HttpStatus.NOT_FOUND, BAD_REQUEST_PRODUCT + id));
        // Actualizar los atributos del producto existente desde el DTO de solicitud
        productMapper.updateProductFromDto(productRequest, product);
        // Guardar el producto actualizado en la base de datos
        product = productRepository.save(product);
        // Mapear la entidad del producto actualizado a un DTO de respuesta
        return productMapper.toProductDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public NoteResponse getByIdProduct(Long id) {
        NoteEntity product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(BAD_REQUEST, HttpStatus.NOT_FOUND, BAD_REQUEST_PRODUCT + id));

        return productMapper.toProductDTO(product);
    }

    @Transactional
    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BusinessException(BAD_REQUEST, HttpStatus.NOT_FOUND, BAD_REQUEST_PRODUCT + id);
        }
        // Desactivar (eliminar lógicamente) un producto por su ID
        productRepository.desactivarProduct(id);
    }

    @Transactional
    @Override
    public void archiveUnarchiveNote(Long noteId, boolean archived) {
        if (!productRepository.existsById(noteId)) {
            throw new BusinessException(BAD_REQUEST, HttpStatus.NOT_FOUND, "Nota no encontrada con el ID: " + noteId);
        }
        productRepository.changeNoteArchivedStatus(noteId, archived);
    }

    @Transactional(readOnly = true)
    @Override
    public PageableResponse<NoteResponse> getAllArchivedNotes(int numeroDePagina, int medidaDePagina, String ordenarPor, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(ordenarPor).ascending()
                : Sort.by(ordenarPor).descending();
        Pageable pageable = PageRequest.of(numeroDePagina - 1, medidaDePagina, sort);

        // Obtener una página de productos desde el repositorio
        Page<NoteEntity> products = productRepository.findAllByArchivedTrueAndDeletedFalse(pageable);

        // Mapear la página de entidades a una página de DTOs
        List<NoteResponse> productResponsePage = productMapper.toListProductsDTO(products.getContent());

        if (productResponsePage.isEmpty()) {
            throw new BusinessException("P-204", HttpStatus.NO_CONTENT, "Lista Vaciá de Productos");
        }

        return PageableResponse.<NoteResponse>builder()
                .content(productResponsePage)
                .pageNumber(products.getNumber() + 1)
                .pageSize(products.getSize())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .last(products.isLast())
                .build();
    }
    @Transactional
    @Override
    public void restoreNote(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BusinessException(BAD_REQUEST, HttpStatus.NOT_FOUND, "Nota no encontrada con el ID: " + id);
        }
        productRepository.restoreNoteFromTrash(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PageableResponse<NoteResponse> getAllDeletedByTrue(int numeroDePagina, int medidaDePagina, String ordenarPor, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(ordenarPor).ascending()
                : Sort.by(ordenarPor).descending();
        Pageable pageable = PageRequest.of(numeroDePagina - 1, medidaDePagina, sort);

        // Obtener una página de productos desde el repositorio
        Page<NoteEntity> products = productRepository.findAllByDeletedTrue(pageable);

        // Mapear la página de entidades a una página de DTOs
        List<NoteResponse> productResponsePage = productMapper.toListProductsDTO(products.getContent());

        if (productResponsePage.isEmpty()) {
            throw new BusinessException("P-204", HttpStatus.NO_CONTENT, "Lista Vaciá de Productos");
        }

        return PageableResponse.<NoteResponse>builder()
                .content(productResponsePage)
                .pageNumber(products.getNumber() + 1)
                .pageSize(products.getSize())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .last(products.isLast())
                .build();
    }







    @Transactional
    @Override
    public NoteResponse createNote(NoteCreationRequest noteCreationRequest) {
        NoteEntity note = productMapper.toProductEntity(noteCreationRequest.getNoteRequest());

        // Asociar categorías solo si se proporcionan IDs de categoría
        if (noteCreationRequest.getCategoryIds() != null && !noteCreationRequest.getCategoryIds().isEmpty()) {
            List<CategoryEntity> categoriesList = categoryRepository.findAllById(noteCreationRequest.getCategoryIds());
            Set<CategoryEntity> categoriesSet = new HashSet<>(categoriesList);
            note.setCategorias(categoriesSet);
        }else {
            note.setCategorias(new HashSet<>());
        }

        NoteEntity savedNote = productRepository.save(note);
        return productMapper.toProductDTO(savedNote);
    }
}
