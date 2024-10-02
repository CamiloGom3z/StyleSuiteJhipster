package com.pruebaproyecto.app.service.impl;

import com.pruebaproyecto.app.domain.Promocion;
import com.pruebaproyecto.app.repository.PromocionRepository;
import com.pruebaproyecto.app.service.PromocionService;
import com.pruebaproyecto.app.service.dto.PromocionDTO;
import com.pruebaproyecto.app.service.mapper.PromocionMapper;
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
 * Service Implementation for managing {@link Promocion}.
 */
@Service
@Transactional
public class PromocionServiceImpl implements PromocionService {

    private final Logger log = LoggerFactory.getLogger(PromocionServiceImpl.class);

    private final PromocionRepository promocionRepository;

    private final PromocionMapper promocionMapper;

    public PromocionServiceImpl(PromocionRepository promocionRepository, PromocionMapper promocionMapper) {
        this.promocionRepository = promocionRepository;
        this.promocionMapper = promocionMapper;
    }

    @Override
    public PromocionDTO save(PromocionDTO promocionDTO) {
        log.debug("Request to save Promocion : {}", promocionDTO);
        Promocion promocion = promocionMapper.toEntity(promocionDTO);
        promocion = promocionRepository.save(promocion);
        return promocionMapper.toDto(promocion);
    }

    @Override
    public PromocionDTO update(PromocionDTO promocionDTO) {
        log.debug("Request to save Promocion : {}", promocionDTO);
        Promocion promocion = promocionMapper.toEntity(promocionDTO);
        promocion = promocionRepository.save(promocion);
        return promocionMapper.toDto(promocion);
    }

    @Override
    public Optional<PromocionDTO> partialUpdate(PromocionDTO promocionDTO) {
        log.debug("Request to partially update Promocion : {}", promocionDTO);

        return promocionRepository
            .findById(promocionDTO.getId())
            .map(existingPromocion -> {
                promocionMapper.partialUpdate(existingPromocion, promocionDTO);

                return existingPromocion;
            })
            .map(promocionRepository::save)
            .map(promocionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromocionDTO> findAll() {
        log.debug("Request to get all Promocions");
        return promocionRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(promocionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<PromocionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return promocionRepository.findAllWithEagerRelationships(pageable).map(promocionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PromocionDTO> findOne(Long id) {
        log.debug("Request to get Promocion : {}", id);
        return promocionRepository.findOneWithEagerRelationships(id).map(promocionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Promocion : {}", id);
        promocionRepository.deleteById(id);
    }
}
