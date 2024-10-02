package com.pruebaproyecto.app.service.impl;

import com.pruebaproyecto.app.domain.Productos;
import com.pruebaproyecto.app.repository.ProductosRepository;
import com.pruebaproyecto.app.service.ProductosService;
import com.pruebaproyecto.app.service.dto.ProductosDTO;
import com.pruebaproyecto.app.service.mapper.ProductosMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Productos}.
 */
@Service
@Transactional
public class ProductosServiceImpl implements ProductosService {

    private final Logger log = LoggerFactory.getLogger(ProductosServiceImpl.class);

    private final ProductosRepository productosRepository;

    private final ProductosMapper productosMapper;

    public ProductosServiceImpl(ProductosRepository productosRepository, ProductosMapper productosMapper) {
        this.productosRepository = productosRepository;
        this.productosMapper = productosMapper;
    }

    @Override
    public ProductosDTO save(ProductosDTO productosDTO) {
        log.debug("Request to save Productos : {}", productosDTO);
        Productos productos = productosMapper.toEntity(productosDTO);
        productos = productosRepository.save(productos);
        return productosMapper.toDto(productos);
    }

    @Override
    public ProductosDTO update(ProductosDTO productosDTO) {
        log.debug("Request to save Productos : {}", productosDTO);
        Productos productos = productosMapper.toEntity(productosDTO);
        productos = productosRepository.save(productos);
        return productosMapper.toDto(productos);
    }

    @Override
    public Optional<ProductosDTO> partialUpdate(ProductosDTO productosDTO) {
        log.debug("Request to partially update Productos : {}", productosDTO);

        return productosRepository
            .findById(productosDTO.getId())
            .map(existingProductos -> {
                productosMapper.partialUpdate(existingProductos, productosDTO);

                return existingProductos;
            })
            .map(productosRepository::save)
            .map(productosMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductosDTO> findAll() {
        log.debug("Request to get all Productos");
        return productosRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(productosMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ProductosDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productosRepository.findAllWithEagerRelationships(pageable).map(productosMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductosDTO> findOne(Long id) {
        log.debug("Request to get Productos : {}", id);
        return productosRepository.findOneWithEagerRelationships(id).map(productosMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Productos : {}", id);
        productosRepository.deleteById(id);
    }
}
