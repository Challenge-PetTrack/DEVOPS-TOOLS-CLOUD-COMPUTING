package com.fiap.pettrack.repository;

import com.fiap.pettrack.model.Notificacao;
import com.fiap.pettrack.model.enums.SimNaoEnum;
import com.fiap.pettrack.model.enums.TipoNotificacaoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface INotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByStatus(SimNaoEnum status);

    List<Notificacao> findByTutorId(Long idTutor);

    List<Notificacao> findByTipo(TipoNotificacaoEnum tipo);

    // Native — busca notificações urgentes não lidas
    @Query(nativeQuery = true, value =
            "SELECT * FROM TB_NOTIFICACAO " +
                    "WHERE ID_TUTOR = :idTutor " +
                    "AND TP_NOTIFICACAO = 'URGENTE' " +
                    "AND ST_LIDA = 'N'")
    List<Notificacao> buscarUrgentesNaoLidasPorTutor(Long idTutor);
}