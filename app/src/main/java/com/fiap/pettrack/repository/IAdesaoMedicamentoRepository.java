package com.fiap.pettrack.repository;

import com.fiap.pettrack.model.AdesaoMedicamento;
import com.fiap.pettrack.model.enums.SimNaoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAdesaoMedicamentoRepository extends JpaRepository<AdesaoMedicamento, Long> {

    List<AdesaoMedicamento> findByMedicamentoId(Long idMedicamento);

    List<AdesaoMedicamento> findByStatus(SimNaoEnum status);
}