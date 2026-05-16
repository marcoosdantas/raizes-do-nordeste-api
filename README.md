# Raízes do Nordeste - API Back-end

API REST desenvolvida para o estudo de caso da rede de lanchonetes Raízes do
Nordeste. O projeto cobre cadastro de usuários, unidades franqueadas, produtos,
estoque local, pedidos multicanal, pagamento mock, pontos de fidelidade e
auditoria de ações sensíveis.

## Requisitos

- Java 25 LTS
- Maven 3.9+
- MongoDB local na porta `27017`

## Configuração

Crie o arquivo de ambiente a partir do exemplo versionado:

```bash
cp .env.example .env
```

Variáveis usadas pela aplicação:

```env
MONGODB_URI=mongodb://localhost:27017/raizesdonordeste
JWT_SECRET=sua-chave-secreta-com-minimo-256-bits
```

O projeto também possui valores padrão em `application.properties`, então a API
consegue subir em ambiente local mesmo sem arquivo `.env`, desde que o MongoDB
esteja disponível.

## Execução

```bash
mvn clean install
mvn spring-boot:run
```

A aplicação sobe em:

```text
http://localhost:8080
```

## Swagger

A documentação interativa fica em:

```text
http://localhost:8080/swagger-ui.html
```

Para testar rotas protegidas no Swagger:

1. faça login em `POST /auth/login`;
2. copie o valor de `accessToken`;
3. clique em **Authorize**;
4. informe `Bearer {accessToken}`.

## Seed De Dados

O seed roda apenas quando a coleção `usuarios` está vazia. Ele cria usuários,
unidades, produtos e estoque inicial.

Credenciais disponíveis:

| Perfil | E-mail | Senha |
|---|---|---|
| ADMIN | `admin@raizes.com` | `Admin@123` |
| GERENTE | `gerente@raizes.com` | `Gerente@123` |
| CLIENTE | `cliente@raizes.com` | `Cliente@123` |

## Decisões De Implementação

- Usei MongoDB porque o domínio possui documentos naturais, como pedido com
  itens embutidos e pagamento com payload flexível do gateway mock.
- A API não retorna entidades diretamente. As respostas passam por DTOs para
  evitar exposição de campos sensíveis, principalmente `senhaHash`.
- O pagamento externo foi simulado com `PaymentMockService`, mantendo o fluxo
  principal testável sem depender de um provedor real.
- O estoque só é deduzido após callback de pagamento aprovado. Na criação do
  pedido existe apenas validação de disponibilidade.
- A autenticação usa JWT com claims de `id`, `perfil` e `sub` com e-mail do
  usuário. A autorização é aplicada por roles nos controllers.
- O cadastro exige consentimento LGPD e grava `dataConsentimento`.
- As ações críticas são registradas em auditoria: criação/cancelamento de
  pedido, alteração de status, login, pagamento e resgate de pontos.
- Virtual threads estão habilitadas para reduzir o custo de concorrência em
  operações bloqueantes de I/O, como MongoDB e mock de pagamento.

## Estrutura Do Projeto

```text
src/main/java/com/raizesdonordeste/api/
  domain/
    model/          documentos MongoDB e objetos embutidos
    enums/          enums do domínio
    exception/      exceções de negócio
  application/
    service/        regras de aplicação e orquestração dos fluxos
    dto/
      request/      contratos de entrada
      response/     contratos de saída
  infrastructure/
    repository/     Spring Data MongoDB
    security/       JWT, filtro, UserDetails, Swagger e SecurityConfig
    audit/          serviço de auditoria
    payment/        gateway mock
    seed/           carga inicial de dados
  api/
    controller/     endpoints REST
    handler/        tratamento global de erros
```

## Coleção Postman

Arquivo na raiz do projeto:

```text
raizes-postman-collection.json
```

O plano detalhado dos cenários está em:

```text
PLANO_DE_TESTES.md
```

Variáveis usadas na coleção:

| Variável | Valor inicial |
|---|---|
| `baseUrl` | `http://localhost:8080` |
| `token` | preenchido no login cliente para compatibilidade |
| `tokenCliente` | preenchido nos logins de CLIENTE |
| `tokenAdmin` | preenchido nos logins de ADMIN |
| `tokenGerente` | preenchido nos logins de GERENTE |
| `tokenPrivilegiado` | preenchido nos logins de ADMIN ou GERENTE |
| `unidadeId` | preenchido durante os CTs que consultam unidades |
| `produtoId` | preenchido durante os CTs que consultam produtos |
| `pedidoId` | preenchido durante os CTs que criam pedidos |
| `pagamentoId` | preenchido durante os CTs de pagamento |

### Execução dos CTs no Postman

A collection possui uma pasta específica chamada:

```text
CTs - Plano de Testes
```

Dentro dela há subpastas de `CT01` a `CT13`, correspondentes aos cenários
descritos no `PLANO_DE_TESTES.md`.

Cada subpasta foi organizada para ser executada isoladamente. As requisições
internas estão numeradas na ordem recomendada. Por exemplo, o
`CT07 - Pagamento aprovado` executa login, busca unidade, busca produto, cria
pedido, solicita pagamento e executa o callback aprovado.

Cenários disponíveis:

1. `CT01 - Login válido`
2. `CT02 - Acesso sem token`
3. `CT03 - Acesso com perfil sem permissão`
4. `CT04 - Canal do pedido ausente`
5. `CT05 - Estoque insuficiente`
6. `CT06 - Pedido válido criado`
7. `CT07 - Pagamento aprovado`
8. `CT08 - Pagamento recusado`
9. `CT09 - Filtro por canalPedido`
10. `CT10 - Resgate sem saldo`
11. `CT11 - Pedido com produto inexistente`
12. `CT12 - Pedido com quantidade inválida`
13. `CT13 - Registro de auditoria após pagamento aprovado`

Observação: o `CT13` não possui endpoint de consulta de auditoria. Após executar
o callback aprovado, a evidência deve ser conferida no MongoDB, na coleção
`auditoria`, procurando registro relacionado a `PAGAMENTO_APROVADO`.

## Limitações Conhecidas

- O gateway de pagamento é propositalmente mockado.
- A coleção Postman cobre os cenários exigidos, mas os testes automatizados de
  integração ainda podem ser ampliados.
- O ambiente local precisa de MongoDB em execução para validar os fluxos ponta a
  ponta.

## Repositório

https://github.com/marcoosdantas/raizes-do-nordeste-api
