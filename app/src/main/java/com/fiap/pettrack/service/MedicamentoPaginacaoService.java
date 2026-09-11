package com.fiap.pettrack.service;


import com.fiap.pettrack.model.Medicamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicamentoPaginacaoService {

    @Autowired
    private MedicamentoCachingService medicamentoCachingService;


    @Transactional(readOnly = true)
    public Page<Medicamento> paginar(PageRequest pageRequest) {
        return medicamentoCachingService.findAll(pageRequest);
    }
}