package com.fiap.pettrack.mapper;

import com.fiap.pettrack.dto.TutorDTO;
import com.fiap.pettrack.model.Tutor;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITutorMapper {

    TutorDTO toDTO(Tutor tutor);
    Tutor toEntity(TutorDTO tutorDTO);
}
