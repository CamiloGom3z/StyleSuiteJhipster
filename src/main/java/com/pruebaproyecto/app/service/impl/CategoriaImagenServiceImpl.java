package com.pruebaproyecto.app.service.impl;

import com.pruebaproyecto.app.domain.CategoriaImagen;
import com.pruebaproyecto.app.repository.CategoriaImagenRepository;
import com.pruebaproyecto.app.service.CategoriaImagenService;
import com.pruebaproyecto.app.service.dto.CategoriaImagenDTO;
import com.pruebaproyecto.app.service.mapper.CategoriaImagenMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CategoriaImagen}.
 */
@Service
@Transactional
public class CategoriaImagenServiceImpl implements CategoriaImagenService {

    private final Logger log = LoggerFactory.getLogger(CategoriaImagenServiceImpl.class);

    private final CategoriaImagenRepository categoriaImagenRepository;

    private final CategoriaImagenMapper categoriaImagenMapper;

    public CategoriaImagenServiceImpl(CategoriaImagenRepository categoriaImagenRepository, CategoriaImagenMapper categoriaImagenMapper) {
        this.categoriaImagenRepository = categoriaImagenRepository;
        this.categoriaImagenMapper = categoriaImagenMapper;
    }

    @Override
    public CategoriaImagenDTO save(CategoriaImagenDTO categoriaImagenDTO) {
        log.debug("Request to save CategoriaImagen : {}", categoriaImagenDTO);
        CategoriaImagen categoriaImagen = categoriaImagenMapper.toEntity(categoriaImagenDTO);
        categoriaImagen = categoriaImagenRepository.save(categoriaImagen);
        return categoriaImagenMapper.toDto(categoriaImagen);
    }

    @Override
    public CategoriaImagenDTO update(CategoriaImagenDTO categoriaImagenDTO) {
        log.debug("Request to save CategoriaImagen : {}", categoriaImagenDTO);
        CategoriaImagen categoriaImagen = categoriaImagenMapper.toEntity(categoriaImagenDTO);
        categoriaImagen = categoriaImagenRepository.save(categoriaImagen);
        return categoriaImagenMapper.toDto(categoriaImagen);
    }

    @Override
    public Optional<CategoriaImagenDTO> partialUpdate(CategoriaImagenDTO categoriaImagenDTO) {
        log.debug("Request to partially update CategoriaImagen : {}", categoriaImagenDTO);

        return categoriaImagenRepository
            .findById(categoriaImagenDTO.getId())
            .map(existingCategoriaImagen -> {
                categoriaImagenMapper.partialUpdate(existingCategoriaImagen, categoriaImagenDTO);

                return existingCategoriaImagen;
            })
            .map(categoriaImagenRepository::save)
            .map(categoriaImagenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaImagenDTO> findAll() {
        log.debug("Request to get all CategoriaImagens");
        return categoriaImagenRepository
            .findAll()
            .stream()
            .map(categoriaImagenMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaImagenDTO> findOne(Long id) {
        log.debug("Request to get CategoriaImagen : {}", id);
        return categoriaImagenRepository.findById(id).map(categoriaImagenMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CategoriaImagen : {}", id);
        categoriaImagenRepository.deleteById(id);
    }
}
