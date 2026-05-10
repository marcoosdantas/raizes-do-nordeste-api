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

Variáveis usadas na coleção:

| Variável | Valor inicial |
|---|---|
| `baseUrl` | `http://localhost:8080` |
| `token` | preenchido após login |
| `unidadeId` | preenchido durante os testes |
| `produtoId` | preenchido durante os testes |
| `pedidoId` | preenchido durante os testes |

### Ordem sugerida de execução da coleção Postman

1. **Auth / Login válido (T01)** - copiar `accessToken` para `{{token}}`
2. **Produtos / Listar produtos** - copiar um `produtoId`
3. **Unidades / Listar unidades** - copiar um `unidadeId`
4. **Pedidos / Criar pedido (T06)** - usar `produtoId` e `unidadeId`
5. **Pagamento / Solicitar pagamento** - usar o `pedidoId`
6. **Pagamento / Callback aprovado (T07)** - validar pedido como `PAGO`
7. **Pagamento / Callback recusado (T08)** - criar novo pedido e testar recusa
8. **Fidelidade / Consultar saldo** - validar pontos acumulados
9. **Erros / Sem token (T02)** - executar sem header `Authorization`
10. **Erros / Role inválida (T03)** - usar token de CLIENTE em rota de ADMIN

## Limitações Conhecidas

- O gateway de pagamento é propositalmente mockado.
- A coleção Postman cobre os cenários exigidos, mas os testes automatizados de
  integração ainda podem ser ampliados.
- O ambiente local precisa de MongoDB em execução para validar os fluxos ponta a
  ponta.

## Repositório

https://github.com/marcoosdantas/raizes-do-nordeste-api
