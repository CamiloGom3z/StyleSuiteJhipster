package com.pruebaproyecto.app.service.impl;

import com.pruebaproyecto.app.domain.Servicio;
import com.pruebaproyecto.app.repository.ServicioRepository;
import com.pruebaproyecto.app.service.ServicioService;
import com.pruebaproyecto.app.service.dto.ServicioDTO;
import com.pruebaproyecto.app.service.mapper.ServicioMapper;
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
 * Service Implementation for managing {@link Servicio}.
 */
@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

    private final Logger log = LoggerFactory.getLogger(ServicioServiceImpl.class);

    private final ServicioRepository servicioRepository;

    private final ServicioMapper servicioMapper;

    public ServicioServiceImpl(ServicioRepository servicioRepository, ServicioMapper servicioMapper) {
        this.servicioRepository = servicioRepository;
        this.servicioMapper = servicioMapper;
    }

    @Override
    public ServicioDTO save(ServicioDTO servicioDTO) {
        log.debug("Request to save Servicio : {}", servicioDTO);
        Servicio servicio = servicioMapper.toEntity(servicioDTO);
        servicio = servicioRepository.save(servicio);
        return servicioMapper.toDto(servicio);
    }

    @Override
    public ServicioDTO update(ServicioDTO servicioDTO) {
        log.debug("Request to save Servicio : {}", servicioDTO);
        Servicio servicio = servicioMapper.toEntity(servicioDTO);
        servicio = servicioRepository.save(servicio);
        return servicioMapper.toDto(servicio);
    }

    @Override
    public Optional<ServicioDTO> partialUpdate(ServicioDTO servicioDTO) {
        log.debug("Request to partially update Servicio : {}", servicioDTO);

        return servicioRepository
            .findById(servicioDTO.getId())
            .map(existingServicio -> {
                servicioMapper.partialUpdate(existingServicio, servicioDTO);

                return existingServicio;
            })
            .map(servicioRepository::save)
            .map(servicioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioDTO> findAll() {
        log.debug("Request to get all Servicios");
        return servicioRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(servicioMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ServicioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return servicioRepository.findAllWithEagerRelationships(pageable).map(servicioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServicioDTO> findOne(Long id) {
        log.debug("Request to get Servicio : {}", id);
        return servicioRepository.findOneWithEagerRelationships(id).map(servicioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Servicio : {}", id);
        servicioRepository.deleteById(id);
    }
}
