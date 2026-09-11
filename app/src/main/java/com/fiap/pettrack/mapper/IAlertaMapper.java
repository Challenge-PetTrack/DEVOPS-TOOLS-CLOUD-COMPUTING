package com.fiap.pettrack.mapper;

import com.fiap.pettrack.dto.AlertaDTO;
import com.fiap.pettrack.model.Alerta;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IAlertaMapper {

    AlertaDTO toDTO(Alerta alerta);
    Alerta toEntity(AlertaDTO alertaDTO);
}