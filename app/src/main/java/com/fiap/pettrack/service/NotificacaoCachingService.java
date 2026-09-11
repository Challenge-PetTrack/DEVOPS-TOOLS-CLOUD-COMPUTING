package com.fiap.pettrack.service;

import com.fiap.pettrack.model.Notificacao;
import com.fiap.pettrack.model.enums.SimNaoEnum;
import com.fiap.pettrack.model.enums.TipoNotificacaoEnum;
import com.fiap.pettrack.repository.INotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacaoCachingService {

    @Autowired
    private INotificacaoRepository iNotificacaoRepository;

    @Cacheable(value = "retornarTodasNotificacoesPaginadas", key = "#pageRequest")
    public Page<Notificacao> findAll(PageRequest pageRequest) {
        return iNotificacaoRepository.findAll(pageRequest);
    }

    @Cacheable(value = "retornarTodasNotificacoes")
    public List<Notificacao> findAll() {
        return iNotificacaoRepository.findAll();
    }

    @Cacheable(value = "retornarNotificacaoPorId", key = "#id")
    public Optional<Notificacao> findById(Long id) {
        return iNotificacaoRepository.findById(id);
    }

    @Cacheable(value = "retornarNotificacoesPorStatus", key = "#status")
    public List<Notificacao> findByStatus(SimNaoEnum status) {
        return iNotificacaoRepository.findByStatus(status);
    }

    @Cacheable(value = "retornarNotificacoesPorTipo", key = "#tipo")
    public List<Notificacao> findByTipo(TipoNotificacaoEnum tipo) {
        return iNotificacaoRepository.findByTipo(tipo);
    }

    @Cacheable(value = "retornarUrgentesNaoLidasPorTutor", key = "#idTutor")
    public List<Notificacao> buscarUrgentesNaoLidasPorTutor(Long idTutor) {
        return iNotificacaoRepository.buscarUrgentesNaoLidasPorTutor(idTutor);
    }

    @CacheEvict(value = {"retornarTodasNotificacoesPaginadas", "retornarTodasNotificacoes",
            "retornarNotificacaoPorId", "retornarNotificacoesPorStatus",
            "retornarNotificacoesPorTipo", "retornarUrgentesNaoLidasPorTutor"}, allEntries = true)
    public void removerCache() {
        System.out.println("Cache de Notificacao removido!");
    }
}