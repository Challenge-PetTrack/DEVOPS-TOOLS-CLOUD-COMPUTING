package com.fiap.pettrack.repository;

import com.fiap.pettrack.model.ProtocoloPreventivo;
import com.fiap.pettrack.model.enums.TipoProtocoloPreventivoEnum;
import com.fiap.pettrack.model.enums.StatusProtocoloPreventivoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IProtocoloPreventivoRepository extends JpaRepository<ProtocoloPreventivo, Long> {

    List<ProtocoloPreventivo> findByTipo(TipoProtocoloPreventivoEnum tipo);

    // JPQL — busca protocolos pendentes ou atrasados por pet
    @Query("SELECT p FROM ProtocoloPreventivo p WHERE " +
            "p.pet.id = :idPet AND " +
            "p.status IN ('PENDENTE', 'ATRASADO')")
    List<ProtocoloPreventivo> buscarPendentesOuAtrasadosPorPet(Long idPet);
}