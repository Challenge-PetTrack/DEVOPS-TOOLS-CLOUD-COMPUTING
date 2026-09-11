package com.fiap.pettrack.service;

import com.fiap.pettrack.model.Tutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TutorPaginacaoService {

    @Autowired
    private TutorCachingService tutorCachingService;

    @Transactional(readOnly = true)
    public Page<Tutor> paginar(PageRequest pageRequest){
        return tutorCachingService.findAll(pageRequest);
    }
}
