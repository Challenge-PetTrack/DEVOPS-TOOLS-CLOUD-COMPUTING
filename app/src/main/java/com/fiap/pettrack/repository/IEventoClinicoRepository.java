package com.fiap.pettrack.repository;

import com.fiap.pettrack.model.EventoClinico;
import com.fiap.pettrack.model.enums.TipoEventoClinicoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IEventoClinicoRepository extends JpaRepository<EventoClinico, Long> {

    List<EventoClinico> findByTipo(TipoEventoClinicoEnum tipo);

    // Native — busca eventos com medicamentos associados
    @Query(nativeQuery = true, value =
            "SELECT TB_EVENTO_CLINICO.* FROM TB_EVENTO_CLINICO " +
                    "JOIN TB_MEDICAMENTO ON TB_MEDICAMENTO.ID_EVENTO = TB_EVENTO_CLINICO.ID_EVENTO " +
                    "WHERE TB_EVENTO_CLINICO.ID_PET = :idPet")
    List<EventoClinico> buscarEventosComMedicamentos(Long idPet);
}