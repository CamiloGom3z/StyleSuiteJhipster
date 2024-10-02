package com.pruebaproyecto.app.service.impl;

import com.pruebaproyecto.app.domain.Resenia;
import com.pruebaproyecto.app.repository.ReseniaRepository;
import com.pruebaproyecto.app.service.ReseniaService;
import com.pruebaproyecto.app.service.dto.ReseniaDTO;
import com.pruebaproyecto.app.service.mapper.ReseniaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Resenia}.
 */
@Service
@Transactional
public class ReseniaServiceImpl implements ReseniaService {

    private final Logger log = LoggerFactory.getLogger(ReseniaServiceImpl.class);

    private final ReseniaRepository reseniaRepository;

    private final ReseniaMapper reseniaMapper;

    public ReseniaServiceImpl(ReseniaRepository reseniaRepository, ReseniaMapper reseniaMapper) {
        this.reseniaRepository = reseniaRepository;
        this.reseniaMapper = reseniaMapper;
    }

    @Override
    public ReseniaDTO save(ReseniaDTO reseniaDTO) {
        log.debug("Request to save Resenia : {}", reseniaDTO);
        Resenia resenia = reseniaMapper.toEntity(reseniaDTO);
        resenia = reseniaRepository.save(resenia);
        return reseniaMapper.toDto(resenia);
    }

    @Override
    public ReseniaDTO update(ReseniaDTO reseniaDTO) {
        log.debug("Request to save Resenia : {}", reseniaDTO);
        Resenia resenia = reseniaMapper.toEntity(reseniaDTO);
        resenia = reseniaRepository.save(resenia);
        return reseniaMapper.toDto(resenia);
    }

    @Override
    public Optional<ReseniaDTO> partialUpdate(ReseniaDTO reseniaDTO) {
        log.debug("Request to partially update Resenia : {}", reseniaDTO);

        return reseniaRepository
            .findById(reseniaDTO.getId())
            .map(existingResenia -> {
                reseniaMapper.partialUpdate(existingResenia, reseniaDTO);

                return existingResenia;
            })
            .map(reseniaRepository::save)
            .map(reseniaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReseniaDTO> findAll() {
        log.debug("Request to get all Resenias");
        return reseniaRepository.findAll().stream().map(reseniaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReseniaDTO> findOne(Long id) {
        log.debug("Request to get Resenia : {}", id);
        return reseniaRepository.findById(id).map(reseniaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Resenia : {}", id);
        reseniaRepository.deleteById(id);
    }
}
