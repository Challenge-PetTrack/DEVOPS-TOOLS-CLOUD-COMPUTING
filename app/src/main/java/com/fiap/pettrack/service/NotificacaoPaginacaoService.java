package com.fiap.pettrack.service;

import com.fiap.pettrack.model.Notificacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificacaoPaginacaoService {

    @Autowired
    private NotificacaoCachingService notificacaoCachingService;



    @Transactional(readOnly = true)
    public Page<Notificacao> paginar(PageRequest pageRequest) {
        return notificacaoCachingService.findAll(pageRequest);
    }
}