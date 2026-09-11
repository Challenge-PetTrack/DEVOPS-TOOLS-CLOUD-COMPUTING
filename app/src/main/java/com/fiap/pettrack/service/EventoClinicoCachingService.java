package com.fiap.pettrack.service;

import com.fiap.pettrack.model.EventoClinico;
import com.fiap.pettrack.model.enums.TipoEventoClinicoEnum;
import com.fiap.pettrack.repository.IEventoClinicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoClinicoCachingService {

    @Autowired
    private IEventoClinicoRepository iEventoClinicoRepository;

    @Cacheable(value = "retornarTodosEventosPaginados", key = "#pageRequest")
    public Page<EventoClinico> findAll(PageRequest pageRequest) {
        return iEventoClinicoRepository.findAll(pageRequest);
    }

    @Cacheable(value = "retornarTodosEventos")
    public List<EventoClinico> findAll() {
        return iEventoClinicoRepository.findAll();
    }

    @Cacheable(value = "retornarEventoPorId", key = "#id")
    public Optional<EventoClinico> findById(Long id) {
        return iEventoClinicoRepository.findById(id);
    }

    @Cacheable(value = "retornarEventosPorTipo", key = "#tipo")
    public List<EventoClinico> findByTipo(TipoEventoClinicoEnum tipo) {
        return iEventoClinicoRepository.findByTipo(tipo);
    }

    @Cacheable(value = "retornarEventosComMedicamentos", key = "#idPet")
    public List<EventoClinico> buscarEventosComMedicamentos(Long idPet) {
        return iEventoClinicoRepository.buscarEventosComMedicamentos(idPet);
    }

    @CacheEvict(value = {"retornarTodosEventosPaginados", "retornarTodosEventos",
            "retornarEventoPorId", "retornarEventosPorTipo",
            "retornarEventosComMedicamentos"}, allEntries = true)
    public void removerCache() {
        System.out.println("Cache de EventoClinico removido!");
    }
}