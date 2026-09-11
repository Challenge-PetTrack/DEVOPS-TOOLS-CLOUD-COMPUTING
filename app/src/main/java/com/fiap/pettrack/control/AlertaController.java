package com.fiap.pettrack.control;

import com.fiap.pettrack.dto.AlertaDTO;
import com.fiap.pettrack.mapper.IAlertaMapper;
import com.fiap.pettrack.model.Alerta;
import com.fiap.pettrack.model.enums.TipoAlertaEnum;
import com.fiap.pettrack.repository.IAlertaRepository;
import com.fiap.pettrack.service.AlertaCachingService;
import com.fiap.pettrack.service.AlertaPaginacaoService;
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
@RequestMapping(value = "/alerta")
public class AlertaController {

    @Autowired
    private IAlertaRepository repository;
    @Autowired
    private IAlertaMapper mapper;
    @Autowired
    private AlertaCachingService cachingService;
    @Autowired
    private AlertaPaginacaoService paginacaoService;

    @Operation(description = "Retorna todos os alertas", summary = "Retorna AlertaDTO", tags = "Retorno de Informações")
    @GetMapping("/todos")
    public ResponseEntity<List<AlertaDTO>> retornarTodos() {
        return ResponseEntity.ok(cachingService.findAll().stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna alertas paginados", summary = "Retorna AlertaDTO paginado", tags = "Retorno de Informações")
    @GetMapping("/paginar")
    public ResponseEntity<Page<AlertaDTO>> paginar(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size) {
        PageRequest request = PageRequest.of(page, size);
        return ResponseEntity.ok(paginacaoService.paginar(request).map(mapper::toDTO));
    }

    @Operation(description = "Retorna alerta por ID", summary = "Retorna Alerta por ID", tags = "Retorno de Informações")
    @GetMapping("/{id}")
    public ResponseEntity<AlertaDTO> retornarPorId(@PathVariable Long id) {
        Optional<Alerta> op = cachingService.findById(id);
        if (op.isPresent()) {
            return ResponseEntity.ok(mapper.toDTO(op.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(description = "Retorna alertas por tipo", summary = "Retorna alertas por tipo", tags = "Retorno de Informações")
    @GetMapping("/tipo")
    public ResponseEntity<List<AlertaDTO>> retornarPorTipo(@RequestParam TipoAlertaEnum tipoAlerta) {
        return ResponseEntity.ok(cachingService.findByTipoAlerta(tipoAlerta).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna alertas pendentes por pet", summary = "Alertas pendentes por pet", tags = "Retorno de Informações")
    @GetMapping("/pendentes/{idPet}")
    public ResponseEntity<List<AlertaDTO>> buscarPendentesPorPet(@PathVariable Long idPet) {
        return ResponseEntity.ok(cachingService.buscarPendentesPorPet(idPet).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Inserir novo alerta", summary = "Inserir Alerta", tags = "Inserção de Informações")
    @PostMapping("/novo")
    public ResponseEntity<AlertaDTO> inserir(@RequestBody @Valid AlertaDTO dto) {
        Alerta salvo = repository.save(mapper.toEntity(dto));
        cachingService.removerCache();
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @Operation(description = "Remover alerta", summary = "Remover Alerta", tags = "Remoção de Informações")
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Optional<Alerta> op = repository.findById(id);
        if (op.isPresent()) {
            repository.delete(op.get());
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Atualizar alerta", summary = "Atualizar Alerta", tags = "Atualização de Informações")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<AlertaDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AlertaDTO dto) {
        Optional<Alerta> op = cachingService.findById(id);
        if (op.isPresent()) {
            Alerta antigo = op.get();
            Alerta novo = mapper.toEntity(dto);
            antigo.transferir(novo);
            repository.save(antigo);
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDTO(antigo));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}