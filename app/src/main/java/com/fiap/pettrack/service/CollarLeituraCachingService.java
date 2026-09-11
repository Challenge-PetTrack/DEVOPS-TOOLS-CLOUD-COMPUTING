package com.fiap.pettrack.service;

import com.fiap.pettrack.model.CollarLeitura;
import com.fiap.pettrack.repository.ICollarLeituraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollarLeituraCachingService {

    @Autowired
    private ICollarLeituraRepository iCollarLeituraRepository;

    @Cacheable(value = "retornarTodasLeitulasPaginadas", key = "#pageRequest")
    public Page<CollarLeitura> findAll(PageRequest pageRequest) {
        return iCollarLeituraRepository.findAll(pageRequest);
    }

    @Cacheable(value = "retornarTodasLeituras")
    public List<CollarLeitura> findAll() {
        return iCollarLeituraRepository.findAll();
    }

    @Cacheable(value = "retornarLeituraPorId", key = "#id")
    public Optional<CollarLeitura> findById(Long id) {
        return iCollarLeituraRepository.findById(id);
    }

    @Cacheable(value = "retornarLeiturasPorPetETemperatura", key = "#idPet + #temperatura")
    public List<CollarLeitura> buscarPorPetETemperaturaAcimaDe(Long idPet, Double temperatura) {
        return iCollarLeituraRepository.buscarPorPetETemperaturaAcimaDe(idPet, temperatura);
    }

    @Cacheable(value = "retornarUltimaLeituraPorPet", key = "#idPet")
    public CollarLeitura buscarUltimaLeituraPorPet(Long idPet) {
        return iCollarLeituraRepository.buscarUltimaLeituraPorPet(idPet);
    }

    @CacheEvict(value = {"retornarTodasLeitulasPaginadas", "retornarTodasLeituras",
            "retornarLeituraPorId", "retornarLeiturasPorPetETemperatura",
            "retornarUltimaLeituraPorPet"}, allEntries = true)
    public void removerCache() {
        System.out.println("Cache de CollarLeitura removido!");
    }
}