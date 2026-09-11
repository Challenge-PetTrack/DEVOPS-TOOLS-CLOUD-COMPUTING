package com.fiap.pettrack.control;

import com.fiap.pettrack.dto.ScoreHistoricoDTO;
import com.fiap.pettrack.mapper.IScoreHistoricoMapper;
import com.fiap.pettrack.model.ScoreHistorico;
import com.fiap.pettrack.repository.IScoreHistoricoRepository;
import com.fiap.pettrack.service.ScoreHistoricoCachingService;
import com.fiap.pettrack.service.ScoreHistoricoPaginacaoService;
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
@RequestMapping(value = "/score")
public class ScoreHistoricoController {

    @Autowired
    private IScoreHistoricoRepository repository;
    @Autowired
    private IScoreHistoricoMapper mapper;
    @Autowired
    private ScoreHistoricoCachingService cachingService;
    @Autowired
    private ScoreHistoricoPaginacaoService paginacaoService;

    @Operation(description = "Retorna todos os scores", summary = "Retorna ScoreHistoricoDTO", tags = "Retorno de Informações")
    @GetMapping("/todos")
    public ResponseEntity<List<ScoreHistoricoDTO>> retornarTodos() {
        return ResponseEntity.ok(cachingService.findAll().stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna scores paginados", summary = "Retorna ScoreHistoricoDTO paginado", tags = "Retorno de Informações")
    @GetMapping("/paginar")
    public ResponseEntity<Page<ScoreHistoricoDTO>> paginar(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size) {
        PageRequest request = PageRequest.of(page, size);
        return ResponseEntity.ok(paginacaoService.paginar(request).map(mapper::toDTO));
    }

    @Operation(description = "Retorna score por ID", summary = "Retorna ScoreHistorico por ID", tags = "Retorno de Informações")
    @GetMapping("/{id}")
    public ResponseEntity<ScoreHistoricoDTO> retornarPorId(@PathVariable Long id) {
        Optional<ScoreHistorico> op = cachingService.findById(id);
        if (op.isPresent()) {
            return ResponseEntity.ok(mapper.toDTO(op.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(description = "Retorna histórico de score por pet", summary = "Histórico de score por pet", tags = "Retorno de Informações")
    @GetMapping("/historico/{idPet}")
    public ResponseEntity<List<ScoreHistoricoDTO>> buscarHistoricoPorPet(@PathVariable Long idPet) {
        return ResponseEntity.ok(cachingService.buscarHistoricoPorPet(idPet).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna média de score por pet", summary = "Média de score por pet", tags = "Retorno de Informações")
    @GetMapping("/media/{idPet}")
    public ResponseEntity<Double> buscarMediaScorePorPet(@PathVariable Long idPet) {
        return ResponseEntity.ok(cachingService.buscarMediaScorePorPet(idPet));
    }

    @Operation(description = "Inserir novo score", summary = "Inserir ScoreHistorico", tags = "Inserção de Informações")
    @PostMapping("/novo")
    public ResponseEntity<ScoreHistoricoDTO> inserir(@RequestBody @Valid ScoreHistoricoDTO dto) {
        ScoreHistorico salvo = repository.save(mapper.toEntity(dto));
        cachingService.removerCache();
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @Operation(description = "Remover score", summary = "Remover ScoreHistorico", tags = "Remoção de Informações")
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Optional<ScoreHistorico> op = repository.findById(id);
        if (op.isPresent()) {
            repository.delete(op.get());
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Atualizar score", summary = "Atualizar ScoreHistorico", tags = "Atualização de Informações")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ScoreHistoricoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ScoreHistoricoDTO dto) {
        Optional<ScoreHistorico> op = cachingService.findById(id);
        if (op.isPresent()) {
            ScoreHistorico antigo = op.get();
            ScoreHistorico novo = mapper.toEntity(dto);
            antigo.transferir(novo);
            repository.save(antigo);
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDTO(antigo));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}