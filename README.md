# Auction Project

Projeto de leilões composto por três aplicações Spring Boot independentes. Este README cobre somente os serviços de backend; a pasta `auction-app` (frontend) não faz parte deste guia.

## O que o projeto faz

- **`user`**: cadastra usuários e consulta seus dados. Também consome do Kafka os eventos de lances para associar o histórico de lances ao usuário.
- **`auction_ms`**: cria e consulta leilões, registra e lista lances, valida as regras dos lances e consulta o serviço de usuários. Publica eventos no Kafka e envia atualizações em tempo real por WebSocket/STOMP.
- **`gateway_ms`**: recebe as chamadas HTTP e as encaminha para `user` e `auction_ms`.
- **Infraestrutura local**: MySQL para persistência e Kafka com ZooKeeper para comunicação assíncrona. O `docker-compose.yaml` também define a interface Kafka UI.

Ao registrar um lance, `auction_ms` verifica o leilão, valida o valor e o usuário, persiste o lance e publica o evento `auction.bid`. O serviço `user` consome esse evento e atualiza o histórico associado ao participante.

## Tecnologias

- Java 21 e Maven Wrapper
- Spring Boot, Spring Data JPA e Spring Cloud Gateway
- MySQL 8
- Apache Kafka e ZooKeeper (imagens Confluent)
- Kafka UI


## Estrutura do repositório

```text
.
├── auction_ms/     Serviço de leilões e lances
├── gateway_ms/     Gateway HTTP
├── user/           Serviço de usuários e histórico de lances
├── docker-compose.yaml
└── auction-app/    Frontend — fora do escopo deste README
```
