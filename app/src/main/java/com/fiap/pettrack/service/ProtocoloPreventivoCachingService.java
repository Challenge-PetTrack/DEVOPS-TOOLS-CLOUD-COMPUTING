package com.fiap.pettrack.service;

import com.fiap.pettrack.model.ProtocoloPreventivo;
import com.fiap.pettrack.model.enums.TipoProtocoloPreventivoEnum;
import com.fiap.pettrack.repository.IProtocoloPreventivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProtocoloPreventivoCachingService {

    @Autowired
    private IProtocoloPreventivoRepository iProtocoloPreventivoRepository;

    @Cacheable(value = "retornarTodosProtocolosPaginados", key = "#pageRequest")
    public Page<ProtocoloPreventivo> findAll(PageRequest pageRequest) {
        return iProtocoloPreventivoRepository.findAll(pageRequest);
    }

    @Cacheable(value = "retornarTodosProtocolos")
    public List<ProtocoloPreventivo> findAll() {
        return iProtocoloPreventivoRepository.findAll();
    }

    @Cacheable(value = "retornarProtocoloPorId", key = "#id")
    public Optional<ProtocoloPreventivo> findById(Long id) {
        return iProtocoloPreventivoRepository.findById(id);
    }

    @Cacheable(value = "retornarProtocolosPorTipo", key = "#tipo")
    public List<ProtocoloPreventivo> findByTipo(TipoProtocoloPreventivoEnum tipo) {
        return iProtocoloPreventivoRepository.findByTipo(tipo);
    }

    @Cacheable(value = "retornarProtocolosPendentesOuAtrasados", key = "#idPet")
    public List<ProtocoloPreventivo> buscarPendentesOuAtrasadosPorPet(Long idPet) {
        return iProtocoloPreventivoRepository.buscarPendentesOuAtrasadosPorPet(idPet);
    }

    @CacheEvict(value = {"retornarTodosProtocolosPaginados", "retornarTodosProtocolos",
            "retornarProtocoloPorId", "retornarProtocolosPorTipo",
            "retornarProtocolosPendentesOuAtrasados"}, allEntries = true)
    public void removerCache() {
        System.out.println("Cache de ProtocoloPreventivo removido!");
    }
}