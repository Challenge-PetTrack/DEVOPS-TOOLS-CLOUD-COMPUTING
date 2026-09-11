package com.fiap.pettrack.repository;

import com.fiap.pettrack.model.CollarLeitura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICollarLeituraRepository extends JpaRepository<CollarLeitura, Long> {

    // JPQL — busca leituras com temperatura acima do normal
    @Query("SELECT c FROM CollarLeitura c WHERE " +
            "c.pet.id = :idPet AND c.temperatura > :temperatura")
    List<CollarLeitura> buscarPorPetETemperaturaAcimaDe(Long idPet, Double temperatura);

    // Native — busca última leitura por pet
    @Query(nativeQuery = true, value =
            "SELECT * FROM TB_COLLAR_LEITURA " +
                    "WHERE ID_PET = :idPet " +
                    "ORDER BY DT_LEITURA DESC " +
                    "FETCH FIRST 1 ROWS ONLY")
    CollarLeitura buscarUltimaLeituraPorPet(Long idPet);
}