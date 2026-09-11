package com.fiap.pettrack.repository;

import com.fiap.pettrack.model.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IClinicaRepository extends JpaRepository<Clinica, Long> {

    @Query("SELECT c FROM Clinica c WHERE " +
            "LOWER(c.nome) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
            "LOWER(c.cnpj) LIKE LOWER(CONCAT('%', :busca, '%'))")
    List<Clinica> buscarPorNomeOuCnpj(String busca);

    @Query(nativeQuery = true, value =
            "SELECT TB_CLINICA.* " +
                    "FROM TB_PET " +
                    "JOIN TB_CLINICA ON TB_PET.ID_CLINICA = TB_CLINICA.ID_CLINICA " +
                    "WHERE LOWER(TB_PET.NM_PET) LIKE LOWER(CONCAT('%', :nomePet, '%'))")
    List<Clinica> buscarClinicaPorNomePet(String nomePet);

}