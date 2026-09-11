package com.fiap.pettrack.control;

import com.fiap.pettrack.dto.NotificacaoDTO;
import com.fiap.pettrack.mapper.INotificacaoMapper;
import com.fiap.pettrack.model.Notificacao;
import com.fiap.pettrack.model.enums.SimNaoEnum;
import com.fiap.pettrack.model.enums.TipoNotificacaoEnum;
import com.fiap.pettrack.repository.INotificacaoRepository;
import com.fiap.pettrack.service.NotificacaoCachingService;
import com.fiap.pettrack.service.NotificacaoPaginacaoService;
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
@RequestMapping(value = "/notificacao")
public class NotificacaoController {

    @Autowired
    private INotificacaoRepository repository;
    @Autowired
    private INotificacaoMapper mapper;
    @Autowired
    private NotificacaoCachingService cachingService;
    @Autowired
    private NotificacaoPaginacaoService paginacaoService;

    @Operation(description = "Retorna todas as notificações", summary = "Retorna NotificacaoDTO", tags = "Retorno de Informações")
    @GetMapping("/todos")
    public ResponseEntity<List<NotificacaoDTO>> retornarTodos() {
        return ResponseEntity.ok(cachingService.findAll().stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna notificações paginadas", summary = "Retorna NotificacaoDTO paginado", tags = "Retorno de Informações")
    @GetMapping("/paginar")
    public ResponseEntity<Page<NotificacaoDTO>> paginar(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size) {
        PageRequest request = PageRequest.of(page, size);
        return ResponseEntity.ok(paginacaoService.paginar(request).map(mapper::toDTO));
    }

    @Operation(description = "Retorna notificação por ID", summary = "Retorna Notificacao por ID", tags = "Retorno de Informações")
    @GetMapping("/{id}")
    public ResponseEntity<NotificacaoDTO> retornarPorId(@PathVariable Long id) {
        Optional<Notificacao> op = cachingService.findById(id);
        if (op.isPresent()) {
            return ResponseEntity.ok(mapper.toDTO(op.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(description = "Retorna notificações por status", summary = "Notificações por status", tags = "Retorno de Informações")
    @GetMapping("/status")
    public ResponseEntity<List<NotificacaoDTO>> retornarPorStatus(@RequestParam SimNaoEnum status) {
        return ResponseEntity.ok(cachingService.findByStatus(status).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna notificações por tipo", summary = "Notificações por tipo", tags = "Retorno de Informações")
    @GetMapping("/tipo")
    public ResponseEntity<List<NotificacaoDTO>> retornarPorTipo(@RequestParam TipoNotificacaoEnum tipo) {
        return ResponseEntity.ok(cachingService.findByTipo(tipo).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Retorna notificações urgentes não lidas por tutor", summary = "Urgentes não lidas por tutor", tags = "Retorno de Informações")
    @GetMapping("/urgentes/{idTutor}")
    public ResponseEntity<List<NotificacaoDTO>> buscarUrgentesNaoLidasPorTutor(@PathVariable Long idTutor) {
        return ResponseEntity.ok(cachingService.buscarUrgentesNaoLidasPorTutor(idTutor).stream()
                .map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Inserir nova notificação", summary = "Inserir Notificacao", tags = "Inserção de Informações")
    @PostMapping("/novo")
    public ResponseEntity<NotificacaoDTO> inserir(@RequestBody @Valid NotificacaoDTO dto) {
        Notificacao salvo = repository.save(mapper.toEntity(dto));
        cachingService.removerCache();
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @Operation(description = "Remover notificação", summary = "Remover Notificacao", tags = "Remoção de Informações")
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Optional<Notificacao> op = repository.findById(id);
        if (op.isPresent()) {
            repository.delete(op.get());
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Atualizar notificação", summary = "Atualizar Notificacao", tags = "Atualização de Informações")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<NotificacaoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid NotificacaoDTO dto) {
        Optional<Notificacao> op = cachingService.findById(id);
        if (op.isPresent()) {
            Notificacao antigo = op.get();
            Notificacao novo = mapper.toEntity(dto);
            antigo.transferir(novo);
            repository.save(antigo);
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDTO(antigo));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
