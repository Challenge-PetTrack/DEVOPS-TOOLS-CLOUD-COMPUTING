package com.fiap.pettrack.service;

import com.fiap.pettrack.model.BCSHistorico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BCSHistoricoPaginacaoService {

    @Autowired
    private BCSHistoricoCachingService bcsHistoricoCachingService;


    @Transactional(readOnly = true)
    public Page<BCSHistorico> paginar(PageRequest pageRequest) {
        return bcsHistoricoCachingService.findAll(pageRequest);
    }
}