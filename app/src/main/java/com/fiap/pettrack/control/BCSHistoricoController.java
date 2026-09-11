package com.fiap.pettrack.control;

import com.fiap.pettrack.dto.BCSHistoricoDTO;
import com.fiap.pettrack.mapper.IBCSHistoricoMapper;
import com.fiap.pettrack.model.BCSHistorico;
import com.fiap.pettrack.repository.IBCSHistoricoRepository;
import com.fiap.pettrack.service.BCSHistoricoCachingService;
import com.fiap.pettrack.service.BCSHistoricoPaginacaoService;
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
@RequestMapping(value = "/bcs")
public class BCSHistoricoController {

    @Autowired
    private IBCSHistoricoRepository repository;
    @Autowired
    private IBCSHistoricoMapper mapper;
    @Autowired
    private BCSHistoricoCachingService cachingService;
    @Autowired
    private BCSHistoricoPaginacaoService paginacaoService;

    @Operation(description = "Retorna todos os BCS", summary = "Retorna BCSHistoricoDTO", tags = "Retorno de Informações")
    @GetMapping("/todos")
    public ResponseEntity<List<BCSHistoricoDTO>> retornarTodos() {
        return ResponseEntity.ok(cachingService.findAll().stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna BCS paginados", summary = "Retorna BCSHistoricoDTO paginado", tags = "Retorno de Informações")
    @GetMapping("/paginar")
    public ResponseEntity<Page<BCSHistoricoDTO>> paginar(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size) {
        PageRequest request = PageRequest.of(page, size);
        return ResponseEntity.ok(paginacaoService.paginar(request).map(mapper::toDTO));
    }

    @Operation(description = "Retorna BCS por ID", summary = "Retorna BCS por ID", tags = "Retorno de Informações")
    @GetMapping("/{id}")
    public ResponseEntity<BCSHistoricoDTO> retornarPorId(@PathVariable Long id) {
        Optional<BCSHistorico> op = cachingService.findById(id);
        if (op.isPresent()) {
            return ResponseEntity.ok(mapper.toDTO(op.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(description = "Retorna histórico BCS por pet", summary = "Histórico BCS por pet", tags = "Retorno de Informações")
    @GetMapping("/historico/{idPet}")
    public ResponseEntity<List<BCSHistoricoDTO>> buscarHistoricoPorPet(@PathVariable Long idPet) {
        return ResponseEntity.ok(cachingService.buscarHistoricoPorPet(idPet).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna média de BCS por pet", summary = "Média BCS por pet", tags = "Retorno de Informações")
    @GetMapping("/media/{idPet}")
    public ResponseEntity<Double> buscarMediaBcsPorPet(@PathVariable Long idPet) {
        return ResponseEntity.ok(cachingService.buscarMediaBcsPorPet(idPet));
    }

    @Operation(description = "Inserir novo BCS", summary = "Inserir BCS", tags = "Inserção de Informações")
    @PostMapping("/novo")
    public ResponseEntity<BCSHistoricoDTO> inserir(@RequestBody @Valid BCSHistoricoDTO dto) {
        BCSHistorico salvo = repository.save(mapper.toEntity(dto));
        cachingService.removerCache();
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @Operation(description = "Remover BCS", summary = "Remover BCS", tags = "Remoção de Informações")
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Optional<BCSHistorico> op = repository.findById(id);
        if (op.isPresent()) {
            repository.delete(op.get());
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Atualizar BCS", summary = "Atualizar BCS", tags = "Atualização de Informações")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<BCSHistoricoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid BCSHistoricoDTO dto) {
        Optional<BCSHistorico> op = cachingService.findById(id);
        if (op.isPresent()) {
            BCSHistorico antigo = op.get();
            BCSHistorico novo = mapper.toEntity(dto);
            antigo.transferir(novo);
            repository.save(antigo);
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDTO(antigo));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}