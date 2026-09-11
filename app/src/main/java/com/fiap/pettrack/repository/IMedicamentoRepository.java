package com.fiap.pettrack.repository;

import com.fiap.pettrack.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IMedicamentoRepository extends JpaRepository<Medicamento, Long> {

    List<Medicamento> findByNomeContainingIgnoreCase(String nome);


    // Native — busca medicamentos ativos (sem data de fim ou data de fim futura)
    @Query(nativeQuery = true, value =
            "SELECT * FROM TB_MEDICAMENTO " +
                    "WHERE ID_EVENTO IN (" +
                    "   SELECT ID_EVENTO FROM TB_EVENTO_CLINICO WHERE ID_PET = :idPet" +
                    ") AND (DT_FIM IS NULL OR DT_FIM >= SYSDATE)")
    List<Medicamento> buscarMedicamentosAtivosPorPet(Long idPet);
}