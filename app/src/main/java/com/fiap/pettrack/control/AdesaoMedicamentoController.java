package com.fiap.pettrack.control;

import com.fiap.pettrack.dto.AdesaoMedicamentoDTO;
import com.fiap.pettrack.mapper.IAdesaoMedicamentoMapper;
import com.fiap.pettrack.model.AdesaoMedicamento;
import com.fiap.pettrack.model.enums.SimNaoEnum;
import com.fiap.pettrack.repository.IAdesaoMedicamentoRepository;
import com.fiap.pettrack.service.AdesaoMedicamentoCachingService;
import com.fiap.pettrack.service.AdesaoMedicamentoPaginacaoService;
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
@RequestMapping(value = "/adesao")
public class AdesaoMedicamentoController {

    @Autowired
    private IAdesaoMedicamentoRepository repository;
    @Autowired
    private IAdesaoMedicamentoMapper mapper;
    @Autowired
    private AdesaoMedicamentoCachingService cachingService;
    @Autowired
    private AdesaoMedicamentoPaginacaoService paginacaoService;

    @Operation(description = "Retorna todas as adesões", summary = "Retorna AdesaoDTO", tags = "Retorno de Informações")
    @GetMapping("/todos")
    public ResponseEntity<List<AdesaoMedicamentoDTO>> retornarTodos() {
        return ResponseEntity.ok(cachingService.findAll().stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna adesões paginadas", summary = "Retorna AdesaoDTO paginado", tags = "Retorno de Informações")
    @GetMapping("/paginar")
    public ResponseEntity<Page<AdesaoMedicamentoDTO>> paginar(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size) {
        PageRequest request = PageRequest.of(page, size);
        return ResponseEntity.ok(paginacaoService.paginar(request).map(mapper::toDTO));
    }

    @Operation(description = "Retorna adesão por ID", summary = "Retorna Adesao por ID", tags = "Retorno de Informações")
    @GetMapping("/{id}")
    public ResponseEntity<AdesaoMedicamentoDTO> retornarPorId(@PathVariable Long id) {
        Optional<AdesaoMedicamento> op = cachingService.findById(id);
        if (op.isPresent()) {
            return ResponseEntity.ok(mapper.toDTO(op.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(description = "Retorna adesões por medicamento", summary = "Retorna adesões por medicamento", tags = "Retorno de Informações")
    public ResponseEntity<List<AdesaoMedicamentoDTO>> retornarPorMedicamento(@PathVariable Long idMedicamento) {
        return ResponseEntity.ok(cachingService.findByMedicamentoId(idMedicamento).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna adesões por status", summary = "Retorna adesões por status", tags = "Retorno de Informações")
    @GetMapping("/status")
    public ResponseEntity<List<AdesaoMedicamentoDTO>> retornarPorStatus(@RequestParam SimNaoEnum status) {
        return ResponseEntity.ok(cachingService.findByStatus(status).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Inserir nova adesão", summary = "Inserir Adesao", tags = "Inserção de Informações")
    @PostMapping("/novo")
    public ResponseEntity<AdesaoMedicamentoDTO> inserir(@RequestBody @Valid AdesaoMedicamentoDTO dto) {
        AdesaoMedicamento salvo = repository.save(mapper.toEntity(dto));
        cachingService.removerCache();
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @Operation(description = "Remover adesão", summary = "Remover Adesao", tags = "Remoção de Informações")
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Optional<AdesaoMedicamento> op = repository.findById(id);
        if (op.isPresent()) {
            repository.delete(op.get());
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Atualizar adesão", summary = "Atualizar Adesao", tags = "Atualização de Informações")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<AdesaoMedicamentoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AdesaoMedicamentoDTO dto) {
        Optional<AdesaoMedicamento> op = cachingService.findById(id);
        if (op.isPresent()) {
            AdesaoMedicamento antigo = op.get();
            AdesaoMedicamento novo = mapper.toEntity(dto);
            antigo.transferir(novo);
            repository.save(antigo);
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDTO(antigo));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}