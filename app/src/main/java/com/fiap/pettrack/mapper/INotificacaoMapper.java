package com.fiap.pettrack.mapper;

import com.fiap.pettrack.dto.NotificacaoDTO;
import com.fiap.pettrack.model.Notificacao;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface INotificacaoMapper {

    NotificacaoDTO toDTO(Notificacao notificacao);
    Notificacao toEntity(NotificacaoDTO notificacaoDTO);
}