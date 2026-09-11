package com.fiap.pettrack.control;

import com.fiap.pettrack.dto.ProtocoloPreventivoDTO;
import com.fiap.pettrack.mapper.IProtocoloPreventivoMapper;
import com.fiap.pettrack.model.ProtocoloPreventivo;
import com.fiap.pettrack.model.enums.TipoProtocoloPreventivoEnum;
import com.fiap.pettrack.repository.IProtocoloPreventivoRepository;
import com.fiap.pettrack.service.ProtocoloPreventivoCachingService;
import com.fiap.pettrack.service.ProtocoloPreventivoPaginacaoService;
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
@RequestMapping(value = "/protocolo")
public class ProtocoloPreventivoController {

    @Autowired
    private IProtocoloPreventivoRepository repository;
    @Autowired
    private IProtocoloPreventivoMapper mapper;
    @Autowired
    private ProtocoloPreventivoCachingService cachingService;
    @Autowired
    private ProtocoloPreventivoPaginacaoService paginacaoService;

    @Operation(description = "Retorna todos os protocolos", summary = "Retorna ProtocoloPreventivoDTO", tags = "Retorno de Informações")
    @GetMapping("/todos")
    public ResponseEntity<List<ProtocoloPreventivoDTO>> retornarTodos() {
        return ResponseEntity.ok(cachingService.findAll().stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna protocolos paginados", summary = "Retorna ProtocoloPreventivoDTO paginado", tags = "Retorno de Informações")
    @GetMapping("/paginar")
    public ResponseEntity<Page<ProtocoloPreventivoDTO>> paginar(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size) {
        PageRequest request = PageRequest.of(page, size);
        return ResponseEntity.ok(paginacaoService.paginar(request).map(mapper::toDTO));
    }

    @Operation(description = "Retorna protocolo por ID", summary = "Retorna ProtocoloPreventivo por ID", tags = "Retorno de Informações")
    @GetMapping("/{id}")
    public ResponseEntity<ProtocoloPreventivoDTO> retornarPorId(@PathVariable Long id) {
        Optional<ProtocoloPreventivo> op = cachingService.findById(id);
        if (op.isPresent()) {
            return ResponseEntity.ok(mapper.toDTO(op.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(description = "Retorna protocolos por tipo", summary = "Protocolos por tipo", tags = "Retorno de Informações")
    @GetMapping("/tipo")
    public ResponseEntity<List<ProtocoloPreventivoDTO>> retornarPorTipo(@RequestParam TipoProtocoloPreventivoEnum tipo) {
        return ResponseEntity.ok(cachingService.findByTipo(tipo).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna protocolos pendentes ou atrasados por pet", summary = "Pendentes ou atrasados por pet", tags = "Retorno de Informações")
    @GetMapping("/pendentes/{idPet}")
    public ResponseEntity<List<ProtocoloPreventivoDTO>> buscarPendentesOuAtrasadosPorPet(@PathVariable Long idPet) {
        return ResponseEntity.ok(cachingService.buscarPendentesOuAtrasadosPorPet(idPet).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Inserir novo protocolo", summary = "Inserir ProtocoloPreventivo", tags = "Inserção de Informações")
    @PostMapping("/novo")
    public ResponseEntity<ProtocoloPreventivoDTO> inserir(@RequestBody @Valid ProtocoloPreventivoDTO dto) {
        ProtocoloPreventivo salvo = repository.save(mapper.toEntity(dto));
        cachingService.removerCache();
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @Operation(description = "Remover protocolo", summary = "Remover ProtocoloPreventivo", tags = "Remoção de Informações")
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Optional<ProtocoloPreventivo> op = repository.findById(id);
        if (op.isPresent()) {
            repository.delete(op.get());
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Atualizar protocolo", summary = "Atualizar ProtocoloPreventivo", tags = "Atualização de Informações")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ProtocoloPreventivoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProtocoloPreventivoDTO dto) {
        Optional<ProtocoloPreventivo> op = cachingService.findById(id);
        if (op.isPresent()) {
            ProtocoloPreventivo antigo = op.get();
            ProtocoloPreventivo novo = mapper.toEntity(dto);
            antigo.transferir(novo);
            repository.save(antigo);
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDTO(antigo));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}