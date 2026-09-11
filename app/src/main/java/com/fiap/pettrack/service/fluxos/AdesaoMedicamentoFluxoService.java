package com.fiap.pettrack.service.fluxos;

import com.fiap.pettrack.model.*;
import com.fiap.pettrack.model.enums.SimNaoEnum;
import com.fiap.pettrack.model.enums.TipoAlertaEnum;
import com.fiap.pettrack.model.enums.TipoNotificacaoEnum;
import com.fiap.pettrack.repository.IAdesaoMedicamentoRepository;
import com.fiap.pettrack.repository.IAlertaRepository;
import com.fiap.pettrack.repository.IMedicamentoRepository;
import com.fiap.pettrack.repository.INotificacaoRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdesaoMedicamentoFluxoService {

    @Autowired
    private IMedicamentoRepository medicamentoRepository;

    @Autowired
    private IAdesaoMedicamentoRepository adesaoMedicamentoRepository;

    @Autowired
    private IAlertaRepository alertaRepository;

    @Autowired
    private INotificacaoRepository notificacaoRepository;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AdesaoInput {
        private Long idMedicamento;
        private LocalDate dataDose;
        private String tomou; // "S" ou "N"
        private String observacao;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AdesaoResultado {
        private Long idMedicamento;
        private String nomeMedicamento;
        private String nomePet;
        private LocalDate dataDose;
        private String tomou;
        private Double taxaAdesaoPercentual;
        private boolean alertaGerado;
        private String mensagem;
    }

    @Transactional
    public AdesaoResultado registrarDose(AdesaoInput input) {
        Medicamento medicamento = medicamentoRepository.findById(input.getIdMedicamento())
                .orElseThrow(() -> new IllegalArgumentException("Medicamento não encontrado para o ID: " + input.getIdMedicamento()));

        Pet pet = medicamento.getEvento().getPet();
        SimNaoEnum statusDose = "S".equalsIgnoreCase(input.getTomou()) ? SimNaoEnum.S : SimNaoEnum.N;

        // 1. Registra a adesão da dose
        AdesaoMedicamento adesao = new AdesaoMedicamento();
        adesao.setMedicamento(medicamento);
        adesao.setDataDose(input.getDataDose() != null ? input.getDataDose() : LocalDate.now());
        adesao.setStatus(statusDose);
        adesao.setObservacao(input.getObservacao());
        adesaoMedicamentoRepository.save(adesao);

        // 2. Calcula taxa percentual de adesão
        List<AdesaoMedicamento> historicoDoses = adesaoMedicamentoRepository.findByMedicamentoId(medicamento.getId());
        long totalDoses = historicoDoses.size();
        long dosesTomadas = historicoDoses.stream().filter(d -> d.getStatus() == SimNaoEnum.S).count();
        double taxaAdesao = totalDoses > 0 ? ((double) dosesTomadas / totalDoses) * 100.0 : 0.0;
        taxaAdesao = Math.round(taxaAdesao * 100.0) / 100.0;

        boolean alertaGerado = false;

        // 3. Se a dose foi NÃO tomada ('N'), dispara alerta clínico e notificação de urgência
        if (statusDose == SimNaoEnum.N) {
            alertaGerado = true;

            Alerta alertaAdesao = new Alerta();
            alertaAdesao.setTipoAlerta(TipoAlertaEnum.ADESAO);
            alertaAdesao.setDescricao("Dose de " + medicamento.getNome() + " não administrada para " + pet.getNome() + ". Motivo: " + (input.getObservacao() != null ? input.getObservacao() : "Não informado"));
            alertaAdesao.setResolvido(SimNaoEnum.N);
            alertaAdesao.setPet(pet);
            alertaRepository.save(alertaAdesao);

            Notificacao notifAdesao = new Notificacao();
            notifAdesao.setTipo(TipoNotificacaoEnum.URGENTE);
            notifAdesao.setTitulo("Atraso de Medicamento: " + pet.getNome());
            notifAdesao.setMensagem("A dose de " + medicamento.getNome() + " não foi administrada. Entre em contato com a clínica se necessário.");
            notifAdesao.setStatus(SimNaoEnum.N);
            notifAdesao.setTutor(pet.getTutor());
            notifAdesao.setPet(pet);
            notificacaoRepository.save(notifAdesao);
        }

        String mensagem = (statusDose == SimNaoEnum.S)
                ? "Dose confirmada com sucesso! Taxa de adesão atualizada: " + taxaAdesao + "%."
                : "Dose registrada como não administrada. Alerta veterinário gerado automaticamente.";

        return AdesaoResultado.builder()
                .idMedicamento(medicamento.getId())
                .nomeMedicamento(medicamento.getNome())
                .nomePet(pet.getNome())
                .dataDose(adesao.getDataDose())
                .tomou(statusDose.name())
                .taxaAdesaoPercentual(taxaAdesao)
                .alertaGerado(alertaGerado)
                .mensagem(mensagem)
                .build();
    }
}
