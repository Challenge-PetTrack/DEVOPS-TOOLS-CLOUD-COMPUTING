package com.fiap.pettrack.repository;

import com.fiap.pettrack.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ITutorRepository extends JpaRepository<Tutor, Long> {


    @Query(nativeQuery = true, value =
            "SELECT TB_TUTOR.* " +
                    "FROM TB_PET " +
                    "JOIN TB_TUTOR ON TB_PET.ID_TUTOR = TB_TUTOR.ID_TUTOR " +
                    "WHERE LOWER(TB_PET.NM_PET) LIKE LOWER(CONCAT('%', :nomePet, '%'))")
    List<Tutor> buscarTutorPorNomePet(String nomePet);


    @Query("SELECT t FROM Tutor t WHERE " +
            "LOWER(t.nome) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
            "LOWER(t.email) LIKE LOWER(CONCAT('%', :busca, '%'))")
    List<Tutor> buscarPorNomeOuEmail(String busca);
}