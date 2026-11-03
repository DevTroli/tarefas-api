package br.com.tarefas.controller;

import br.com.tarefas.model.Tarefa;
import br.com.tarefas.repository.TarefaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tarefas", description = "Cadastro, consulta, atualizacao e remocao de tarefas")
public class TarefaController {

    private final TarefaRepository repository;

    public TarefaController(TarefaRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Lista todas as tarefas")
    @GetMapping
    public List<Tarefa> listar() {
        return repository.findAll();
    }

    @Operation(summary = "Busca uma tarefa pelo id")
    @GetMapping("/{id}")
    public Tarefa buscarPorId(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> tarefaNaoEncontrada(id));
    }

    @Operation(summary = "Cadastra uma nova tarefa", description = "Retorna 400 se a descricao estiver vazia ou se a prioridade nao estiver entre 1 e 5.")
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

    @Operation(summary = "Atualiza uma tarefa existente", description = "Retorna 404 se a tarefa nao existir e 400 se os dados forem invalidos.")
    @PutMapping("/{id}")
    public Tarefa atualizar(@PathVariable Long id, @Valid @RequestBody Tarefa dados) {
        Tarefa tarefa = repository.findById(id).orElseThrow(() -> tarefaNaoEncontrada(id));

        tarefa.setDescricao(dados.getDescricao());
        tarefa.setConcluida(dados.getConcluida());
        tarefa.setPrioridade(dados.getPrioridade());
        return repository.save(tarefa);
    }

    @Operation(summary = "Remove uma tarefa")
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
