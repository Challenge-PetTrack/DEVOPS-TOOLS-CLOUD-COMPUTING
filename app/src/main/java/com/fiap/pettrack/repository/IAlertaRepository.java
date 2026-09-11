package com.fiap.pettrack.repository;

import com.fiap.pettrack.model.Alerta;
import com.fiap.pettrack.model.enums.TipoAlertaEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAlertaRepository extends JpaRepository<Alerta, Long> {

    // Busca por tipo
    List<Alerta> findByTipoAlerta(TipoAlertaEnum tipoAlerta);

    // Busca alertas por status de resolução (S/N)
    List<Alerta> findByResolvido(com.fiap.pettrack.model.enums.SimNaoEnum resolvido);

    // JPQL — busca alertas pendentes por pet
    @Query("SELECT a FROM Alerta a WHERE " +
            "a.pet.id = :idPet AND a.resolvido = 'N'")
    List<Alerta> buscarPendentesPorPet(Long idPet);
}