package com.fiap.pettrack.mapper;

import com.fiap.pettrack.dto.CollarLeituraDTO;
import com.fiap.pettrack.model.CollarLeitura;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICollarLeituraMapper {

    CollarLeituraDTO toDTO(CollarLeitura collar);
    CollarLeitura toEntity(CollarLeituraDTO collarDTO);
}