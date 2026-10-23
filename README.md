# 🌱 AgroMarket

### Inteligência de mercado para o agronegócio brasileiro

**O AgroMarket é uma plataforma web para consulta, análise e visualização de cotações agrícolas, desenvolvida para transformar dados de mercado em informações úteis para a tomada de decisão no agronegócio.**

O projeto combina desenvolvimento backend, banco de dados relacional, APIs REST e visualização de dados em um dashboard voltado ao acompanhamento de preços de commodities agrícolas.

<p align="center">
  <strong>Java · Spring Boot · MySQL · JavaScript</strong>
</p>

<p align="center">
  <a href="https://github.com/matheusvccarvalho/agromarket">Repositório</a> ·
  <a href="#-funcionalidades">Funcionalidades</a> ·
  <a href="#-roadmap">Roadmap</a> ·
  <a href="#-como-executar">Como executar</a>
</p>

---

## 📌 Sobre o projeto

O agronegócio depende de informações de mercado para acompanhar preços, identificar variações e avaliar cenários comerciais. Entretanto, consultar e interpretar dados de diferentes fontes pode ser trabalhoso.

O AgroMarket surge como um projeto de desenvolvimento de software aplicado a esse contexto, reunindo cotações agrícolas, indicadores estatísticos e histórico de preços em uma interface web.

A versão atual possui uma base inicial de cotações de soja do Paraná. A proposta é evoluir gradualmente para uma plataforma com cobertura regional mais ampla, novas commodities e fontes adicionais de dados.

## 🎯 Objetivos

- Centralizar o acesso às cotações agrícolas disponíveis na base do sistema.
- Facilitar a análise da evolução dos preços ao longo do tempo.
- Calcular indicadores para apoiar a interpretação do mercado.
- Desenvolver uma interface simples para explorar informações agrícolas.
- Aplicar conceitos de engenharia de software, APIs REST, persistência de dados e análise de dados a um problema do agronegócio.

## ✨ Funcionalidades implementadas

### 📊 Dashboard analítico

- Indicadores de preço médio, mínimo e máximo.
- Visualização do histórico de preços.
- Análise da variação absoluta e percentual.
- Tabela com registros de cotações.
- Filtros por commodity, período e unidade de medida.
- Consumo dos dados por meio da API REST do backend.

### 📥 Importação de dados

- Importação de cotações por arquivos CSV.
- Persistência dos registros no banco de dados.
- Disponibilização das cotações importadas para consulta e análise.

### 🔌 API REST

- Consulta de cotações agrícolas.
- Endpoints para indicadores e histórico de preços.
- Cálculo da variação entre cotações.
- Estrutura organizada em controllers, services, repositories, entities e DTOs.

> **Escopo atual:** o projeto está em desenvolvimento. A cobertura regional ainda é limitada e os resultados dependem dos dados cadastrados no banco. O AgroMarket não deve ser interpretado como uma plataforma de cotações nacionais em tempo real nesta versão.

## 🖥️ Tecnologias e ferramentas

| Tecnologia | Utilização |
|---|---|
| Java 25 | Desenvolvimento do backend |
| Spring Boot 4.1.1 | Estrutura da aplicação e API REST |
| Spring Data JPA | Persistência e acesso aos dados |
| MySQL 9.6 | Banco de dados relacional |
| Maven | Gerenciamento de dependências e build |
| HTML5 | Estrutura da interface |
| CSS3 | Estilização do dashboard |
| JavaScript | Interatividade e consumo da API |
| Python | Script auxiliar de consulta de preços |
| Git e GitHub | Versionamento e hospedagem do código |

## 🏗️ Arquitetura

A aplicação segue uma organização em camadas, separando as responsabilidades de acesso HTTP, regras de negócio e persistência.

```text
┌────────────────────────────────────┐
│          Frontend Web              │
│       HTML + CSS + JavaScript      │
└─────────────────┬──────────────────┘
                  │ HTTP / JSON
┌─────────────────▼──────────────────┐
│         API REST                   │
│       Spring Boot / Java            │
├────────────────────────────────────┤
│ Controllers                        │
│ DTOs                               │
│ Services                           │
│ Entities                           │
│ Repositories                       │
│ Tratamento global de exceções      │
└─────────────────┬──────────────────┘
                  │ JPA / SQL
┌─────────────────▼──────────────────┐
│              MySQL                 │
│     Persistência das cotações      │
└────────────────────────────────────┘
```

### Estrutura de diretórios

```text
agromarket/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/com/agromarket/api/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── AgromarketApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/style.css
│   │       │   ├── js/app.js
│   │       │   └── index.html
│   │       └── application.properties
│   └── test/
├── consulta_precos.py
├── pom.xml
├── mvnw
├── .gitignore
└── README.md
```

## 📈 Indicadores e análises

O AgroMarket disponibiliza recursos para examinar diferentes aspectos das cotações cadastradas.

| Indicador | Descrição |
|---|---|
| Preço médio | Média dos preços considerados na consulta |
| Preço mínimo | Menor preço registrado no recorte |
| Preço máximo | Maior preço registrado no recorte |
| Histórico | Evolução das cotações ao longo do período |
| Variação absoluta | Diferença entre os preços comparados |
| Variação percentual | Mudança percentual entre os preços comparados |

A interpretação desses indicadores depende da consistência dos registros, da unidade de medida e do período selecionado.

## 🔌 Endpoints da API

A API local utiliza a base `/api/v1`.

| Método | Endpoint | Finalidade |
|---|---|---|
| GET | `/api/v1/quotes` | Consultar cotações |
| POST | `/api/v1/quotes/import-csv` | Importar cotações por CSV |
| GET | `/api/v1/analytics/prices` | Consultar indicadores de preços |
| GET | `/api/v1/analytics/prices/history` | Consultar histórico |
| GET | `/api/v1/analytics/prices/variation` | Consultar variação de preços |

Exemplo de consulta de indicadores:

```http
GET /api/v1/analytics/prices?commodity=soja&startDate=2026-10-07&endDate=2026-10-21&unit=BRL%2Fsc60kg
```

Exemplo de consulta do histórico:

```http
GET /api/v1/analytics/prices/history?commodity=soja&startDate=2026-10-07&endDate=2026-10-21&unit=BRL%2Fsc60kg
```

Exemplo de consulta da variação:

```http
GET /api/v1/analytics/prices/variation?commodity=soja&startDate=2026-10-07&endDate=2026-10-21&unit=BRL%2Fsc60kg
```

Os parâmetros devem respeitar os nomes e formatos aceitos pelos respectivos endpoints.

## ⚙️ Como executar

### Pré-requisitos

- Java 25 instalado, compatível com a configuração atual do projeto.
- MySQL instalado e em execução.
- Git.
- Acesso ao Terminal.

### 1. Clonar o repositório

```bash
git clone https://github.com/matheusvccarvalho/agromarket.git
cd agromarket
```

### 2. Configurar o banco de dados

Crie um banco de dados MySQL para a aplicação e configure as propriedades de conexão de acordo com o ambiente local.

Antes de executar, confira `src/main/resources/application.properties` e ajuste:

- URL de conexão JDBC.
- Usuário do banco.
- Senha do banco.
- Configurações JPA necessárias para o ambiente.

**Segurança:** não publique credenciais reais no GitHub. Prefira variáveis de ambiente ou uma configuração local fora do controle de versão.

### 3. Iniciar a aplicação

No macOS ou Linux:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Se o Maven já estiver instalado, também é possível utilizar:

```bash
mvn spring-boot:run
```

### 4. Acessar o dashboard

Com a aplicação iniciada, acesse:

**http://localhost:8080/**

A interface estática é servida pelo próprio Spring Boot.

## 🗺️ Roadmap

O desenvolvimento do AgroMarket será realizado em etapas, priorizando a confiabilidade dos dados e a evolução gradual da plataforma.

### Fase 1 — Consolidar a versão atual

- [x] Estruturar o backend com Spring Boot.
- [x] Integrar o MySQL.
- [x] Implementar consulta de cotações.
- [x] Criar importação por CSV.
- [x] Implementar indicadores de preços.
- [x] Disponibilizar histórico e variação.
- [x] Desenvolver o dashboard web.
- [x] Publicar o código no GitHub.

### Fase 2 — Ampliar a cobertura de mercado

- [ ] Incluir cotações de outras regiões brasileiras.
- [ ] Expandir a cobertura para soja e milho.
- [ ] Adicionar filtros por estado e região.
- [ ] Padronizar unidades de medida.
- [ ] Identificar origem, data e metodologia de cada cotação.
- [ ] Avaliar a integração com fontes oficiais e institucionais.

### Fase 3 — Evoluir a inteligência analítica

- [ ] Comparar preços entre regiões.
- [ ] Criar rankings regionais por commodity.
- [ ] Adicionar gráficos comparativos e tendências.
- [ ] Desenvolver indicadores de volatilidade.
- [ ] Permitir comparações entre diferentes períodos.
- [ ] Implementar exportação de relatórios.

### Fase 4 — Automatizar a atualização dos dados

- [ ] Criar rotinas automatizadas de coleta.
- [ ] Implementar validação e prevenção de duplicidades.
- [ ] Registrar falhas de importação e histórico de atualizações.
- [ ] Monitorar a atualidade e a qualidade das cotações.
- [ ] Avaliar notificações para variações relevantes de preços.

### Fase 5 — Preparar para evolução e produção

- [ ] Implementar testes automatizados de serviços e endpoints.
- [ ] Aprimorar a documentação da API.
- [ ] Externalizar configurações e segredos.
- [ ] Adicionar autenticação e controle de acesso, se necessário.
- [ ] Configurar integração e entrega contínuas (CI/CD).
- [ ] Avaliar implantação em nuvem e monitoramento.

*As fases futuras são propostas de evolução, não funcionalidades já entregues.*

## 🔭 Visão de futuro

A visão de longo prazo é transformar o AgroMarket em uma plataforma de inteligência de mercado agrícola capaz de consolidar dados de diferentes regiões e fontes, facilitando a comparação de preços e a compreensão dos movimentos do mercado.

Com uma base consistente e histórico suficiente, o projeto poderá explorar análises mais avançadas e ferramentas de apoio à decisão, sempre distinguindo dados observados de estimativas ou previsões.

## 👨‍💻 Autor

**Matheus Carvalho**

Estudante de Engenharia de Software, interessado em desenvolvimento backend, análise de dados, Business Intelligence e tecnologia aplicada ao agronegócio.

- GitHub: [@matheusvccarvalho](https://github.com/matheusvccarvalho)
- Repositório: [AgroMarket](https://github.com/matheusvccarvalho/agromarket)

---

<p align="center">
  <strong>AgroMarket</strong><br>
  Tecnologia, dados e inteligência para o agronegócio. 🌱
</p>
