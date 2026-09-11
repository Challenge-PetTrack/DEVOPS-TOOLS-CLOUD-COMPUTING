package com.fiap.pettrack.web;

import com.fiap.pettrack.model.Pet;
import com.fiap.pettrack.model.enums.SexoPetEnum;
import com.fiap.pettrack.model.enums.SimNaoEnum;
import com.fiap.pettrack.repository.IAlertaRepository;
import com.fiap.pettrack.repository.IClinicaRepository;
import com.fiap.pettrack.repository.IPetRepository;
import com.fiap.pettrack.repository.ITutorRepository;
import com.fiap.pettrack.service.fluxos.TelemetriaCollarFluxoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminWebController {

    @Autowired
    private IPetRepository petRepository;

    @Autowired
    private ITutorRepository tutorRepository;

    @Autowired
    private IClinicaRepository clinicaRepository;

    @Autowired
    private IAlertaRepository alertaRepository;

    @Autowired
    private TelemetriaCollarFluxoService telemetriaFluxoService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalPets", petRepository.count());
        model.addAttribute("totalTutores", tutorRepository.count());
        model.addAttribute("totalClinicas", clinicaRepository.count());
        model.addAttribute("alertasPendentes", alertaRepository.findByResolvido(SimNaoEnum.N));
        model.addAttribute("pets", petRepository.findAll());
        return "admin/dashboard";
    }

    @GetMapping("/pets")
    public String listarPets(Model model) {
        model.addAttribute("pets", petRepository.findAll());
        return "admin/pets-lista";
    }

    @GetMapping("/pets/novo")
    public String novoPetForm(Model model) {
        model.addAttribute("pet", new Pet());
        model.addAttribute("tutores", tutorRepository.findAll());
        model.addAttribute("clinicas", clinicaRepository.findAll());
        model.addAttribute("sexos", SexoPetEnum.values());
        return "admin/pet-form";
    }

    @PostMapping("/pets/salvar")
    public String salvarPet(
            @Valid @ModelAttribute("pet") Pet pet,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("tutores", tutorRepository.findAll());
            model.addAttribute("clinicas", clinicaRepository.findAll());
            model.addAttribute("sexos", SexoPetEnum.values());
            return "admin/pet-form";
        }

        petRepository.save(pet);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Pet '" + pet.getNome() + "' cadastrado/atualizado com sucesso!");
        return "redirect:/admin/pets";
    }

    @GetMapping("/pets/editar/{id}")
    public String editarPet(@PathVariable Long id, Model model) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pet não encontrado: " + id));

        model.addAttribute("pet", pet);
        model.addAttribute("tutores", tutorRepository.findAll());
        model.addAttribute("clinicas", clinicaRepository.findAll());
        model.addAttribute("sexos", SexoPetEnum.values());
        return "admin/pet-form";
    }

    @GetMapping("/pets/excluir/{id}")
    public String excluirPet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (petRepository.existsById(id)) {
            petRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Pet excluído com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("mensagemErro", "Pet não encontrado para exclusão.");
        }
        return "redirect:/admin/pets";
    }

    // Interface do FLUXO 1: Telemetria IoT & Geração de Alertas
    @GetMapping("/telemetria")
    public String simuladorTelemetriaForm(Model model) {
        model.addAttribute("pets", petRepository.findAll());
        model.addAttribute("telemetriaInput", new TelemetriaCollarFluxoService.TelemetriaInput());
        return "admin/telemetria-simulador";
    }

    @PostMapping("/telemetria/processar")
    public String processarTelemetria(
            @ModelAttribute("telemetriaInput") TelemetriaCollarFluxoService.TelemetriaInput input,
            Model model) {

        try {
            TelemetriaCollarFluxoService.TelemetriaResultado resultado = telemetriaFluxoService.processarTelemetria(input);
            model.addAttribute("resultado", resultado);
            model.addAttribute("mensagemSucesso", "Telemetria processada com sucesso no fluxo automatizado!");
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Falha ao processar telemetria: " + e.getMessage());
        }

        model.addAttribute("pets", petRepository.findAll());
        return "admin/telemetria-simulador";
    }
}
