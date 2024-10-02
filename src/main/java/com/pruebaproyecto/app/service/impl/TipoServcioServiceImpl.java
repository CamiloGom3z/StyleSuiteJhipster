package com.pruebaproyecto.app.service.impl;

import com.pruebaproyecto.app.domain.TipoServcio;
import com.pruebaproyecto.app.repository.TipoServcioRepository;
import com.pruebaproyecto.app.service.TipoServcioService;
import com.pruebaproyecto.app.service.dto.TipoServcioDTO;
import com.pruebaproyecto.app.service.mapper.TipoServcioMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TipoServcio}.
 */
@Service
@Transactional
public class TipoServcioServiceImpl implements TipoServcioService {

    private final Logger log = LoggerFactory.getLogger(TipoServcioServiceImpl.class);

    private final TipoServcioRepository tipoServcioRepository;

    private final TipoServcioMapper tipoServcioMapper;

    public TipoServcioServiceImpl(TipoServcioRepository tipoServcioRepository, TipoServcioMapper tipoServcioMapper) {
        this.tipoServcioRepository = tipoServcioRepository;
        this.tipoServcioMapper = tipoServcioMapper;
    }

    @Override
    public TipoServcioDTO save(TipoServcioDTO tipoServcioDTO) {
        log.debug("Request to save TipoServcio : {}", tipoServcioDTO);
        TipoServcio tipoServcio = tipoServcioMapper.toEntity(tipoServcioDTO);
        tipoServcio = tipoServcioRepository.save(tipoServcio);
        return tipoServcioMapper.toDto(tipoServcio);
    }

    @Override
    public TipoServcioDTO update(TipoServcioDTO tipoServcioDTO) {
        log.debug("Request to save TipoServcio : {}", tipoServcioDTO);
        TipoServcio tipoServcio = tipoServcioMapper.toEntity(tipoServcioDTO);
        tipoServcio = tipoServcioRepository.save(tipoServcio);
        return tipoServcioMapper.toDto(tipoServcio);
    }

    @Override
    public Optional<TipoServcioDTO> partialUpdate(TipoServcioDTO tipoServcioDTO) {
        log.debug("Request to partially update TipoServcio : {}", tipoServcioDTO);

        return tipoServcioRepository
            .findById(tipoServcioDTO.getId())
            .map(existingTipoServcio -> {
                tipoServcioMapper.partialUpdate(existingTipoServcio, tipoServcioDTO);

                return existingTipoServcio;
            })
            .map(tipoServcioRepository::save)
            .map(tipoServcioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoServcioDTO> findAll() {
        log.debug("Request to get all TipoServcios");
        return tipoServcioRepository.findAll().stream().map(tipoServcioMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoServcioDTO> findOne(Long id) {
        log.debug("Request to get TipoServcio : {}", id);
        return tipoServcioRepository.findById(id).map(tipoServcioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoServcio : {}", id);
        tipoServcioRepository.deleteById(id);
    }
}
