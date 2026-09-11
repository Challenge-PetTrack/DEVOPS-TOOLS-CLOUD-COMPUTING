package com.fiap.pettrack.control;

import com.fiap.pettrack.dto.MedicamentoDTO;
import com.fiap.pettrack.mapper.IMedicamentoMapper;
import com.fiap.pettrack.model.Medicamento;
import com.fiap.pettrack.repository.IMedicamentoRepository;
import com.fiap.pettrack.service.MedicamentoCachingService;
import com.fiap.pettrack.service.MedicamentoPaginacaoService;
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
@RequestMapping(value = "/medicamento")
public class MedicamentoController {

    @Autowired
    private IMedicamentoRepository repository;
    @Autowired
    private IMedicamentoMapper mapper;
    @Autowired
    private MedicamentoCachingService cachingService;
    @Autowired
    private MedicamentoPaginacaoService paginacaoService;

    @Operation(description = "Retorna todos os medicamentos", summary = "Retorna MedicamentoDTO", tags = "Retorno de Informações")
    @GetMapping("/todos")
    public ResponseEntity<List<MedicamentoDTO>> retornarTodos() {
        return ResponseEntity.ok(cachingService.findAll().stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna medicamentos paginados", summary = "Retorna MedicamentoDTO paginado", tags = "Retorno de Informações")
    @GetMapping("/paginar")
    public ResponseEntity<Page<MedicamentoDTO>> paginar(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size) {
        PageRequest request = PageRequest.of(page, size);
        return ResponseEntity.ok(paginacaoService.paginar(request).map(mapper::toDTO));
    }

    @Operation(description = "Retorna medicamento por ID", summary = "Retorna Medicamento por ID", tags = "Retorno de Informações")
    @GetMapping("/{id}")
    public ResponseEntity<MedicamentoDTO> retornarPorId(@PathVariable Long id) {
        Optional<Medicamento> op = cachingService.findById(id);
        if (op.isPresent()) {
            return ResponseEntity.ok(mapper.toDTO(op.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(description = "Busca medicamentos por nome", summary = "Busca por nome", tags = "Retorno de Informações")
    @GetMapping("/buscar")
    public ResponseEntity<List<MedicamentoDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(cachingService.findByNomeContainingIgnoreCase(nome).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna medicamentos ativos por pet", summary = "Medicamentos ativos por pet", tags = "Retorno de Informações")
    @GetMapping("/ativos/{idPet}")
    public ResponseEntity<List<MedicamentoDTO>> buscarMedicamentosAtivosPorPet(@PathVariable Long idPet) {
        return ResponseEntity.ok(cachingService.buscarMedicamentosAtivosPorPet(idPet).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Inserir novo medicamento", summary = "Inserir Medicamento", tags = "Inserção de Informações")
    @PostMapping("/novo")
    public ResponseEntity<MedicamentoDTO> inserir(@RequestBody @Valid MedicamentoDTO dto) {
        Medicamento salvo = repository.save(mapper.toEntity(dto));
        cachingService.removerCache();
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @Operation(description = "Remover medicamento", summary = "Remover Medicamento", tags = "Remoção de Informações")
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Optional<Medicamento> op = repository.findById(id);
        if (op.isPresent()) {
            repository.delete(op.get());
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Atualizar medicamento", summary = "Atualizar Medicamento", tags = "Atualização de Informações")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<MedicamentoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid MedicamentoDTO dto) {
        Optional<Medicamento> op = cachingService.findById(id);
        if (op.isPresent()) {
            Medicamento antigo = op.get();
            Medicamento novo = mapper.toEntity(dto);
            antigo.transferir(novo);
            repository.save(antigo);
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDTO(antigo));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}