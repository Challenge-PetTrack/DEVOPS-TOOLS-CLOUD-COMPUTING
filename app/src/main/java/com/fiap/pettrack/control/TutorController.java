package com.fiap.pettrack.control;

import com.fiap.pettrack.dto.TutorDTO;
import com.fiap.pettrack.mapper.ITutorMapper;
import com.fiap.pettrack.model.Tutor;
import com.fiap.pettrack.repository.ITutorRepository;
import com.fiap.pettrack.service.TutorCachingService;
import com.fiap.pettrack.service.TutorPaginacaoService;
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
@RequestMapping(value = "/tutor")
public class TutorController {

    @Autowired
    private ITutorRepository repository;
    @Autowired
    private ITutorMapper mapper;
    @Autowired
    private TutorCachingService cachingService;
    @Autowired
    private TutorPaginacaoService paginacaoService;

    @Operation(description = "Este endpoint retorna todos os tutores", summary = "Retorna TutorDTO", tags = "Retorno de Informações")
    @GetMapping("/todos")
    public ResponseEntity<List<TutorDTO>> retornarTodosDTO(){
        return ResponseEntity.ok(cachingService.findAll().stream().map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Este endpooint retorna todos tutores de forma paginada", summary = "Retorna TutorDTO", tags = "Retorno de Informação")
    @GetMapping("/paginar")
    public ResponseEntity<Page<TutorDTO>> paginar(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "5") Integer size){
        PageRequest request = PageRequest.of(page, size);
        return ResponseEntity.ok(paginacaoService.paginar(request).map(mapper::toDTO));
    }

    @Operation(description = "Este endpoint retorna Tutor por ID",
            summary = "Retorna Tutor por ID",
            tags = "Retorno de Informações")
    @GetMapping(value = "/{id}")
    public ResponseEntity<TutorDTO> retornarTutorPorID(@PathVariable Long id) {
        Optional<Tutor> op = cachingService.findById(id);
        if (op.isPresent()) {
            return ResponseEntity.ok(mapper.toDTO(op.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(description = "Este endpoint retorna Tutor por nome de seus pets cadastrado",
            summary = "Retorna Tutor por nome pet",
            tags = "Retorno de Informações")
    @GetMapping("/nomePet")
    public ResponseEntity<List<TutorDTO>> buscarTutorPorNomePet(@RequestParam String nomePet){
        return ResponseEntity.ok(cachingService.buscarTutorPorNomePet(nomePet).stream().map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Este endpoint retorna Tutor por nome ou email",
            summary = "Retorna Tutor por nome ou email",
            tags = "Retorno de Informações")
    @GetMapping("/nomeOuEmail")
    public ResponseEntity<List<TutorDTO>> buscarPorNomeOuEmail(@RequestParam String busca){
        return ResponseEntity.ok(cachingService.buscarPorNomeOuEmail(busca).stream().map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(description = "Este endpoint realiza a inserção de uma novo Tutor", summary = "Inserir nova Tutor", tags = "Inserção de Informações")
    @PostMapping(value = "/novo")
    public ResponseEntity<TutorDTO> inserirTutor(@RequestBody @Valid TutorDTO tutorDTO) {
        Tutor salvo = repository.save(mapper.toEntity(tutorDTO));
        cachingService.removerCache();
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salvo));
    }

    @Operation(description = "Este endpoint realiza a remoção de um Tutor", summary = "Remover Tutor", tags = "Remoção de Informações")
    @DeleteMapping(value = "/remover/{id}")
    public ResponseEntity<TutorDTO> removerTutor(@PathVariable Long id) {

        Optional<Tutor> op = repository.findById(id);

        if (op.isPresent()) {
            repository.delete(op.get());
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Este endpoint realiza a atualização de informações de Tutor", summary = "Atualizar Tutor", tags = "Atualização de Informações")
    @PutMapping(value = "/atualizar/{id}")
    public ResponseEntity<TutorDTO> atualizarTutor(@PathVariable Long id, @RequestBody @Valid TutorDTO tutorDTO) {
        Optional<Tutor> op = cachingService.findById(id);
        if (op.isPresent()) {
            Tutor tutorAntigo = op.get();
            Tutor tutorNovo = mapper.toEntity(tutorDTO);
            tutorAntigo.transferir(tutorNovo);
            repository.save(tutorAntigo);
            cachingService.removerCache();
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toDTO(tutorAntigo));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
