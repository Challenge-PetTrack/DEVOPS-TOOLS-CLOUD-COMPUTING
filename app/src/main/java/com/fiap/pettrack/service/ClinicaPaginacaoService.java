package com.fiap.pettrack.service;

import com.fiap.pettrack.model.Clinica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClinicaPaginacaoService {

    @Autowired
    private ClinicaCachingService clinicaCachingService;


    @Transactional(readOnly = true)
    public Page<Clinica> paginar(PageRequest pageRequest) {
        return clinicaCachingService.findAll(pageRequest);
    }
}