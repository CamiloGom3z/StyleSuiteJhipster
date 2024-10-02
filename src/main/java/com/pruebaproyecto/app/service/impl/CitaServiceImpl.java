package com.pruebaproyecto.app.service.impl;

import com.pruebaproyecto.app.domain.Cita;
import com.pruebaproyecto.app.repository.CitaRepository;
import com.pruebaproyecto.app.service.CitaService;
import com.pruebaproyecto.app.service.dto.CitaDTO;
import com.pruebaproyecto.app.service.mapper.CitaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cita}.
 */
@Service
@Transactional
public class CitaServiceImpl implements CitaService {

    private final Logger log = LoggerFactory.getLogger(CitaServiceImpl.class);

    private final CitaRepository citaRepository;

    private final CitaMapper citaMapper;

    public CitaServiceImpl(CitaRepository citaRepository, CitaMapper citaMapper) {
        this.citaRepository = citaRepository;
        this.citaMapper = citaMapper;
    }

    @Override
    public CitaDTO save(CitaDTO citaDTO) {
        log.debug("Request to save Cita : {}", citaDTO);
        Cita cita = citaMapper.toEntity(citaDTO);
        cita = citaRepository.save(cita);
        return citaMapper.toDto(cita);
    }

    @Override
    public CitaDTO update(CitaDTO citaDTO) {
        log.debug("Request to save Cita : {}", citaDTO);
        Cita cita = citaMapper.toEntity(citaDTO);
        cita = citaRepository.save(cita);
        return citaMapper.toDto(cita);
    }

    @Override
    public Optional<CitaDTO> partialUpdate(CitaDTO citaDTO) {
        log.debug("Request to partially update Cita : {}", citaDTO);

        return citaRepository
            .findById(citaDTO.getId())
            .map(existingCita -> {
                citaMapper.partialUpdate(existingCita, citaDTO);

                return existingCita;
            })
            .map(citaRepository::save)
            .map(citaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaDTO> findAll() {
        log.debug("Request to get all Citas");
        return citaRepository.findAll().stream().map(citaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CitaDTO> findOne(Long id) {
        log.debug("Request to get Cita : {}", id);
        return citaRepository.findById(id).map(citaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cita : {}", id);
        citaRepository.deleteById(id);
    }
}
