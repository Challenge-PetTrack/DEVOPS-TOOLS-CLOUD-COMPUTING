package com.fiap.pettrack.service;

import com.fiap.pettrack.model.Tutor;
import com.fiap.pettrack.repository.ITutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TutorCachingService {

    @Autowired
    private ITutorRepository iTutorRepository;

    @Cacheable(value = "retornarTodosTutoresPaginados", key = "#pageRequest")
    public Page<Tutor> findAll(PageRequest pageRequest){
        return iTutorRepository.findAll(pageRequest);
    }

    @Cacheable(value = "retornarTodosTutores")
    public List<Tutor> findAll(){
        return iTutorRepository.findAll();
    }

    @Cacheable(value = "retornarTutorPorId", key = "#id")
    public Optional<Tutor> findById(Long id){
        return iTutorRepository.findById(id);
    }

    @Cacheable(value = "retornarTutorPorNomePet", key = "#nomePet")
    public List<Tutor> buscarTutorPorNomePet(String nomePet){
        return iTutorRepository.buscarTutorPorNomePet(nomePet);
    };

    @Cacheable(value = "retornarTutorPorNomeOuEmail", key = "#busca")
    public List<Tutor> buscarPorNomeOuEmail(String busca){
        return iTutorRepository.buscarPorNomeOuEmail(busca);
    }

    @CacheEvict(value = {"retornarTodosTutoresPaginados","retornarTodosTutores", "retornarTutorPorId", "retornarTutorPorNomePet", "retornarTutorPorNomeOuEmail"}, allEntries = true)
    public void removerCache(){
        System.out.println("Cache de Tutor removido!");
    }
}
