package com.fiap.pettrack.service;

import com.fiap.pettrack.model.Alerta;
import com.fiap.pettrack.model.enums.TipoAlertaEnum;
import com.fiap.pettrack.repository.IAlertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertaCachingService {

    @Autowired
    private IAlertaRepository iAlertaRepository;

    @Cacheable(value = "retornarTodosAlertasPaginados", key = "#pageRequest")
    public Page<Alerta> findAll(PageRequest pageRequest) {
        return iAlertaRepository.findAll(pageRequest);
    }

    @Cacheable(value = "retornarTodosAlertas")
    public List<Alerta> findAll() {
        return iAlertaRepository.findAll();
    }

    @Cacheable(value = "retornarAlertaPorId", key = "#id")
    public Optional<Alerta> findById(Long id) {
        return iAlertaRepository.findById(id);
    }

    @Cacheable(value = "retornarAlertasPorTipo", key = "#tipoAlerta")
    public List<Alerta> findByTipoAlerta(TipoAlertaEnum tipoAlerta) {
        return iAlertaRepository.findByTipoAlerta(tipoAlerta);
    }

    @Cacheable(value = "retornarAlertasPendentesPorPet", key = "#idPet")
    public List<Alerta> buscarPendentesPorPet(Long idPet) {
        return iAlertaRepository.buscarPendentesPorPet(idPet);
    }

    @CacheEvict(value = {"retornarTodosAlertasPaginados", "retornarTodosAlertas",
            "retornarAlertaPorId", "retornarAlertasPorTipo",
            "retornarAlertasPendentesPorPet"}, allEntries = true)
    public void removerCache() {
        System.out.println("Cache de Alerta removido!");
    }
}