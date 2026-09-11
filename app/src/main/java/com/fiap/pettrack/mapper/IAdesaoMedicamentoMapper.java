package com.fiap.pettrack.mapper;

import com.fiap.pettrack.dto.AdesaoMedicamentoDTO;
import com.fiap.pettrack.model.AdesaoMedicamento;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IAdesaoMedicamentoMapper {

    AdesaoMedicamentoDTO toDTO(AdesaoMedicamento adesao);
    AdesaoMedicamento toEntity(AdesaoMedicamentoDTO adesaoDTO);
}