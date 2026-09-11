package com.fiap.pettrack.service;

import com.fiap.pettrack.model.Medicamento;
import com.fiap.pettrack.repository.IMedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicamentoCachingService {

    @Autowired
    private IMedicamentoRepository iMedicamentoRepository;

    @Cacheable(value = "retornarTodosMedicamentosPaginados", key = "#pageRequest")
    public Page<Medicamento> findAll(PageRequest pageRequest) {
        return iMedicamentoRepository.findAll(pageRequest);
    }

    @Cacheable(value = "retornarTodosMedicamentos")
    public List<Medicamento> findAll() {
        return iMedicamentoRepository.findAll();
    }

    @Cacheable(value = "retornarMedicamentoPorId", key = "#id")
    public Optional<Medicamento> findById(Long id) {
        return iMedicamentoRepository.findById(id);
    }

    @Cacheable(value = "retornarMedicamentosPorNome", key = "#nome")
    public List<Medicamento> findByNomeContainingIgnoreCase(String nome) {
        return iMedicamentoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Cacheable(value = "retornarMedicamentosAtivosPorPet", key = "#idPet")
    public List<Medicamento> buscarMedicamentosAtivosPorPet(Long idPet) {
        return iMedicamentoRepository.buscarMedicamentosAtivosPorPet(idPet);
    }

    @CacheEvict(value = {"retornarTodosMedicamentosPaginados", "retornarTodosMedicamentos",
            "retornarMedicamentoPorId", "retornarMedicamentosPorNome",
            "retornarMedicamentosAtivosPorPet"}, allEntries = true)
    public void removerCache() {
        System.out.println("Cache de Medicamento removido!");
    }
}