package com.fiap.pettrack.service;

import com.fiap.pettrack.model.ProtocoloPreventivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProtocoloPreventivoPaginacaoService {

    @Autowired
    private ProtocoloPreventivoCachingService protocoloPreventivoCachingService;

    @Transactional(readOnly = true)
    public Page<ProtocoloPreventivo> paginar(PageRequest pageRequest) {
        return protocoloPreventivoCachingService.findAll(pageRequest);
    }
}