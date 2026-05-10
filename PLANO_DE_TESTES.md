# Plano de Testes — Raízes do Nordeste API

| ID  | Cenário | Endpoint + Método | Pré-condição | Entrada | Status Esperado | Trecho do Response | Evidência (pasta Postman) |
|-----|---------|-------------------|--------------|---------|-----------------|-------------------|--------------------------|
| T01 | Login válido | POST /auth/login | Seed aplicado | { "email": "cliente@raizes.com", "senha": "Cliente@123" } | 200 | accessToken presente | Auth/Login válido |
| T02 | Acesso sem token | GET /pedidos | — | sem Authorization header | 401 | error: NAO_AUTENTICADO | Erros/Sem token |
| T03 | Acesso com role errada | POST /unidades | Token de CLIENTE | body de unidade válido | 403 | error: ACESSO_NEGADO | Erros/Role inválida |
| T04 | canalPedido ausente | POST /pedidos | Token de CLIENTE | body sem campo canalPedido | 422 | field: canalPedido | Erros/Canal ausente |
| T05 | Estoque insuficiente | POST /pedidos | Token de CLIENTE, produto com estoque=0 | pedido com quantidade > estoque | 409 | error: ESTOQUE_INSUFICIENTE | Erros/Estoque |
| T06 | Pedido válido criado | POST /pedidos | Token de CLIENTE, seed aplicado | pedido com canalPedido=APP e itens válidos | 201 | status: AGUARDANDO_PAGAMENTO | Pedidos/Criar pedido |
| T07 | Pagamento mock aprovado | POST /pagamentos/mock/callback | Pedido criado | { pedidoId, aprovado: true } | 200 | Pedido.status = PAGO | Pagamento/Callback aprovado |
| T08 | Pagamento mock recusado | POST /pagamentos/mock/callback | Pedido criado | { pedidoId, aprovado: false } | 200 | Pagamento.status = RECUSADO | Pagamento/Callback recusado |
| T09 | Filtro por canalPedido | GET /pedidos?canalPedido=APP | Token GERENTE, pedidos existentes | query param canalPedido=APP | 200 | lista só com APP | Pedidos/Filtro canal |
| T10 | Resgate sem saldo | POST /fidelidade/resgatar | Token CLIENTE, saldo=0 | body de resgate | 409 ou 400 | error coerente | Erros/Saldo insuficiente |
