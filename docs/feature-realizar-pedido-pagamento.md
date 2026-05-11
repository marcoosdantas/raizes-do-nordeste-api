# Feature Crítica - Realizar Pedido e Solicitar Pagamento

## Objetivo

Permitir que um cliente autenticado crie um pedido em uma unidade ativa,
solicite pagamento e tenha o pedido confirmado após callback aprovado do gateway
mock.

## Atores

- Cliente
- Gateway de Pagamento Mock

## Pré-condições

- Cliente autenticado com perfil `CLIENTE`.
- Unidade existente e ativa.
- Produtos existentes.
- Estoque disponível na unidade para todos os itens do pedido.
- Pedido em status `AGUARDANDO_PAGAMENTO` para solicitar pagamento.

## Fluxo Principal

1. Cliente envia `POST /pedidos` com `unidadeId`, `canalPedido`, itens e forma de pagamento.
2. `PedidoService` valida unidade ativa.
3. `PedidoService` valida produto e estoque de cada item.
4. O total é calculado pela soma dos subtotais dos itens.
5. O pedido é salvo com status `AGUARDANDO_PAGAMENTO`.
6. A criação do pedido é registrada em auditoria com `CRIAR_PEDIDO`.
7. Cliente envia `POST /pagamentos/solicitar` com `pedidoId`.
8. `PagamentoService` valida se o pedido pertence ao cliente e está aguardando pagamento.
9. `PaymentMockService` cria um pagamento pendente.
10. A solicitação é registrada em auditoria com `PAGAMENTO_SOLICITADO`.
11. O gateway mock chama `POST /pagamentos/mock/callback`.
12. Se aprovado, o pagamento fica `APROVADO`, o pedido fica `PAGO`, o estoque é deduzido e os pontos são acumulados.
13. Se recusado, o pagamento fica `RECUSADO` e o pedido permanece `AGUARDANDO_PAGAMENTO`.

## Pós-condições

- Pedido criado com itens e total calculado.
- Pagamento registrado.
- Auditoria registrada para pedido e pagamento.
- Se aprovado: estoque deduzido, pedido pago e pontos acumulados.
- Se recusado: pedido permanece aguardando pagamento.

## Exceções

- Unidade inexistente ou inativa: `RecursoNaoEncontradoException`.
- Produto inexistente: `RecursoNaoEncontradoException`.
- Estoque inexistente ou insuficiente: `EstoqueInsuficienteException`.
- Pedido inexistente: `RecursoNaoEncontradoException`.
- Pedido de outro cliente: `AccessDeniedException`.
- Pedido fora de `AGUARDANDO_PAGAMENTO`: `IllegalArgumentException`.
- Pagamento inexistente no callback: `RecursoNaoEncontradoException`.

## Regras De Negócio

- `canalPedido` é obrigatório no DTO de criação de pedido.
- O estoque não é deduzido na criação do pedido.
- A dedução de estoque acontece apenas após callback aprovado.
- Pontuação: 1 ponto para cada R$ 10,00 pagos.
- Resgate de fidelidade exige consentimento LGPD.
- O código atual não implementa idempotência explícita no callback; ele processa
  o pagamento mais recente do pedido.
