package com.fiap.pettrack.service;

import com.fiap.pettrack.model.ScoreHistorico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScoreHistoricoPaginacaoService {

    @Autowired
    private ScoreHistoricoCachingService scoreHistoricoCachingService;

    @Transactional(readOnly = true)
    public Page<ScoreHistorico> paginar(PageRequest pageRequest) {
        return scoreHistoricoCachingService.findAll(pageRequest);
    }
}