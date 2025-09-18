# 🗄️ Object Store Study

Repositório para estudar **conceitos de Object-Store, Blob-Store e Cloud Storage** por meio de uma **API** que permite armazenar arquivos em uma **Object-Store** (MinIO), utilizando a lógica de **Buckets**.

---

## 📌 Termos-chave (resumo técnico)

- **Object-Store / Object Storage**  
  Armazenamento que gerencia dados como **objetos** (dados + metadados + identificador único), sem hierarquia de pastas. Ideal para grandes volumes de dados não estruturados.

- **Blob-Store / Blob Storage**  
  Armazenamento de **blobs** (_Binary Large Objects_), como imagens, vídeos, documentos ou qualquer dado binário não estruturado.

- **Cloud Storage**  
  Modelo de armazenamento remoto, acessível via internet, no qual provedores garantem escalabilidade, segurança e disponibilidade.

- **API**  
  Interface de programação (ex.: REST) que permite criar, ler, atualizar e excluir objetos em um sistema de armazenamento.

- **MinIO**  
  Solução open-source de **object storage**, compatível com a API do Amazon S3, de alto desempenho e escalável.

- **Bucket**  
  Container lógico que organiza objetos dentro de um Object-Store. Equivalente a uma “pasta”, mas sem hierarquia real; cada bucket pode conter vários objetos.

---

## 🚀 Como rodar com Docker

Certifique-se de ter **Docker** e **Docker Compose** instalados.  
Para subir o ambiente (banco de dados e object store), rode:

```bash
docker compose up -d
```

Para rodar a API

```bash
mvn clean install --DskipTests 
```

```bash
java -jar object_store-0.0.1-SNAPSHOT.jar
```

### Próximos Passos ###

Ainda há espaço para aprender e aprimorar meus conhecimentos na ferramenta. Neste projeto aqui vai uma lista do que aprimorar:

- **Logs Aprimorados**
  Logs aprimorados em Prometheus ou Datadog.
- **Diferenciar Buckets por Content-Type**
  Essa feature tem por base diferenciar arquivos em buckets por content-type, isso também explora o conceito de regras em um bucket.