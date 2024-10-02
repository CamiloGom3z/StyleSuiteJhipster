package com.pruebaproyecto.app.service.impl;

import com.pruebaproyecto.app.domain.Pago;
import com.pruebaproyecto.app.repository.PagoRepository;
import com.pruebaproyecto.app.service.PagoService;
import com.pruebaproyecto.app.service.dto.PagoDTO;
import com.pruebaproyecto.app.service.mapper.PagoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pago}.
 */
@Service
@Transactional
public class PagoServiceImpl implements PagoService {

    private final Logger log = LoggerFactory.getLogger(PagoServiceImpl.class);

    private final PagoRepository pagoRepository;

    private final PagoMapper pagoMapper;

    public PagoServiceImpl(PagoRepository pagoRepository, PagoMapper pagoMapper) {
        this.pagoRepository = pagoRepository;
        this.pagoMapper = pagoMapper;
    }

    @Override
    public PagoDTO save(PagoDTO pagoDTO) {
        log.debug("Request to save Pago : {}", pagoDTO);
        Pago pago = pagoMapper.toEntity(pagoDTO);
        pago = pagoRepository.save(pago);
        return pagoMapper.toDto(pago);
    }

    @Override
    public PagoDTO update(PagoDTO pagoDTO) {
        log.debug("Request to save Pago : {}", pagoDTO);
        Pago pago = pagoMapper.toEntity(pagoDTO);
        pago = pagoRepository.save(pago);
        return pagoMapper.toDto(pago);
    }

    @Override
    public Optional<PagoDTO> partialUpdate(PagoDTO pagoDTO) {
        log.debug("Request to partially update Pago : {}", pagoDTO);

        return pagoRepository
            .findById(pagoDTO.getId())
            .map(existingPago -> {
                pagoMapper.partialUpdate(existingPago, pagoDTO);

                return existingPago;
            })
            .map(pagoRepository::save)
            .map(pagoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> findAll() {
        log.debug("Request to get all Pagos");
        return pagoRepository.findAll().stream().map(pagoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PagoDTO> findOne(Long id) {
        log.debug("Request to get Pago : {}", id);
        return pagoRepository.findById(id).map(pagoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pago : {}", id);
        pagoRepository.deleteById(id);
    }
}
