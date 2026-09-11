package com.fiap.pettrack.service.fluxos;

import com.fiap.pettrack.model.*;
import com.fiap.pettrack.model.enums.SimNaoEnum;
import com.fiap.pettrack.model.enums.TipoAlertaEnum;
import com.fiap.pettrack.model.enums.TipoNotificacaoEnum;
import com.fiap.pettrack.repository.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TelemetriaCollarFluxoService {

    @Autowired
    private IPetRepository petRepository;

    @Autowired
    private ICollarLeituraRepository collarLeituraRepository;

    @Autowired
    private IAlertaRepository alertaRepository;

    @Autowired
    private IScoreHistoricoRepository scoreHistoricoRepository;

    @Autowired
    private INotificacaoRepository notificacaoRepository;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TelemetriaInput {
        private Long idPet;
        private Double temperatura;
        private Double atividade;
        private String topicoMqtt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TelemetriaResultado {
        private Long idPet;
        private String nomePet;
        private Double temperatura;
        private Double atividade;
        private boolean febreDetectada;
        private boolean sedentarismoDetectado;
        private Double novoHealthScore;
        private String mensagem;
    }

    @Transactional
    public TelemetriaResultado processarTelemetria(TelemetriaInput input) {
        Pet pet = petRepository.findById(input.getIdPet())
                .orElseThrow(() -> new IllegalArgumentException("Pet não encontrado para o ID: " + input.getIdPet()));

        // 1. Grava a Leitura IoT do Colar
        CollarLeitura leitura = new CollarLeitura();
        leitura.setTemperatura(input.getTemperatura());
        leitura.setAtividade(input.getAtividade());
        leitura.setTopicoMqtt(input.getTopicoMqtt() != null ? input.getTopicoMqtt() : "pettrack/collar/pet" + pet.getId());
        leitura.setPet(pet);
        collarLeituraRepository.save(leitura);

        boolean febre = false;
        boolean sedentarismo = false;
        double scoreCalculado = 100.0;

        // 2. Análise de Febre (Temperatura >= 39.5 °C)
        if (input.getTemperatura() != null && input.getTemperatura() >= 39.5) {
            febre = true;
            scoreCalculado -= 35.0;

            Alerta alertaFebre = new Alerta();
            alertaFebre.setTipoAlerta(TipoAlertaEnum.FEBRE);
            alertaFebre.setDescricao("Temperatura crítica de " + input.getTemperatura() + "°C detectada via coleira inteligente.");
            alertaFebre.setValorRef(input.getTemperatura());
            alertaFebre.setResolvido(SimNaoEnum.N);
            alertaFebre.setPet(pet);
            alertaRepository.save(alertaFebre);

            Notificacao notifFebre = new Notificacao();
            notifFebre.setTipo(TipoNotificacaoEnum.URGENTE);
            notifFebre.setTitulo("Alerta de Febre: " + pet.getNome());
            notifFebre.setMensagem("A coleira inteligente detectou febre (" + input.getTemperatura() + "°C) em " + pet.getNome() + ". Contate o veterinário.");
            notifFebre.setStatus(SimNaoEnum.N);
            notifFebre.setTutor(pet.getTutor());
            notifFebre.setPet(pet);
            notificacaoRepository.save(notifFebre);
        }

        // 3. Análise de Atividade / Sedentarismo (< 20 minutos ou pontos)
        if (input.getAtividade() != null && input.getAtividade() < 20.0) {
            sedentarismo = true;
            scoreCalculado -= 20.0;

            Alerta alertaSedent = new Alerta();
            alertaSedent.setTipoAlerta(TipoAlertaEnum.SEDENTARISMO);
            alertaSedent.setDescricao("Nível de atividade física severamente baixo (" + input.getAtividade() + ").");
            alertaSedent.setValorRef(input.getAtividade());
            alertaSedent.setResolvido(SimNaoEnum.N);
            alertaSedent.setPet(pet);
            alertaRepository.save(alertaSedent);

            Notificacao notifSedent = new Notificacao();
            notifSedent.setTipo(TipoNotificacaoEnum.ALERTA);
            notifSedent.setTitulo("Baixa Atividade: " + pet.getNome());
            notifSedent.setMensagem(pet.getNome() + " apresentou pouca movimentação nas últimas horas.");
            notifSedent.setStatus(SimNaoEnum.N);
            notifSedent.setTutor(pet.getTutor());
            notifSedent.setPet(pet);
            notificacaoRepository.save(notifSedent);
        }

        if (scoreCalculado < 0.0) scoreCalculado = 0.0;
        if (scoreCalculado > 100.0) scoreCalculado = 100.0;

        // 4. Recálculo e Atualização do Health Score
        ScoreHistorico scoreHist = new ScoreHistorico();
        scoreHist.setScore(scoreCalculado);
        scoreHist.setObservacao(febre ? "Score recalculado após detecção de febre" : (sedentarismo ? "Score recalculado após baixa atividade" : "Score regular de telemetria"));
        scoreHist.setPet(pet);
        scoreHistoricoRepository.save(scoreHist);

        String msgStatus = (febre || sedentarismo) 
                ? "Telemetria processada com alertas clínicos gerados!" 
                : "Telemetria normal. Parâmetros dentro dos limites saudáveis.";

        return TelemetriaResultado.builder()
                .idPet(pet.getId())
                .nomePet(pet.getNome())
                .temperatura(input.getTemperatura())
                .atividade(input.getAtividade())
                .febreDetectada(febre)
                .sedentarismoDetectado(sedentarismo)
                .novoHealthScore(scoreCalculado)
                .mensagem(msgStatus)
                .build();
    }
}
