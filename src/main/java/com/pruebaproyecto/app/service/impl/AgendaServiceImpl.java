package com.pruebaproyecto.app.service.impl;

import com.pruebaproyecto.app.domain.Agenda;
import com.pruebaproyecto.app.repository.AgendaRepository;
import com.pruebaproyecto.app.service.AgendaService;
import com.pruebaproyecto.app.service.dto.AgendaDTO;
import com.pruebaproyecto.app.service.mapper.AgendaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Agenda}.
 */
@Service
@Transactional
public class AgendaServiceImpl implements AgendaService {

    private final Logger log = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private final AgendaRepository agendaRepository;

    private final AgendaMapper agendaMapper;

    public AgendaServiceImpl(AgendaRepository agendaRepository, AgendaMapper agendaMapper) {
        this.agendaRepository = agendaRepository;
        this.agendaMapper = agendaMapper;
    }

    @Override
    public AgendaDTO save(AgendaDTO agendaDTO) {
        log.debug("Request to save Agenda : {}", agendaDTO);
        Agenda agenda = agendaMapper.toEntity(agendaDTO);
        agenda = agendaRepository.save(agenda);
        return agendaMapper.toDto(agenda);
    }

    @Override
    public AgendaDTO update(AgendaDTO agendaDTO) {
        log.debug("Request to save Agenda : {}", agendaDTO);
        Agenda agenda = agendaMapper.toEntity(agendaDTO);
        agenda = agendaRepository.save(agenda);
        return agendaMapper.toDto(agenda);
    }

    @Override
    public Optional<AgendaDTO> partialUpdate(AgendaDTO agendaDTO) {
        log.debug("Request to partially update Agenda : {}", agendaDTO);

        return agendaRepository
            .findById(agendaDTO.getId())
            .map(existingAgenda -> {
                agendaMapper.partialUpdate(existingAgenda, agendaDTO);

                return existingAgenda;
            })
            .map(agendaRepository::save)
            .map(agendaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendaDTO> findAll() {
        log.debug("Request to get all Agenda");
        return agendaRepository.findAll().stream().map(agendaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AgendaDTO> findOne(Long id) {
        log.debug("Request to get Agenda : {}", id);
        return agendaRepository.findById(id).map(agendaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Agenda : {}", id);
        agendaRepository.deleteById(id);
    }
}
