package com.fiap.pettrack.repository;

import com.fiap.pettrack.model.Pet;
import com.fiap.pettrack.model.enums.SexoPetEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPetRepository extends JpaRepository<Pet, Long> {


    List<Pet> findByClinicaId(Long idClinica);

    List<Pet> findByTutorId(Long idTutor);

    List<Pet> findBySexo(SexoPetEnum sexo);

    @Query("SELECT p FROM Pet p WHERE " +
            "LOWER(p.nome) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
            "LOWER(p.especie) LIKE LOWER(CONCAT('%', :busca, '%'))")
    List<Pet> buscarPorNomeOuEspecie(String busca);

    @Query(nativeQuery = true, value =
            "SELECT TB_PET.* FROM TB_PET " +
                    "JOIN TB_ALERTA ON TB_ALERTA.ID_PET = TB_PET.ID_PET " +
                    "WHERE TB_ALERTA.ST_RESOLVIDO = 'N'")
    List<Pet> buscarPetsComAlertasPendentes();
}