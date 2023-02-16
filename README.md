# MarketSystemNG
![Badge em Desenvolvimento](http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge)

### Descrição:

Desenvolver um sistema utilizando Spring framework contendo as funcionalidades impostas pelo stakeholder.

### Requisitos funcionais:

- [x] RF01 - O sistema deve permitir o cadastro de produtos de supermercado,
com foto, nome e descrição.
- [x] RF02 - O sistema deve permitir a desativação de produtos de
supermercado - não é a exclusão, apenas desativação.
- [x] RF03 - O sistema deve permitir o alteração de produtos de
supermercado, com foto, nome e descrição.
- [x] RF04 - O sistema deve permitir o consulta de produtos de supermercado
por nome e descrição.
- [x] RF05 - O sistema deve permitir o cadastro de listas de compra de
mercado, adicionando produtos previamente cadastrados e suas
quantidades.
- [x] RF06 - O sistema deve permitir a atualização de listas de compra de
mercado, removendo produtos previamente adicionados ou alterando
suas quantidades.
- [x] RF07 - O sistema deve permitir excluir listas de mercado já cadastradas.

### Requisitos não funcionais:

- [ ] RNF01 - O sistema deve ter dois perfis: Administrator e Usuário. Os
administradores podem gerir produtos (RF01 ... 04). Os usuários podem
gerir listas (RF05 ... 07).
- [x] RNF02 - Deve ser possível acessar estas funções por meio de interface ou
API. No caso de API, deve ser utilizado um JSON web token. No caso de
interface, deve ser feita autenticação por usuário e senha.

# Novos requisitos - MarketSystemNG

### Requisitos funcionais:

- [x] RF01 - O sistema deve permitir o cadastro de categoria de produtos.
- [x] RF02 - O sistema deve permitir vincular categorias de produtos aos produtos cadastrados.
- [x] RF03 - O sistema deve implementar tratamentos de paginação dinâmica na consulta de  produtos, permitindo informar qual a página e a quantidade de registros por página.
- [x] RF04 - O sistema deve implementar ordem na paginação dinâmica na consulta de produtos.
- [x] RF05 - Deverá ser adicionado a informação de preço nos produtos.
- [x] RF06 - Deverá ser aprimorado o filtro de produtos permitindo busca por nome, descrição e categorias simultaneamente.
- [x] RF07 - Deverá ser aprimorado o filtro de produtos permitindo busca por faixa de preço.
- [x] RF08 - O sistema deve permitir que todos os filtros sejam aplicados ao mesmo tempo.
- [x] RF09 - O sistema deve ter um endpoint para retornar o valor total de uma lista de compras.

### Requisitos não funcionais:

- [x] RNF01 - Deve ser possível acessar estas funções por meio de interface ou API. No caso de API, deve ser utilizado um JSON web token. No caso de interface, deve ser feita autenticação por usuário e senha.

