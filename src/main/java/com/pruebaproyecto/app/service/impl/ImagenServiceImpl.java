package com.pruebaproyecto.app.service.impl;

import com.pruebaproyecto.app.domain.Imagen;
import com.pruebaproyecto.app.repository.ImagenRepository;
import com.pruebaproyecto.app.service.ImagenService;
import com.pruebaproyecto.app.service.dto.ImagenDTO;
import com.pruebaproyecto.app.service.mapper.ImagenMapper;
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
 * Service Implementation for managing {@link Imagen}.
 */
@Service
@Transactional
public class ImagenServiceImpl implements ImagenService {

    private final Logger log = LoggerFactory.getLogger(ImagenServiceImpl.class);

    private final ImagenRepository imagenRepository;

    private final ImagenMapper imagenMapper;

    public ImagenServiceImpl(ImagenRepository imagenRepository, ImagenMapper imagenMapper) {
        this.imagenRepository = imagenRepository;
        this.imagenMapper = imagenMapper;
    }

    @Override
    public ImagenDTO save(ImagenDTO imagenDTO) {
        log.debug("Request to save Imagen : {}", imagenDTO);
        Imagen imagen = imagenMapper.toEntity(imagenDTO);
        imagen = imagenRepository.save(imagen);
        return imagenMapper.toDto(imagen);
    }

    @Override
    public ImagenDTO update(ImagenDTO imagenDTO) {
        log.debug("Request to save Imagen : {}", imagenDTO);
        Imagen imagen = imagenMapper.toEntity(imagenDTO);
        imagen = imagenRepository.save(imagen);
        return imagenMapper.toDto(imagen);
    }

    @Override
    public Optional<ImagenDTO> partialUpdate(ImagenDTO imagenDTO) {
        log.debug("Request to partially update Imagen : {}", imagenDTO);

        return imagenRepository
            .findById(imagenDTO.getId())
            .map(existingImagen -> {
                imagenMapper.partialUpdate(existingImagen, imagenDTO);

                return existingImagen;
            })
            .map(imagenRepository::save)
            .map(imagenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImagenDTO> findAll() {
        log.debug("Request to get all Imagens");
        return imagenRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(imagenMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ImagenDTO> findAllWithEagerRelationships(Pageable pageable) {
        return imagenRepository.findAllWithEagerRelationships(pageable).map(imagenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImagenDTO> findOne(Long id) {
        log.debug("Request to get Imagen : {}", id);
        return imagenRepository.findOneWithEagerRelationships(id).map(imagenMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Imagen : {}", id);
        imagenRepository.deleteById(id);
    }
}
