# Raízes do Nordeste — API Back-end

## Requisitos
- Java 25 LTS
- MongoDB rodando na porta 27017
- Maven 3.9+

## Configuração
cp .env.example .env
# edite as variáveis conforme necessário

## Variáveis de ambiente (.env.example)
MONGODB_URI=mongodb://localhost:27017/raizesdonordeste
JWT_SECRET=sua-chave-secreta-com-minimo-256-bits

## Instalação e execução
mvn clean install
mvn spring-boot:run

## Documentação da API
Acesse: http://localhost:8080/swagger-ui.html
- Clique em "Authorize" e informe: Bearer {seu_token}
- Faça login em POST /auth/login para obter o token

## Seed de dados
O seed executa automaticamente na primeira inicialização.
Credenciais disponíveis:
- Admin:    admin@raizes.com    / Admin@123
- Gerente:  gerente@raizes.com  / Gerente@123
- Cliente:  cliente@raizes.com  / Cliente@123

## Estrutura do projeto
src/main/java/com/raizesdonordeste/api/
  domain/       → entidades, enums, exceções de negócio
  application/  → services, DTOs
  infrastructure/ → repositories, security, audit, payment, seed
  api/          → controllers, exception handler

## Coleção Postman
Arquivo: raizes-postman-collection.json (na raiz do projeto)
Variáveis: baseUrl=http://localhost:8080, token={obtido no login}

### Ordem sugerida de execução da coleção Postman
1. **Auth / Login válido (T01)** → executar, copiar valor de `accessToken`, 
   colar na variável de ambiente `{{token}}`
2. **Produtos / Listar produtos** → copiar um `produtoId` da resposta
3. **Unidades / Listar unidades** → copiar um `unidadeId` da resposta
4. **Pedidos / Criar pedido (T06)** → usar produtoId e unidadeId acima, 
   copiar `pedidoId` da resposta
5. **Pagamento / Solicitar pagamento** → usar pedidoId acima
6. **Pagamento / Callback aprovado (T07)** → confirmar que pedido virou PAGO
7. **Pagamento / Callback recusado (T08)** → criar novo pedido e testar recusa
8. **Fidelidade / Consultar saldo** → verificar pontos acumulados
9. **Erros / Sem token (T02)** → sem Authorization header
10. **Erros / Role inválida (T03)** → usar token de CLIENTE em rota de ADMIN

Link do repositório: https://github.com/SEU_USUARIO/raizes-do-nordeste-api
(substituir após publicar)
