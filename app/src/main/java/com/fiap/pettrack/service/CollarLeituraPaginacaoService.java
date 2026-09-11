package com.fiap.pettrack.service;

import com.fiap.pettrack.model.CollarLeitura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CollarLeituraPaginacaoService {

    @Autowired
    private CollarLeituraCachingService collarLeituraCachingService;


    @Transactional(readOnly = true)
    public Page<CollarLeitura> paginar(PageRequest pageRequest) {
        return collarLeituraCachingService.findAll(pageRequest);
    }
}