package com.fiap.pettrack.mapper;

import com.fiap.pettrack.dto.ProtocoloPreventivoDTO;
import com.fiap.pettrack.model.ProtocoloPreventivo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IProtocoloPreventivoMapper {

    ProtocoloPreventivoDTO toDTO(ProtocoloPreventivo protocolo);
    ProtocoloPreventivo toEntity(ProtocoloPreventivoDTO protocoloDTO);
}