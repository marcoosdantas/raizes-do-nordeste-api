# Plano de Testes - Raízes do Nordeste API

Este plano registra os cenários usados para validar a API do projeto Raízes do Nordeste.
Os testes foram organizados para cobrir autenticação, autorização por perfil, pedido
multicanal, validações de entrada, estoque, pagamento mock, filtro por canal, fidelidade
e evidência de auditoria.

A evidência executável está na collection `raizes-postman-collection.json`, na pasta
`CTs - Plano de Testes`. Cada cenário possui uma subpasta própria (`CT01` a `CT13`) com
as requisições numeradas na ordem recomendada de execução.

## Premissas para execução

- API em execução em `http://localhost:8080`.
- MongoDB disponível em `mongodb://localhost:27017/raizesdonordeste`.
- Seed inicial aplicado.
- Variáveis da collection mantidas:
  `baseUrl`, `token`, `tokenCliente`, `tokenAdmin`, `tokenGerente`,
  `tokenPrivilegiado`, `unidadeId`, `produtoId`, `pedidoId` e `pagamentoId`.

## Cenários de teste

| ID | Cenário | Endpoint principal | Pré-condição | Entrada principal | Status esperado | Trecho esperado da resposta | Evidência na collection |
|---|---|---|---|---|---|---|---|
| CT01 | Login válido | `POST /auth/login` | Seed aplicado | E-mail e senha do cliente seedado | 200 | `accessToken` preenchido | `CTs - Plano de Testes / CT01 - Login válido` |
| CT02 | Acesso sem token | `GET /pedidos` | API em execução | Sem header `Authorization` | 401 | `error = NAO_AUTENTICADO` | `CTs - Plano de Testes / CT02 - Acesso sem token` |
| CT03 | Acesso com perfil sem permissão | `POST /unidades` | Token de CLIENTE | Body válido de unidade | 403 | `error = ACESSO_NEGADO` | `CTs - Plano de Testes / CT03 - Acesso com perfil sem permissão` |
| CT04 | Canal do pedido ausente | `POST /pedidos` | Token de CLIENTE, unidade e produto válidos | Pedido sem `canalPedido` | 400 ou 422 | Erro contendo `canalPedido` | `CTs - Plano de Testes / CT04 - Canal do pedido ausente` |
| CT05 | Estoque insuficiente | `POST /pedidos` | Token de CLIENTE, unidade e produto válidos | Pedido com quantidade acima do estoque disponível | 409 | `error = ESTOQUE_INSUFICIENTE` | `CTs - Plano de Testes / CT05 - Estoque insuficiente` |
| CT06 | Pedido válido criado | `POST /pedidos` | Token de CLIENTE, unidade e produto válidos | Pedido com `canalPedido = APP` | 201 | `status = AGUARDANDO_PAGAMENTO` e `canalPedido = APP` | `CTs - Plano de Testes / CT06 - Pedido válido criado` |
| CT07 | Pagamento aprovado | `POST /pagamentos/mock/callback` | Pedido criado e pagamento pendente solicitado | `{ "pedidoId": "{{pedidoId}}", "aprovado": true }` | 200 | `status = APROVADO` | `CTs - Plano de Testes / CT07 - Pagamento aprovado` |
| CT08 | Pagamento recusado | `POST /pagamentos/mock/callback` | Pedido criado e pagamento pendente solicitado | `{ "pedidoId": "{{pedidoId}}", "aprovado": false }` | 200 | `status = RECUSADO` | `CTs - Plano de Testes / CT08 - Pagamento recusado` |
| CT09 | Filtro por canalPedido | `GET /pedidos?canalPedido=APP&status=AGUARDANDO_PAGAMENTO&unidadeId={{unidadeId}}&page=0&limit=10` | Token de GERENTE e pedido APP criado | Query params `canalPedido`, `status`, `unidadeId`, `page` e `limit` | 200 | Lista paginada com pedidos do canal `APP` | `CTs - Plano de Testes / CT09 - Filtro por canalPedido` |
| CT10 | Resgate sem saldo | `POST /fidelidade/resgatar` | Token de CLIENTE com saldo insuficiente | `{ "pontos": 100 }` | 400 ou 409 | Mensagem relacionada a saldo insuficiente | `CTs - Plano de Testes / CT10 - Resgate sem saldo` |
| CT11 | Pedido com produto inexistente | `POST /pedidos` | Token de CLIENTE e unidade válida | Pedido com `produtoId = produto-inexistente` | 404 | `error = RECURSO_NAO_ENCONTRADO` | `CTs - Plano de Testes / CT11 - Pedido com produto inexistente` |
| CT12 | Pedido com quantidade inválida | `POST /pedidos` | Token de CLIENTE, unidade e produto válidos | Pedido com `quantidade = 0` | 400 ou 422 | Erro contendo `quantidade` | `CTs - Plano de Testes / CT12 - Pedido com quantidade inválida` |
| CT13 | Registro de auditoria após pagamento aprovado | `POST /pagamentos/mock/callback` | Pedido criado e pagamento pendente solicitado | Callback aprovado | 200 | `status = APROVADO` | `CTs - Plano de Testes / CT13 - Registro de auditoria após pagamento aprovado` |

## Ordem prática de execução

Cada subpasta `CTxx` foi montada para ser executada isoladamente no Postman. As requisições
internas já estão numeradas na sequência recomendada. Por exemplo, o CT07 executa login,
busca unidade, busca produto, cria pedido, solicita pagamento e depois executa o callback
aprovado.

Para os CTs que dependem de produto e unidade, a própria subpasta contém chamadas para
preencher `unidadeId` e `produtoId`. Para os CTs de pagamento, a subpasta cria um novo pedido
e solicita pagamento antes do callback.

## Observações

- O CT11 usa obrigatoriamente `POST /pedidos` com `produtoId` inexistente. Ele não deve ser
  substituído por `GET /produtos/produto-inexistente`, pois o objetivo é validar a regra dentro
  do fluxo de criação do pedido.
- O CT12 valida quantidade inválida no item do pedido. O retorno aceito é 400 ou 422, desde que
  a resposta padronizada indique o problema no campo de quantidade.
- O CT13 comprova a execução de uma ação sensível auditada. Como a API não expõe endpoint de
  auditoria, a evidência complementar deve ser conferida diretamente no MongoDB, na coleção
  `auditoria`, procurando registro relacionado a `PAGAMENTO_APROVADO` após o callback aprovado.
- Alguns testes alteram estado no banco, principalmente os cenários de pagamento e estoque.
  Caso a base fique sem estoque suficiente, recomenda-se reiniciar a base de testes ou repor
  estoque pela rota de entrada.
