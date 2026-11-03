# Tarefas API ✅

API REST para gerenciar tarefas: crie, consulte, atualize, conclua e remova tarefas, cada uma com prioridade de 1 a 5.

Feita com **Java 17**, **Spring Boot 4**, **Spring Data JPA** e **PostgreSQL**.

## Como rodar

Você precisa de **JDK 17**, **Maven** e **Docker**.

```bash
git clone https://github.com/DevTroli/tarefas-api.git
cd tarefas-api
mvn spring-boot:run
```

Pronto. O Spring Boot sobe o PostgreSQL sozinho (via `compose.yaml`) e a API fica em `http://localhost:8080`.

Ao abrir esse endereço no navegador, você cai na **documentação interativa (Swagger)**, onde pode testar cada endpoint.

## O que uma tarefa tem

| Campo | Tipo | Regra |
|---|---|---|
| `id` | número | Gerado automaticamente |
| `descricao` | texto | Obrigatória |
| `concluida` | verdadeiro/falso | Começa como `false` |
| `prioridade` | número | Opcional; se enviada, de 1 a 5 |

## Endpoints

| Ação | Rota | Resposta |
|---|---|---|
| Listar tarefas | `GET /tarefas` | 200 |
| Buscar uma tarefa | `GET /tarefas/{id}` | 200, ou 404 se não existir |
| Criar | `POST /tarefas` | 201, ou 400 (descrição vazia ou prioridade fora de 1 a 5) |
| Atualizar | `PUT /tarefas/{id}` | 200, 400 ou 404 |
| Remover | `DELETE /tarefas/{id}` | 204, ou 404 se não existir |

Exemplo de corpo para criar ou atualizar:

```json
{
  "descricao": "Estudar Spring Boot",
  "concluida": false,
  "prioridade": 3
}
```

## Como testar

**Pelo navegador:** abra `http://localhost:8080` e use o botão **Try it out** em cada endpoint.

**Pelo Postman:**

1. No Postman, clique em **Import** e escolha o arquivo `postman/tarefas-api.postman_collection.json`.
2. Com a API rodando, clique na coleção **Tarefas API**, depois em **Run** (Collection Runner) e em **Run Tarefas API**.
3. Veja os 8 passos executarem em ordem, cada um com o resultado do teste.

Os passos criam uma tarefa, tentam criar com prioridade 6 (400) e com descrição vazia (400), listam, buscam, atualizam, removem e confirmam que a tarefa removida não existe mais (404). Cada passo já verifica o status esperado.

**Direto no banco:**

```bash
docker exec -it tarefas-postgres psql -U tarefas -d tarefas
```

Dentro do `psql`, rode `SELECT * FROM tarefas;`.

## Estrutura

```text
src/main/java/br/com/tarefas/
├── controller/   # rotas da API (TarefaController) e redirecionamento da raiz
├── model/        # entidade Tarefa
├── repository/   # acesso ao banco (TarefaRepository)
└── config/       # configuração da documentação
```

## Testes automatizados

```bash
mvn test
```

Os testes usam um banco H2 em memória, então não precisam do Docker.
