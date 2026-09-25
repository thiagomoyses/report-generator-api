# 📊 Report Generator

· [🇧🇷 Português](#-português) 
· [🇫🇷 Français](#-français) 
· [🇬🇧 English](#-english)

---

# 🇧🇷 Português

## 📖 Sobre o projeto

**Report Generator** é uma API REST desenvolvida com **Spring Boot** para centralizar e simplificar a geração de relatórios.

A API recebe o nome do relatório, o formato desejado e, opcionalmente, parâmetros adicionais. A partir dessas informações, ela executa a consulta correspondente no banco de dados, prepara os dados, envia as informações para o relatório e retorna o arquivo gerado ao cliente.

Atualmente, a geração de **PDF** é realizada utilizando **JasperReports** e arquivos `.jasper`.

A geração de arquivos **XLSX** e **CSV** está prevista para as próximas etapas do projeto.

O banco de dados utilizado é **PostgreSQL**, executado dentro de um container **Docker**. Para fins de desenvolvimento e demonstração, o banco contém dados simulados de alunos e suas respectivas notas.

---

## 🎯 Objetivos

O projeto tem como principais objetivos:

* Centralizar a geração de relatórios em uma única API.
* Separar a lógica de acesso aos dados da apresentação dos relatórios.
* Permitir que diferentes aplicações consumam os mesmos relatórios.
* Suportar diferentes formatos de saída.
* Evitar que cada aplicação precise implementar sua própria lógica de geração de relatórios.
* Disponibilizar os relatórios através de uma API REST simples.

---

## 🏗️ Arquitetura

O fluxo principal da aplicação é:

```text
┌──────────────────┐
│      Cliente     │
│  Web / Desktop   │
│   / Aplicação    │
└────────┬─────────┘
         │
         │ HTTP Request
         ▼
┌──────────────────────────┐
│    Report Generator API  │
│        Spring Boot       │
└────────────┬─────────────┘
             │
             │ SQL Query
             ▼
┌──────────────────────────┐
│       PostgreSQL         │
│      Docker Container    │
└────────────┬─────────────┘
             │
             │ Dados
             ▼
┌──────────────────────────┐
│      JasperReports       │
│        .jasper           │
└────────────┬─────────────┘
             │
             │ Arquivo gerado
             ▼
┌──────────────────────────┐
│          Cliente         │
│      PDF / XLSX / CSV    │
└──────────────────────────┘
```

---

## 🚀 API

### Endpoint

```http
GET /reports/{reportName}/{format}
```

### Exemplo

```http
GET http://localhost:3003/reports/findallstudents/PDF
```

### Com parâmetros

Os parâmetros do relatório podem ser enviados através da query string:

```http
GET http://localhost:3003/reports/findallstudents/PDF?studentId=10
```

### Parâmetros

| Parâmetro        | Obrigatório | Descrição                               |
| ---------------- | :---------: | --------------------------------------- |
| `reportName`     |      ✅      | Nome do relatório                       |
| `format`         |      ✅      | Formato de saída                        |
| Query Parameters |      ❌      | Parâmetros necessários para o relatório |

---

## 📄 Formatos

### PDF

Atualmente implementado utilizando **JasperReports**.

```http
GET /reports/findallstudents/PDF
```

O resultado é retornado diretamente na resposta HTTP como um arquivo PDF.

### XLSX

Atualmente implementado utilizando **Apache POI**.

```http
GET /reports/findallstudents/XLSX
```

A ideia é gerar arquivos Excel utilizando os mesmos dados recuperados pela API.

### CSV

Atualmente implementado utilizando **Apache commons: commons-csv**.

```http
GET /reports/findallstudents/CSV
```

---

## 📊 JasperReports

Os relatórios são desenvolvidos utilizando **Jaspersoft Studio** e posteriormente compilados para arquivos `.jasper`.

O fluxo de geração de um relatório é:

```text
Request
   │
   ▼
Identificação do relatório
   │
   ▼
Execução da query
   │
   ▼
Preparação dos dados
   │
   ▼
Carregamento do .jasper
   │
   ▼
Preenchimento do relatório
   │
   ▼
Exportação
   │
   ▼
HTTP Response
```

A intenção é manter a maior parte da preparação dos dados e das regras de negócio na aplicação, deixando o JasperReports principalmente responsável pela apresentação do relatório.

---

## 🗄️ Banco de dados

O projeto utiliza **PostgreSQL** executado em um container Docker.

O banco contém dados simulados para representar uma aplicação acadêmica.

Atualmente existem entidades relacionadas a:

* 👨‍🎓 Alunos
* 📚 Disciplinas
* 📝 Notas
* 📊 Informações acadêmicas

Exemplo conceitual:

```text
Student
├── id
├── name
├── email
└── ...

Grade
├── id
├── student_id
├── subject
├── grade
└── ...
```

Esses dados servem como base para testar diferentes tipos de relatórios.

---

## 🐳 Docker

O PostgreSQL é executado através do Docker, permitindo reproduzir o ambiente de desenvolvimento sem precisar instalar o banco diretamente na máquina.

Iniciar os containers:

```bash
docker compose up -d
```

Verificar os containers:

```bash
docker ps
```

Parar os containers:

```bash
docker compose down
```

---

## ⚙️ Tecnologias

| Tecnologia            | Utilização                     |
| --------------------- | ------------------------------ |
| ☕ Java 21.0.9-tem     | Linguagem / Runtime            |
| 🌱 Spring Boot 2.5.4  | Framework da API               |
| 🐘 PostgreSQL         | Banco de dados                 |
| 🐳 Docker             | Containerização                |
| 📊 JasperReports      | Geração de relatórios          |
| 🖥️ Jaspersoft Studio | Desenvolvimento dos relatórios |
| 🔗 REST               | Comunicação entre aplicações   |
| 📦 Maven              | Gerenciamento do projeto       |

---

## ▶️ Executando o projeto

### 1. Iniciar o PostgreSQL

```bash
docker compose up -d
```

### 2. Configurar a aplicação

Configure os parâmetros de conexão com o PostgreSQL no arquivo:

```text
src/main/resources/application.properties
```

ou:

```text
src/main/resources/application.yml
```

### 3. Iniciar a API

Utilizando Maven:

```bash
./mvnw spring-boot:run
```

ou:

```bash
mvn spring-boot:run
```

A API estará disponível em:

```text
http://localhost:3003
```

---

## 🧪 Exemplo

Para gerar um relatório de alunos em PDF:

```bash
curl http://localhost:3003/reports/findallstudents/PDF \
     --output findallstudents.pdf
```

O arquivo `findallstudents.pdf` conterá o relatório gerado a partir dos dados do PostgreSQL.

---

## 🔄 Fluxo de uma requisição

```text
GET /reports/findallstudents/PDF
            │
            ▼
      Report Controller
            │
            ▼
     Report Identification
            │
            ▼
       Database Query
            │
            ▼
      Data Preparation
            │
            ▼
       JasperReports
            │
            ▼
        PDF Export
            │
            ▼
       HTTP Response
```

---

## 📁 Estrutura do projeto

```text
report-generator/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ...
│   │   │
│   │   └── resources/
│   │       ├── reports/
│   │       │   └── *.jasper
│   │       │
│   │       ├── application.properties
│   │       └── ...
│   │
│   └── test/
│
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## 🗺️ Roadmap

* [x] REST API para geração de relatórios
* [x] Integração com PostgreSQL
* [x] PostgreSQL executando via Docker
* [x] Integração com JasperReports
* [x] Geração de PDF
* [X] Geração de XLSX
* [X] Geração de CSV
* [ ] Validação dos parâmetros dos relatórios
* [X] Melhor tratamento de erros
* [X] Documentação OpenAPI / Swagger
* [ ] Autenticação e autorização
* [ ] Criaçao de UI

---

# 🇫🇷 Français

## 📖 Présentation

**Report Generator** est une API REST développée avec **Spring Boot** afin de centraliser et de simplifier la génération de rapports.

L'API reçoit le nom du rapport, le format souhaité et, si nécessaire, des paramètres supplémentaires. Elle exécute ensuite la requête correspondante dans la base de données, prépare les données, les transmet au rapport et retourne le fichier généré au client.

Actuellement, la génération des **PDF** est réalisée avec **JasperReports** et des fichiers `.jasper`.

La génération des fichiers **XLSX** et **CSV** est prévue dans les prochaines étapes du projet.

La base de données utilisée est **PostgreSQL**, exécutée dans un conteneur **Docker**. Pour les besoins du développement et de la démonstration, la base contient des données simulées représentant des étudiants et leurs notes.

---

## 🎯 Objectifs

Les principaux objectifs du projet sont :

* Centraliser la génération des rapports dans une seule API.
* Séparer l'accès aux données de la présentation des rapports.
* Permettre à plusieurs applications de consommer les mêmes rapports.
* Supporter différents formats de sortie.
* Éviter de réimplémenter la génération des rapports dans chaque application.
* Fournir les rapports via une API REST simple.

---

## 🏗️ Architecture

Le fonctionnement général est le suivant :

```text
┌──────────────────┐
│      Client      │
│  Web / Desktop   │
│   / Application  │
└────────┬─────────┘
         │
         │ Requête HTTP
         ▼
┌──────────────────────────┐
│    Report Generator API  │
│        Spring Boot       │
└────────────┬─────────────┘
             │
             │ Requête SQL
             ▼
┌──────────────────────────┐
│       PostgreSQL         │
│    Conteneur Docker      │
└────────────┬─────────────┘
             │
             │ Données
             ▼
┌──────────────────────────┐
│      JasperReports       │
│        .jasper           │
└────────────┬─────────────┘
             │
             │ Fichier généré
             ▼
┌──────────────────────────┐
│          Client          │
│      PDF / XLSX / CSV    │
└──────────────────────────┘
```

---

## 🚀 API

### Endpoint

```http
GET /reports/{reportName}/{format}
```

### Exemple

```http
GET http://localhost:3003/reports/findallstudents/PDF
```

### Avec des paramètres

Les paramètres du rapport peuvent être transmis dans la query string :

```http
GET http://localhost:3003/reports/findallstudents/PDF?studentId=10
```

### Paramètres

| Paramètre        | Obligatoire | Description                       |
| ---------------- | :---------: | --------------------------------- |
| `reportName`     |      ✅      | Nom du rapport                    |
| `format`         |      ✅      | Format de sortie                  |
| Query Parameters |      ❌      | Paramètres nécessaires au rapport |

---

## 📄 Formats

### PDF

Actuellement implémenté avec **JasperReports**.

```http
GET /reports/findallstudents/PDF
```

Le résultat est directement retourné dans la réponse HTTP sous forme de fichier PDF.

### XLSX

Actuellement implémenté avec **Apache POI**.

```http
GET /reports/findallstudents/XLSX
```

L'objectif est de générer des fichiers Excel à partir des mêmes données récupérées par l'API.

### CSV

Actuellement implémenté avec **Apache commons: commons-csv**.

```http
GET /reports/findallstudents/CSV
```

---

## 📊 JasperReports

Les rapports sont développés avec **Jaspersoft Studio**, puis compilés sous forme de fichiers `.jasper`.

Le processus de génération est le suivant :

```text
Requête
   │
   ▼
Identification du rapport
   │
   ▼
Exécution de la requête SQL
   │
   ▼
Préparation des données
   │
   ▼
Chargement du .jasper
   │
   ▼
Remplissage du rapport
   │
   ▼
Export
   │
   ▼
Réponse HTTP
```

L'objectif est de conserver la préparation des données et la logique applicative dans l'API, tandis que JasperReports reste principalement responsable de la présentation du rapport.

---

## 🗄️ Base de données

Le projet utilise **PostgreSQL**, exécuté dans un conteneur Docker.

La base contient des données simulées représentant une application académique.

Les données comprennent notamment :

* 👨‍🎓 Étudiants
* 📚 Matières
* 📝 Notes
* 📊 Informations académiques

Exemple conceptuel :

```text
Student
├── id
├── name
├── email
└── ...

Grade
├── id
├── student_id
├── subject
├── grade
└── ...
```

Ces données servent à tester différents types de rapports.

---

## 🐳 Docker

PostgreSQL est exécuté via Docker afin de faciliter la reproduction de l'environnement de développement.

Démarrer les conteneurs :

```bash
docker compose up -d
```

Vérifier les conteneurs :

```bash
docker ps
```

Arrêter les conteneurs :

```bash
docker compose down
```

---

## ⚙️ Technologies

| Technologie           | Utilisation                      |
| --------------------- | -------------------------------- |
| ☕ Java 21.0.9-tem     | Langage / Runtime                |
| 🌱 Spring Boot 2.5.4  | Framework de l'API               |
| 🐘 PostgreSQL         | Base de données                  |
| 🐳 Docker             | Conteneurisation                 |
| 📊 JasperReports      | Génération des rapports          |
| 🖥️ Jaspersoft Studio | Conception des rapports          |
| 🔗 REST               | Communication entre applications |
| 📦 Maven              | Gestion du projet                |

---

## ▶️ Lancer le projet

### 1. Démarrer PostgreSQL

```bash
docker compose up -d
```

### 2. Configurer l'application

Configurer les paramètres de connexion à PostgreSQL dans :

```text
src/main/resources/application.properties
```

ou :

```text
src/main/resources/application.yml
```

### 3. Démarrer l'API

Avec Maven :

```bash
./mvnw spring-boot:run
```

ou :

```bash
mvn spring-boot:run
```

L'API sera disponible à l'adresse :

```text
http://localhost:3003
```

---

## 🧪 Exemple

Générer un rapport des étudiants au format PDF :

```bash
curl http://localhost:3003/reports/findallstudents/PDF \
     --output findallstudents.pdf
```

Le fichier `findallstudents.pdf` contiendra le rapport généré à partir des données PostgreSQL.

---

## 🔄 Flux d'une requête

```text
GET /reports/findallstudents/PDF
            │
            ▼
      Report Controller
            │
            ▼
     Identification du rapport
            │
            ▼
       Requête SQL
            │
            ▼
      Préparation des données
            │
            ▼
       JasperReports
            │
            ▼
        Export PDF
            │
            ▼
       Réponse HTTP
```

---

## 📁 Structure du projet

```text
report-generator/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ...
│   │   │
│   │   └── resources/
│   │       ├── reports/
│   │       │   └── *.jasper
│   │       │
│   │       ├── application.properties
│   │       └── ...
│   │
│   └── test/
│
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## 🗺️ Roadmap

* [x] API REST pour la génération des rapports
* [x] Intégration PostgreSQL
* [x] PostgreSQL exécuté via Docker
* [x] Intégration JasperReports
* [x] Génération PDF
* [X] Génération XLSX
* [X] Génération CSV
* [ ] Validation des paramètres des rapports
* [X] Amélioration de la gestion des erreurs
* [X] Documentation OpenAPI / Swagger
* [ ] Authentification et autorisation
* [ ] Création d'interface utilisateur

---

# 🇬🇧 English

## 📖 About

**Report Generator** is a **Spring Boot REST API** designed to centralize and simplify report generation.

The API receives a report name, the requested output format and, when required, additional parameters. It then executes the corresponding database query, prepares the data, passes the information to the report engine and returns the generated file to the client.

PDF generation is currently implemented using **JasperReports** and compiled `.jasper` files.

**XLSX** and **CSV** generation are planned for future versions.

The project uses **PostgreSQL**, running inside a **Docker** container. For development and demonstration purposes, the database contains simulated data representing findallstudents and their grades.

---

## 🎯 Goals

The main goals of this project are:

* Centralize report generation behind a single API.
* Separate data access from report presentation.
* Allow multiple applications to consume the same reports.
* Support multiple output formats.
* Avoid implementing report generation independently in every application.
* Provide reports through a simple REST API.

---

## 🏗️ Architecture

The general workflow is:

```text
┌──────────────────┐
│      Client      │
│  Web / Desktop   │
│   / Application  │
└────────┬─────────┘
         │
         │ HTTP Request
         ▼
┌──────────────────────────┐
│    Report Generator API  │
│        Spring Boot       │
└────────────┬─────────────┘
             │
             │ SQL Query
             ▼
┌──────────────────────────┐
│       PostgreSQL         │
│      Docker Container    │
└────────────┬─────────────┘
             │
             │ Data
             ▼
┌──────────────────────────┐
│      JasperReports       │
│        .jasper           │
└────────────┬─────────────┘
             │
             │ Generated File
             ▼
┌──────────────────────────┐
│          Client          │
│      PDF / XLSX / CSV    │
└──────────────────────────┘
```

---

## 🚀 API

### Endpoint

```http
GET /reports/{reportName}/{format}
```

### Example

```http
GET http://localhost:3003/reports/findallstudents/PDF
```

### With parameters

Report parameters can be passed through the query string:

```http
GET http://localhost:3003/reports/findallstudents/PDF?studentId=10
```

### Parameters

| Parameter        | Required | Description                       |
| ---------------- | :------: | --------------------------------- |
| `reportName`     |     ✅    | Name of the report                |
| `format`         |     ✅    | Output format                     |
| Query Parameters |     ❌    | Parameters required by the report |

---

## 📄 Formats

### PDF

Currently implemented using **JasperReports**.

```http
GET /reports/findallstudents/PDF
```

The generated PDF is returned directly in the HTTP response.

### XLSX

Currently implemented using **Apache POI**.

```http
GET /reports/findallstudents/XLSX
```

The goal is to generate Excel workbooks using the same data retrieved by the API.

### CSV

Currently implemented using **Apache commons: commons-csv**.

```http
GET /reports/findallstudents/CSV
```

---

## 📊 JasperReports

Reports are designed using **Jaspersoft Studio** and then compiled into `.jasper` files.

The report generation process is:

```text
Request
   │
   ▼
Report Identification
   │
   ▼
SQL Query
   │
   ▼
Data Preparation
   │
   ▼
Load .jasper
   │
   ▼
Fill Report
   │
   ▼
Export
   │
   ▼
HTTP Response
```

The goal is to keep data preparation and application logic inside the API, while JasperReports is primarily responsible for report presentation.

---

## 🗄️ Database

The project uses **PostgreSQL**, running inside a Docker container.

The database contains simulated data representing an academic application.

The current data includes:

* 👨‍🎓 Students
* 📚 Subjects
* 📝 Grades
* 📊 Academic information

Conceptual example:

```text
Student
├── id
├── name
├── email
└── ...

Grade
├── id
├── student_id
├── subject
├── grade
└── ...
```

This data is used to test different report scenarios.

---

## 🐳 Docker

PostgreSQL runs inside Docker, making the development environment easier to reproduce.

Start the containers:

```bash
docker compose up -d
```

Check running containers:

```bash
docker ps
```

Stop the containers:

```bash
docker compose down
```

---

## ⚙️ Technology Stack

| Technology            | Purpose                   |
| --------------------- | ------------------------- |
| ☕ Java 21.0.9-tem     | Language / Runtime        |
| 🌱 Spring Boot 2.5.4  | API Framework             |
| 🐘 PostgreSQL         | Database                  |
| 🐳 Docker             | Containerization          |
| 📊 JasperReports      | Report generation         |
| 🖥️ Jaspersoft Studio | Report design             |
| 🔗 REST               | Application communication |
| 📦 Maven              | Project management        |

---

## ▶️ Running the Project

### 1. Start PostgreSQL

```bash
docker compose up -d
```

### 2. Configure the application

Configure the PostgreSQL connection in:

```text
src/main/resources/application.properties
```

or:

```text
src/main/resources/application.yml
```

### 3. Start the API

Using Maven:

```bash
./mvnw spring-boot:run
```

or:

```bash
mvn spring-boot:run
```

The API will be available at:

```text
http://localhost:3003
```

---

## 🧪 Example

Generate a student report as PDF:

```bash
curl http://localhost:3003/reports/findallstudents/PDF \
     --output findallstudents.pdf
```

The `findallstudents.pdf` file will contain the report generated from the PostgreSQL data.

---

## 🔄 Request Flow

```text
GET /reports/findallstudents/PDF
            │
            ▼
      Report Controller
            │
            ▼
      Report Identification
            │
            ▼
         SQL Query
            │
            ▼
       Data Preparation
            │
            ▼
       JasperReports
            │
            ▼
        PDF Export
            │
            ▼
       HTTP Response
```

---

## 📁 Project Structure

```text
report-generator/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ...
│   │   │
│   │   └── resources/
│   │       ├── reports/
│   │       │   └── *.jasper
│   │       │
│   │       ├── application.properties
│   │       └── ...
│   │
│   └── test/
│
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## 🗺️ Roadmap

* [x] REST API for report generation
* [x] PostgreSQL integration
* [x] PostgreSQL running through Docker
* [x] JasperReports integration
* [x] PDF generation
* [X] XLSX generation
* [X] CSV generation
* [ ] Report parameter validation
* [X] Improved error handling
* [X] OpenAPI / Swagger documentation
* [ ] Authentication and authorization
* [ ] UI creation

---

## 📄 License

This project is intended for development and demonstration purposes.
