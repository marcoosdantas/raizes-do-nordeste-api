# Plano de Testes - Raízes do Nordeste API

Este plano cobre o fluxo principal da API: autenticação, autorização por perfil,
pedido multicanal, pagamento mock, estoque, consulta por canal e fidelidade.

| ID | Cenário | Endpoint + Método | Pré-condição | Entrada | Status Esperado | Trecho do Response | Evidência (pasta Postman) |
|---|---|---|---|---|---|---|---|
| T01 | Login válido | `POST /auth/login` | Seed aplicado | `{ "email": "cliente@raizes.com", "senha": "Cliente@123" }` | 200 | `accessToken` preenchido e `usuario.perfil = CLIENTE` | Auth / T01 - Login válido |
| T02 | Acesso sem token | `GET /pedidos` | API em execução | Sem header `Authorization` | 401 | `error = NAO_AUTENTICADO` | Erros / T02 - Sem token |
| T03 | Acesso com role errada | `POST /unidades` | Token de CLIENTE | Unidade válida no body | 403 | `error = ACESSO_NEGADO` | Erros / T03 - Role inválida |
| T04 | Canal do pedido ausente | `POST /pedidos` | Token de CLIENTE | Pedido sem o campo `canalPedido` | 422 | `details[0].field = canalPedido` | Erros / T04 - Canal ausente |
| T05 | Estoque insuficiente | `POST /pedidos` | Token de CLIENTE e estoque menor que a quantidade solicitada | Pedido com quantidade acima do estoque disponível | 409 | `error = ESTOQUE_INSUFICIENTE` | Erros / T05 - Estoque insuficiente |
| T06 | Pedido válido criado | `POST /pedidos` | Token de CLIENTE, unidade ativa e produto com estoque | Pedido com `canalPedido = APP`, `unidadeId`, `produtoId`, quantidade e forma de pagamento | 201 | `status = AGUARDANDO_PAGAMENTO` e `canalPedido = APP` | Pedidos / T06 - Criar pedido válido |
| T07 | Pagamento aprovado | `POST /pagamentos/mock/callback` | Pedido aguardando pagamento e pagamento pendente criado | `{ "pedidoId": "{{pedidoId}}", "aprovado": true }` | 200 | `status = APROVADO`; na consulta do pedido, status passa para `PAGO` | Pagamento / T07 - Callback aprovado |
| T08 | Pagamento recusado | `POST /pagamentos/mock/callback` | Novo pedido aguardando pagamento e pagamento pendente criado | `{ "pedidoId": "{{pedidoId}}", "aprovado": false }` | 200 | `status = RECUSADO`; pedido permanece `AGUARDANDO_PAGAMENTO` | Pagamento / T08 - Callback recusado |
| T09 | Filtro por canalPedido | `GET /pedidos?canalPedido=APP` | Token de GERENTE e pedidos cadastrados | Query param `canalPedido=APP` | 200 | Itens retornados com `canalPedido = APP` | Pedidos / T09 - Filtro por canalPedido |
| T10 | Resgate sem saldo | `POST /fidelidade/resgatar` | Token de CLIENTE sem pontos suficientes | `{ "pontos": 100 }` | 400 | `error = INVALID_REQUEST` e mensagem de saldo insuficiente | Erros / T10 - Saldo insuficiente |

## Observações De Execução

- Para os testes T06, T07 e T08, os IDs de unidade, produto e pedido devem ser
  preenchidos a partir das respostas das requisições anteriores.
- O teste T05 pode ser reproduzido usando uma quantidade maior que o estoque
  disponível ou após uma saída de estoque que reduza a quantidade do produto.
- O teste T10 usa o comportamento atual da API: saldo insuficiente é tratado
  como requisição inválida, retornando HTTP 400.
