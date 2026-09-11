package com.fiap.pettrack.service;

import com.fiap.pettrack.model.Clinica;
import com.fiap.pettrack.repository.IClinicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicaCachingService {

    @Autowired
    private IClinicaRepository iClinicaRepository;

    @Cacheable(value = "retornarTodasClinicasPaginadas", key = "#pageRequest")
    public Page<Clinica> findAll(PageRequest pageRequest) {
        return iClinicaRepository.findAll(pageRequest);
    }

    @Cacheable(value = "retornarTodasClinicas")
    public List<Clinica> findAll() {
        return iClinicaRepository.findAll();
    }

    @Cacheable(value = "retornarClinicaPorId", key = "#id")
    public Optional<Clinica> findById(Long id) {
        return iClinicaRepository.findById(id);
    }

    @Cacheable(value = "retornarClinicasPorNomeOuCnpj", key = "#busca")
    public List<Clinica> buscarPorNomeOuCnpj(String busca) {
        return iClinicaRepository.buscarPorNomeOuCnpj(busca);
    }

    @Cacheable(value = "retornarClinicaPorNomePet", key = "#nomePet")
    public List<Clinica> buscarClinicaPorNomePet(String nomePet) {
        return iClinicaRepository.buscarClinicaPorNomePet(nomePet);
    }

    @CacheEvict(value = {"retornarTodasClinicasPaginadas", "retornarTodasClinicas",
            "retornarClinicaPorId", "retornarClinicasPorNomeOuCnpj",
            "retornarClinicaPorNomePet"}, allEntries = true)
    public void removerCache() {
        System.out.println("Cache de Clinica removido!");
    }
}