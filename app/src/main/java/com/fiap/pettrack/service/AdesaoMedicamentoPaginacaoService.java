package com.fiap.pettrack.service;

import com.fiap.pettrack.model.AdesaoMedicamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdesaoMedicamentoPaginacaoService {

    @Autowired
    private AdesaoMedicamentoCachingService adesaoMedicamentoCachingService;

    @Transactional(readOnly = true)
    public Page<AdesaoMedicamento> paginar(PageRequest pageRequest) {
        return adesaoMedicamentoCachingService.findAll(pageRequest);
    }
}