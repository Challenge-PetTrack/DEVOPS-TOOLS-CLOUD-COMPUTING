package com.fiap.pettrack.mapper;

import com.fiap.pettrack.dto.ClinicaDTO;
import com.fiap.pettrack.model.Clinica;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IClinicaMapper {
    ClinicaDTO toDTO(Clinica clinica);
    Clinica toEntity(ClinicaDTO clinicaDTO);
}