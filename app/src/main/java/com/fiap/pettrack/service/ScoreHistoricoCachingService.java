package com.fiap.pettrack.service;

import com.fiap.pettrack.model.ScoreHistorico;
import com.fiap.pettrack.repository.IScoreHistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreHistoricoCachingService {

    @Autowired
    private IScoreHistoricoRepository iScoreHistoricoRepository;

    @Cacheable(value = "retornarTodosScoresPaginados", key = "#pageRequest")
    public Page<ScoreHistorico> findAll(PageRequest pageRequest) {
        return iScoreHistoricoRepository.findAll(pageRequest);
    }

    @Cacheable(value = "retornarTodosScores")
    public List<ScoreHistorico> findAll() {
        return iScoreHistoricoRepository.findAll();
    }

    @Cacheable(value = "retornarScorePorId", key = "#id")
    public Optional<ScoreHistorico> findById(Long id) {
        return iScoreHistoricoRepository.findById(id);
    }

    @Cacheable(value = "retornarHistoricoPorPet", key = "#idPet")
    public List<ScoreHistorico> buscarHistoricoPorPet(Long idPet) {
        return iScoreHistoricoRepository.buscarHistoricoPorPet(idPet);
    }

    @Cacheable(value = "retornarMediaScorePorPet", key = "#idPet")
    public Double buscarMediaScorePorPet(Long idPet) {
        return iScoreHistoricoRepository.buscarMediaScorePorPet(idPet);
    }

    @CacheEvict(value = {"retornarTodosScoresPaginados", "retornarTodosScores",
            "retornarScorePorId", "retornarHistoricoPorPet",
            "retornarMediaScorePorPet"}, allEntries = true)
    public void removerCache() {
        System.out.println("Cache de ScoreHistorico removido!");
    }
}