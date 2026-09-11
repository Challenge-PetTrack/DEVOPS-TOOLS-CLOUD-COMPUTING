package com.fiap.pettrack.service;

import com.fiap.pettrack.model.AdesaoMedicamento;
import com.fiap.pettrack.model.enums.SimNaoEnum;
import com.fiap.pettrack.repository.IAdesaoMedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdesaoMedicamentoCachingService {

    @Autowired
    private IAdesaoMedicamentoRepository iAdesaoMedicamentoRepository;

    @Cacheable(value = "retornarTodasAdesoesPaginadas", key = "#pageRequest")
    public Page<AdesaoMedicamento> findAll(PageRequest pageRequest) {
        return iAdesaoMedicamentoRepository.findAll(pageRequest);
    }

    @Cacheable(value = "retornarTodasAdesoes")
    public List<AdesaoMedicamento> findAll() {
        return iAdesaoMedicamentoRepository.findAll();
    }

    @Cacheable(value = "retornarAdesaoPorId", key = "#id")
    public Optional<AdesaoMedicamento> findById(Long id) {
        return iAdesaoMedicamentoRepository.findById(id);
    }

    @Cacheable(value = "retornarAdesoesPorMedicamento", key = "#idMedicamento")
    public List<AdesaoMedicamento> findByMedicamentoId(Long idMedicamento) {
        return iAdesaoMedicamentoRepository.findByMedicamentoId(idMedicamento);
    }

    @Cacheable(value = "retornarAdesoesPorStatus", key = "#status")
    public List<AdesaoMedicamento> findByStatus(SimNaoEnum status) {
        return iAdesaoMedicamentoRepository.findByStatus(status);
    }

    @CacheEvict(value = {"retornarTodasAdesoesPaginadas", "retornarTodasAdesoes",
            "retornarAdesaoPorId", "retornarAdesoesPorMedicamento",
            "retornarAdesoesPorStatus"}, allEntries = true)
    public void removerCache() {
        System.out.println("Cache de AdesaoMedicamento removido!");
    }
}
