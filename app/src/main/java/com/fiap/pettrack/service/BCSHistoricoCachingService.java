package com.fiap.pettrack.service;

import com.fiap.pettrack.model.BCSHistorico;
import com.fiap.pettrack.repository.IBCSHistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BCSHistoricoCachingService {

    @Autowired
    private IBCSHistoricoRepository iBCSHistoricoRepository;

    @Cacheable(value = "retornarTodosBCSPaginados", key = "#pageRequest")
    public Page<BCSHistorico> findAll(PageRequest pageRequest) {
        return iBCSHistoricoRepository.findAll(pageRequest);
    }

    @Cacheable(value = "retornarTodosBCS")
    public List<BCSHistorico> findAll() {
        return iBCSHistoricoRepository.findAll();
    }

    @Cacheable(value = "retornarBCSPorId", key = "#id")
    public Optional<BCSHistorico> findById(Long id) {
        return iBCSHistoricoRepository.findById(id);
    }

    @Cacheable(value = "retornarHistoricoBCSPorPet", key = "#idPet")
    public List<BCSHistorico> buscarHistoricoPorPet(Long idPet) {
        return iBCSHistoricoRepository.buscarHistoricoPorPet(idPet);
    }

    @Cacheable(value = "retornarMediaBCSPorPet", key = "#idPet")
    public Double buscarMediaBcsPorPet(Long idPet) {
        return iBCSHistoricoRepository.buscarMediaBcsPorPet(idPet);
    }

    @CacheEvict(value = {"retornarTodosBCSPaginados", "retornarTodosBCS",
            "retornarBCSPorId", "retornarHistoricoBCSPorPet",
            "retornarMediaBCSPorPet"}, allEntries = true)
    public void removerCache() {
        System.out.println("Cache de BCSHistorico removido!");
    }
}