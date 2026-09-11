package com.fiap.pettrack.web;

import com.fiap.pettrack.model.Medicamento;
import com.fiap.pettrack.model.Pet;
import com.fiap.pettrack.model.Usuario;
import com.fiap.pettrack.repository.*;
import com.fiap.pettrack.service.fluxos.AdesaoMedicamentoFluxoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/portal-tutor")
public class TutorWebController {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IPetRepository petRepository;

    @Autowired
    private IMedicamentoRepository medicamentoRepository;

    @Autowired
    private IAdesaoMedicamentoRepository adesaoMedicamentoRepository;

    @Autowired
    private IProtocoloPreventivoRepository protocoloRepository;

    @Autowired
    private INotificacaoRepository notificacaoRepository;

    @Autowired
    private IScoreHistoricoRepository scoreHistoricoRepository;

    @Autowired
    private IAlertaRepository alertaRepository;

    @Autowired
    private AdesaoMedicamentoFluxoService adesaoFluxoService;

    private Usuario obterUsuarioLogado(Authentication auth) {
        if (auth == null) return null;
        return usuarioRepository.findByEmail(auth.getName()).orElse(null);
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Usuario usuario = obterUsuarioLogado(auth);
        List<Pet> pets;

        if (usuario != null && usuario.getTutor() != null) {
            pets = petRepository.findByTutorId(usuario.getTutor().getId());
            model.addAttribute("notificacoes", notificacaoRepository.findByTutorId(usuario.getTutor().getId()));
        } else {
            pets = petRepository.findAll();
            model.addAttribute("notificacoes", notificacaoRepository.findAll());
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("pets", pets);
        return "tutor/dashboard";
    }

    @GetMapping("/pet/{id}")
    public String detalhesPet(@PathVariable Long id, Model model) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pet não encontrado: " + id));

        model.addAttribute("pet", pet);
        model.addAttribute("protocolos", protocoloRepository.buscarPendentesOuAtrasadosPorPet(id));
        model.addAttribute("alertas", alertaRepository.buscarPendentesPorPet(id));
        model.addAttribute("medicamentos", medicamentoRepository.buscarMedicamentosAtivosPorPet(id));
        return "tutor/pet-detalhes";
    }

    // Interface do FLUXO 2: Acompanhamento e Adesão Medicamentosa
    @GetMapping("/medicamentos")
    public String listarMedicamentos(Authentication auth, Model model) {
        Usuario usuario = obterUsuarioLogado(auth);
        List<Medicamento> medicamentos = medicamentoRepository.findAll();

        model.addAttribute("usuario", usuario);
        model.addAttribute("medicamentos", medicamentos);
        model.addAttribute("adesaoInput", new AdesaoMedicamentoFluxoService.AdesaoInput());
        model.addAttribute("historicoAdesao", adesaoMedicamentoRepository.findAll());
        return "tutor/medicamentos";
    }

    @PostMapping("/medicamentos/registrar-dose")
    public String registrarDose(
            @ModelAttribute("adesaoInput") AdesaoMedicamentoFluxoService.AdesaoInput input,
            RedirectAttributes redirectAttributes) {

        try {
            AdesaoMedicamentoFluxoService.AdesaoResultado resultado = adesaoFluxoService.registrarDose(input);
            if (resultado.isAlertaGerado()) {
                redirectAttributes.addFlashAttribute("mensagemAlerta", resultado.getMensagem());
            } else {
                redirectAttributes.addFlashAttribute("mensagemSucesso", resultado.getMensagem());
            }
            redirectAttributes.addFlashAttribute("resultadoAdesao", resultado);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao registrar dose: " + e.getMessage());
        }

        return "redirect:/portal-tutor/medicamentos";
    }
}
