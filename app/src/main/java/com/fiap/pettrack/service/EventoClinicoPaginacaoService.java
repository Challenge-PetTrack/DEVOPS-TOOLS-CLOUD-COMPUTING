package com.fiap.pettrack.service;

import com.fiap.pettrack.model.EventoClinico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventoClinicoPaginacaoService {

    @Autowired
    private EventoClinicoCachingService eventoClinicoCachingService;


    @Transactional(readOnly = true)
    public Page<EventoClinico> paginar(PageRequest pageRequest) {
        return eventoClinicoCachingService.findAll(pageRequest);
    }
}