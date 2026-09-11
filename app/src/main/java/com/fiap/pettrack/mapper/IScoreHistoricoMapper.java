package com.fiap.pettrack.mapper;

import com.fiap.pettrack.dto.ScoreHistoricoDTO;
import com.fiap.pettrack.model.ScoreHistorico;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IScoreHistoricoMapper {

    ScoreHistoricoDTO toDTO(ScoreHistorico score);
    ScoreHistorico toEntity(ScoreHistoricoDTO scoreDTO);
}