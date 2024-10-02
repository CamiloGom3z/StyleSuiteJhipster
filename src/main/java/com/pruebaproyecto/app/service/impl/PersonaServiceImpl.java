package com.pruebaproyecto.app.service.impl;

import com.pruebaproyecto.app.domain.Persona;
import com.pruebaproyecto.app.repository.PersonaRepository;
import com.pruebaproyecto.app.service.PersonaService;
import com.pruebaproyecto.app.service.dto.PersonaDTO;
import com.pruebaproyecto.app.service.mapper.PersonaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Persona}.
 */
@Service
@Transactional
public class PersonaServiceImpl implements PersonaService {

    private final Logger log = LoggerFactory.getLogger(PersonaServiceImpl.class);

    private final PersonaRepository personaRepository;

    private final PersonaMapper personaMapper;

    public PersonaServiceImpl(PersonaRepository personaRepository, PersonaMapper personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    @Override
    public PersonaDTO save(PersonaDTO personaDTO) {
        log.debug("Request to save Persona : {}", personaDTO);
        Persona persona = personaMapper.toEntity(personaDTO);
        persona = personaRepository.save(persona);
        return personaMapper.toDto(persona);
    }

    @Override
    public PersonaDTO update(PersonaDTO personaDTO) {
        log.debug("Request to save Persona : {}", personaDTO);
        Persona persona = personaMapper.toEntity(personaDTO);
        persona = personaRepository.save(persona);
        return personaMapper.toDto(persona);
    }

    @Override
    public Optional<PersonaDTO> partialUpdate(PersonaDTO personaDTO) {
        log.debug("Request to partially update Persona : {}", personaDTO);

        return personaRepository
            .findById(personaDTO.getId())
            .map(existingPersona -> {
                personaMapper.partialUpdate(existingPersona, personaDTO);

                return existingPersona;
            })
            .map(personaRepository::save)
            .map(personaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaDTO> findAll() {
        log.debug("Request to get all Personas");
        return personaRepository.findAll().stream().map(personaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonaDTO> findOne(Long id) {
        log.debug("Request to get Persona : {}", id);
        return personaRepository.findById(id).map(personaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Persona : {}", id);
        personaRepository.deleteById(id);
    }
}
