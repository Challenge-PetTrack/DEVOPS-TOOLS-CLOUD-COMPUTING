package com.fiap.pettrack.control;

import com.fiap.pettrack.dto.ClinicaDTO;
import com.fiap.pettrack.mapper.IClinicaMapper;
import com.fiap.pettrack.model.Clinica;
import com.fiap.pettrack.repository.IClinicaRepository;
import com.fiap.pettrack.service.ClinicaCachingService;
import com.fiap.pettrack.service.ClinicaPaginacaoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/clinica")
public class ClinicaController {

    @Autowired
    private IClinicaRepository repository;
    @Autowired
    private IClinicaMapper mapper;
    @Autowired
    private ClinicaCachingService cachingService;
    @Autowired
    private ClinicaPaginacaoService paginacaoService;

    @Operation(description = "Retorna todas as clínicas", summary = "Retorna ClinicaDTO", tags = "Retorno de Informações")
    @GetMapping("/todos")
    public ResponseEntity<List<ClinicaDTO>> retornarTodos() {
        return ResponseEntity.ok(cachingService.findAll().stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna clínicas paginadas", summary = "Retorna ClinicaDTO paginado", tags = "Retorno de Informações")
    @GetMapping("/paginar")
    public ResponseEntity<Page<ClinicaDTO>> paginar(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size) {
        PageRequest request = PageRequest.of(page, size);
        return ResponseEntity.ok(paginacaoService.paginar(request).map(mapper::toDTO));
    }

    @Operation(description = "Retorna clínica por ID", summary = "Retorna Clinica por ID", tags = "Retorno de Informações")
    @GetMapping("/{id}")
    public ResponseEntity<ClinicaDTO> retornarPorId(@PathVariable Long id) {
        Optional<Clinica> op = cachingService.findById(id);
        if (op.isPresent()) {
            return ResponseEntity.ok(mapper.toDTO(op.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(description = "Busca clínicas por nome ou CNPJ", summary = "Busca clínicas", tags = "Retorno de Informações")
    @GetMapping("/buscar")
    public ResponseEntity<List<ClinicaDTO>> buscarPorNomeOuCnpj(@RequestParam String busca) {
        return ResponseEntity.ok(cachingService.buscarPorNomeOuCnpj(busca).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Busca clínica por nome do pet", summary = "Clínica por nome do pet", tags = "Retorno de Informações")
    @GetMapping("/nomePet")
    public ResponseEntity<List<ClinicaDTO>> buscarClinicaPorNomePet(@RequestParam String nomePet) {
        return ResponseEntity.ok(cachingService.buscarClinicaPorNomePet(nomePet).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Inserir nova clínica", summary = "Inserir Clinica", tags = "Inserção de Informações")
    @PostMapping("/novo")
    public ResponseEntity<ClinicaDTO> inserir(@RequestBody @Valid ClinicaDTO dto) {
        Clinica salvo = repository.save(mapper.toEntity(dto));
        cachingService.removerCache();
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @Operation(description = "Remover clínica", summary = "Remover Clinica", tags = "Remoção de Informações")
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Optional<Clinica> op = repository.findById(id);
        if (op.isPresent()) {
            repository.delete(op.get());
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Atualizar clínica", summary = "Atualizar Clinica", tags = "Atualização de Informações")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ClinicaDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ClinicaDTO dto) {
        Optional<Clinica> op = cachingService.findById(id);
        if (op.isPresent()) {
            Clinica antigo = op.get();
            Clinica novo = mapper.toEntity(dto);
            antigo.transferir(novo);
            repository.save(antigo);
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDTO(antigo));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}