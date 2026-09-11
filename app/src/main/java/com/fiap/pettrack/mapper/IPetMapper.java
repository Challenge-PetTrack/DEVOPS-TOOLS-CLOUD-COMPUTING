package com.fiap.pettrack.mapper;

import com.fiap.pettrack.dto.PetDTO;
import com.fiap.pettrack.model.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPetMapper {

    PetDTO toDTO(Pet pet);
    Pet toEntity(PetDTO petDTO);
}