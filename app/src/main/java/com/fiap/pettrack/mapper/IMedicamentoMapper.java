package com.fiap.pettrack.mapper;

import com.fiap.pettrack.dto.MedicamentoDTO;
import com.fiap.pettrack.model.Medicamento;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IMedicamentoMapper {


    MedicamentoDTO toDTO(Medicamento medicamento);
    Medicamento toEntity(MedicamentoDTO medicamentoDTO);
}