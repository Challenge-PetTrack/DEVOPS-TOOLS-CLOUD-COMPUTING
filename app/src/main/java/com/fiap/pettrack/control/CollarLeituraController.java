package com.fiap.pettrack.control;

import com.fiap.pettrack.dto.CollarLeituraDTO;
import com.fiap.pettrack.mapper.ICollarLeituraMapper;
import com.fiap.pettrack.model.CollarLeitura;
import com.fiap.pettrack.repository.ICollarLeituraRepository;
import com.fiap.pettrack.service.CollarLeituraCachingService;
import com.fiap.pettrack.service.CollarLeituraPaginacaoService;
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
@RequestMapping(value = "/collar")
public class CollarLeituraController {

    @Autowired
    private ICollarLeituraRepository repository;
    @Autowired
    private ICollarLeituraMapper mapper;
    @Autowired
    private CollarLeituraCachingService cachingService;
    @Autowired
    private CollarLeituraPaginacaoService paginacaoService;

    @Operation(description = "Retorna todas as leituras", summary = "Retorna CollarLeituraDTO", tags = "Retorno de Informações")
    @GetMapping("/todos")
    public ResponseEntity<List<CollarLeituraDTO>> retornarTodos() {
        return ResponseEntity.ok(cachingService.findAll().stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna leituras paginadas", summary = "Retorna CollarLeituraDTO paginado", tags = "Retorno de Informações")
    @GetMapping("/paginar")
    public ResponseEntity<Page<CollarLeituraDTO>> paginar(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size) {
        PageRequest request = PageRequest.of(page, size);
        return ResponseEntity.ok(paginacaoService.paginar(request).map(mapper::toDTO));
    }

    @Operation(description = "Retorna leitura por ID", summary = "Retorna CollarLeitura por ID", tags = "Retorno de Informações")
    @GetMapping("/{id}")
    public ResponseEntity<CollarLeituraDTO> retornarPorId(@PathVariable Long id) {
        Optional<CollarLeitura> op = cachingService.findById(id);
        if (op.isPresent()) {
            return ResponseEntity.ok(mapper.toDTO(op.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(description = "Busca leituras por pet e temperatura acima de", summary = "Leituras por temperatura", tags = "Retorno de Informações")
    @GetMapping("/temperatura")
    public ResponseEntity<List<CollarLeituraDTO>> buscarPorPetETemperatura(
            @RequestParam Long idPet,
            @RequestParam Double temperatura) {
        return ResponseEntity.ok(cachingService.buscarPorPetETemperaturaAcimaDe(idPet, temperatura).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna última leitura por pet", summary = "Última leitura por pet", tags = "Retorno de Informações")
    @GetMapping("/ultima/{idPet}")
    public ResponseEntity<CollarLeituraDTO> buscarUltimaLeituraPorPet(@PathVariable Long idPet) {
        CollarLeitura leitura = cachingService.buscarUltimaLeituraPorPet(idPet);
        if (leitura != null) {
            return ResponseEntity.ok(mapper.toDTO(leitura));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(description = "Inserir nova leitura", summary = "Inserir CollarLeitura", tags = "Inserção de Informações")
    @PostMapping("/novo")
    public ResponseEntity<CollarLeituraDTO> inserir(@RequestBody @Valid CollarLeituraDTO dto) {
        CollarLeitura salvo = repository.save(mapper.toEntity(dto));
        cachingService.removerCache();
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @Operation(description = "Remover leitura", summary = "Remover CollarLeitura", tags = "Remoção de Informações")
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Optional<CollarLeitura> op = repository.findById(id);
        if (op.isPresent()) {
            repository.delete(op.get());
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Atualizar leitura", summary = "Atualizar CollarLeitura", tags = "Atualização de Informações")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<CollarLeituraDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid CollarLeituraDTO dto) {
        Optional<CollarLeitura> op = cachingService.findById(id);
        if (op.isPresent()) {
            CollarLeitura antigo = op.get();
            CollarLeitura novo = mapper.toEntity(dto);
            antigo.transferir(novo);
            repository.save(antigo);
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDTO(antigo));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}