package com.fiap.pettrack.repository;

import com.fiap.pettrack.model.BCSHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IBCSHistoricoRepository extends JpaRepository<BCSHistorico, Long> {

    // JPQL — busca último BCS do pet
    @Query("SELECT b FROM BCSHistorico b WHERE " +
            "b.pet.id = :idPet ORDER BY b.dataAnalise DESC")
    List<BCSHistorico> buscarHistoricoPorPet(Long idPet);

    // Native — busca média de BCS por pet
    @Query(nativeQuery = true, value =
            "SELECT AVG(NR_BCS) FROM TB_BCS_HISTORICO " +
                    "WHERE ID_PET = :idPet")
    Double buscarMediaBcsPorPet(Long idPet);
}