package com.fiap.pettrack.mapper;

import com.fiap.pettrack.dto.EventoClinicoDTO;
import com.fiap.pettrack.model.EventoClinico;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IEventoClinicoMapper {

    EventoClinicoDTO toDTO(EventoClinico evento);
    EventoClinico toEntity(EventoClinicoDTO eventoDTO);
}