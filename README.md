# 🌱 AgroMarket

**Plataforma de análise e acompanhamento de cotações agrícolas.**

O AgroMarket é um projeto desenvolvido para acompanhar preços de commodities agrícolas, visualizar indicadores de mercado e analisar a variação das cotações ao longo do tempo. A proposta é transformar dados agrícolas em informações úteis para análise e tomada de decisão no agronegócio.

## 🎯 Objetivo

Desenvolver uma plataforma que una tecnologia, análise de dados e agronegócio, facilitando a consulta e a interpretação de cotações agrícolas.

## ✨ Funcionalidades

- Consulta de cotações agrícolas por meio de uma API REST.
- Importação de cotações a partir de arquivos CSV.
- Cálculo de indicadores de preços, incluindo média, mínimo e máximo.
- Consulta do histórico de preços por período.
- Análise de variação absoluta e percentual das cotações.
- Dashboard interativo com indicadores, gráficos e tabela de cotações.
- Filtros por commodity, período e unidade de medida.

## 🛠️ Tecnologias utilizadas

| Tecnologia | Aplicação |
|---|---|
| Java | Linguagem principal do backend |
| Spring Boot | Construção da API REST |
| Spring Data JPA | Persistência e acesso a dados |
| MySQL | Banco de dados relacional |
| HTML5 | Estrutura da interface |
| CSS3 | Estilização e identidade visual |
| JavaScript | Interatividade e integração com a API |
| Maven | Gerenciamento de dependências e execução |
| Git e GitHub | Versionamento e hospedagem do código |

## 📊 Indicadores de mercado

O sistema disponibiliza recursos para analisar:

- Preço médio das cotações no período.
- Menor e maior preço registrado.
- Evolução histórica dos preços.
- Variação absoluta e percentual entre cotações.
- Registros de mercado importados para a base de dados.

A base atual contém cotações de soja do Paraná. A expansão para outras regiões e commodities está prevista para evoluções futuras.

## 🏗️ Arquitetura do projeto

O projeto utiliza uma arquitetura com backend e frontend integrados:

- **Backend:** API REST desenvolvida com Java e Spring Boot.
- **Persistência:** banco de dados MySQL acessado por meio da camada de persistência Java.
- **Frontend:** interface web construída com HTML, CSS e JavaScript.
- **Integração:** o frontend consulta os endpoints do backend para exibir os indicadores e as cotações.

### Estrutura principal

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
│   │   │   └── service/
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── index.html
│   │       └── application.properties
├── consulta_precos.py
├── pom.xml
├── mvnw
└── README.md
```

## ⚙️ Como executar

### Pré-requisitos

- Java instalado em uma versão compatível com o projeto.
- MySQL instalado e em execução.
- Git.

### 1. Clonar o repositório

```bash
git clone https://github.com/matheusvccarvalho/agromarket.git
cd agromarket
```

### 2. Configurar o banco de dados

Crie um banco de dados MySQL para o projeto e configure a conexão nas propriedades da aplicação.

Defina as credenciais por meio de variáveis de ambiente ou de uma configuração local não versionada. Não publique senhas no repositório.

### 3. Executar a aplicação

No macOS ou Linux:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Se preferir utilizar o Maven instalado:

```bash
mvn spring-boot:run
```

### 4. Acessar o dashboard

Após a inicialização, acesse:

http://localhost:8080/

## 🔌 Endpoints principais

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/v1/quotes` | Consulta cotações |
| POST | `/api/v1/quotes/import-csv` | Importa cotações por CSV |
| GET | `/api/v1/analytics/prices` | Consulta indicadores de preços |
| GET | `/api/v1/analytics/prices/history` | Consulta histórico de preços |
| GET | `/api/v1/analytics/prices/variation` | Consulta variação de preços |

Os endpoints de análise aceitam parâmetros para filtrar os dados, como commodity, período e unidade de medida.

## 🚀 Próximas evoluções

- Ampliação da cobertura de cotações para diferentes estados brasileiros.
- Inclusão de novas commodities agrícolas.
- Integração com fontes adicionais de dados de mercado.
- Comparação regional de preços.
- Evolução dos gráficos e indicadores analíticos.
- Aprimoramento dos filtros e da experiência de utilização.

## 👨‍💻 Autor

**Matheus Carvalho**

Estudante de Engenharia de Software, interessado em desenvolvimento de sistemas, análise de dados, Business Intelligence e tecnologia aplicada ao agronegócio.

- GitHub: [@matheusvccarvalho](https://github.com/matheusvccarvalho)
- Projeto: [AgroMarket](https://github.com/matheusvccarvalho/agromarket)

---

*AgroMarket — tecnologia e dados a serviço do agronegócio.* 🌱
