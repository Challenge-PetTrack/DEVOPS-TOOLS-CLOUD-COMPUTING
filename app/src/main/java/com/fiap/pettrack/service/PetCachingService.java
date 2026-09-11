package com.fiap.pettrack.service;

import com.fiap.pettrack.model.Pet;
import com.fiap.pettrack.model.enums.SexoPetEnum;
import com.fiap.pettrack.repository.IPetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetCachingService {

    @Autowired
    private IPetRepository iPetRepository;

    @Cacheable(value = "retornarTodosPetsPaginados", key = "#pageRequest")
    public Page<Pet> findAll(PageRequest pageRequest) {
        return iPetRepository.findAll(pageRequest);
    }

    @Cacheable(value = "retornarTodosPets")
    public List<Pet> findAll() {
        return iPetRepository.findAll();
    }

    @Cacheable(value = "retornarPetPorId", key = "#id")
    public Optional<Pet> findById(Long id) {
        return iPetRepository.findById(id);
    }

    @Cacheable(value = "retornarPetsPorClinica", key = "#idClinica")
    public List<Pet> findByClinicaId(Long idClinica) {
        return iPetRepository.findByClinicaId(idClinica);
    }

    @Cacheable(value = "retornarPetsPorSexo", key = "#sexo")
    public List<Pet> findBySexo(SexoPetEnum sexo) {
        return iPetRepository.findBySexo(sexo);
    }

    @Cacheable(value = "retornarPetsPorNomeOuEspecie", key = "#busca")
    public List<Pet> buscarPorNomeOuEspecie(String busca) {
        return iPetRepository.buscarPorNomeOuEspecie(busca);
    }

    @Cacheable(value = "retornarPetsComAlertasPendentes")
    public List<Pet> buscarPetsComAlertasPendentes() {
        return iPetRepository.buscarPetsComAlertasPendentes();
    }

    @CacheEvict(value = {"retornarTodosPetsPaginados", "retornarTodosPets",
            "retornarPetPorId", "retornarPetsPorClinica", "retornarPetsPorSexo",
            "retornarPetsPorNomeOuEspecie", "retornarPetsComAlertasPendentes"}, allEntries = true)
    public void removerCache() {
        System.out.println("Cache de Pet removido!");
    }
}