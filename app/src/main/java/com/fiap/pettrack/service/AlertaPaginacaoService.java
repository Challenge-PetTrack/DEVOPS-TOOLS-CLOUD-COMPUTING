package com.fiap.pettrack.service;


import com.fiap.pettrack.model.Alerta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertaPaginacaoService {

    @Autowired
    private AlertaCachingService alertaCachingService;


    @Transactional(readOnly = true)
    public Page<Alerta> paginar(PageRequest pageRequest) {
        return alertaCachingService.findAll(pageRequest);
    }
}
