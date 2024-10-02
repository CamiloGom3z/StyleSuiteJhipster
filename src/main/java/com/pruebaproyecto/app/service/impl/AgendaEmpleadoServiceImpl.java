package com.pruebaproyecto.app.service.impl;

import com.pruebaproyecto.app.domain.AgendaEmpleado;
import com.pruebaproyecto.app.repository.AgendaEmpleadoRepository;
import com.pruebaproyecto.app.service.AgendaEmpleadoService;
import com.pruebaproyecto.app.service.dto.AgendaEmpleadoDTO;
import com.pruebaproyecto.app.service.mapper.AgendaEmpleadoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AgendaEmpleado}.
 */
@Service
@Transactional
public class AgendaEmpleadoServiceImpl implements AgendaEmpleadoService {

    private final Logger log = LoggerFactory.getLogger(AgendaEmpleadoServiceImpl.class);

    private final AgendaEmpleadoRepository agendaEmpleadoRepository;

    private final AgendaEmpleadoMapper agendaEmpleadoMapper;

    public AgendaEmpleadoServiceImpl(AgendaEmpleadoRepository agendaEmpleadoRepository, AgendaEmpleadoMapper agendaEmpleadoMapper) {
        this.agendaEmpleadoRepository = agendaEmpleadoRepository;
        this.agendaEmpleadoMapper = agendaEmpleadoMapper;
    }

    @Override
    public AgendaEmpleadoDTO save(AgendaEmpleadoDTO agendaEmpleadoDTO) {
        log.debug("Request to save AgendaEmpleado : {}", agendaEmpleadoDTO);
        AgendaEmpleado agendaEmpleado = agendaEmpleadoMapper.toEntity(agendaEmpleadoDTO);
        agendaEmpleado = agendaEmpleadoRepository.save(agendaEmpleado);
        return agendaEmpleadoMapper.toDto(agendaEmpleado);
    }

    @Override
    public AgendaEmpleadoDTO update(AgendaEmpleadoDTO agendaEmpleadoDTO) {
        log.debug("Request to save AgendaEmpleado : {}", agendaEmpleadoDTO);
        AgendaEmpleado agendaEmpleado = agendaEmpleadoMapper.toEntity(agendaEmpleadoDTO);
        agendaEmpleado = agendaEmpleadoRepository.save(agendaEmpleado);
        return agendaEmpleadoMapper.toDto(agendaEmpleado);
    }

    @Override
    public Optional<AgendaEmpleadoDTO> partialUpdate(AgendaEmpleadoDTO agendaEmpleadoDTO) {
        log.debug("Request to partially update AgendaEmpleado : {}", agendaEmpleadoDTO);

        return agendaEmpleadoRepository
            .findById(agendaEmpleadoDTO.getId())
            .map(existingAgendaEmpleado -> {
                agendaEmpleadoMapper.partialUpdate(existingAgendaEmpleado, agendaEmpleadoDTO);

                return existingAgendaEmpleado;
            })
            .map(agendaEmpleadoRepository::save)
            .map(agendaEmpleadoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendaEmpleadoDTO> findAll() {
        log.debug("Request to get all AgendaEmpleados");
        return agendaEmpleadoRepository
            .findAll()
            .stream()
            .map(agendaEmpleadoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the agendaEmpleados where Cita is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AgendaEmpleadoDTO> findAllWhereCitaIsNull() {
        log.debug("Request to get all agendaEmpleados where Cita is null");
        return StreamSupport
            .stream(agendaEmpleadoRepository.findAll().spliterator(), false)
            .filter(agendaEmpleado -> agendaEmpleado.getCita() == null)
            .map(agendaEmpleadoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AgendaEmpleadoDTO> findOne(Long id) {
        log.debug("Request to get AgendaEmpleado : {}", id);
        return agendaEmpleadoRepository.findById(id).map(agendaEmpleadoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AgendaEmpleado : {}", id);
        agendaEmpleadoRepository.deleteById(id);
    }
}
