package br.com.tarefas.controller;

import br.com.tarefas.model.Tarefa;
import br.com.tarefas.repository.TarefaRepository;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaRepository repository;

    public TarefaController(TarefaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Tarefa> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Tarefa buscarPorId(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> tarefaNaoEncontrada(id));
    }

    @PostMapping
    public ResponseEntity<Tarefa> criar(@Valid @RequestBody Tarefa tarefa) {
        tarefa.setId(null);
        Tarefa salva = repository.save(tarefa);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salva.getId())
                .toUri();
        return ResponseEntity.created(location).body(salva);
    }

    @PutMapping("/{id}")
    public Tarefa atualizar(@PathVariable Long id, @Valid @RequestBody Tarefa dados) {
        Tarefa tarefa = repository.findById(id).orElseThrow(() -> tarefaNaoEncontrada(id));

        tarefa.setDescricao(dados.getDescricao());
        tarefa.setConcluida(dados.getConcluida());
        tarefa.setPrioridade(dados.getPrioridade());
        return repository.save(tarefa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw tarefaNaoEncontrada(id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseStatusException tarefaNaoEncontrada(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa " + id + " nao encontrada");
    }
}
