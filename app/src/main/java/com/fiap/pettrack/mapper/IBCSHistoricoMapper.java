package com.fiap.pettrack.mapper;

import com.fiap.pettrack.dto.BCSHistoricoDTO;
import com.fiap.pettrack.model.BCSHistorico;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBCSHistoricoMapper {


    BCSHistoricoDTO toDTO(BCSHistorico bcs);
    BCSHistorico toEntity(BCSHistoricoDTO bcsDTO);
}