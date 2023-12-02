# Pixel Wave

![Pixel Wave](banner.png)

Pixel Wave, uma plataforma Java Spring para operações com imagens.

## Descrição do Projeto

Pixel Wave é uma plataforma que oferece uma variedade de serviços para manipulação, processamento e gerenciamento de imagens. Desde armazenamento até operações de processamento, o Pixel Wave é projetado para atender às necessidades de desenvolvedores e aplicativos que trabalham com imagens.

## Tecnologias Utilizadas

- Java 21
- Spring Framework
- Docker

## Estrutura do Projeto

O projeto é organizado em microsserviços, cada um responsável por uma função específica. Aqui estão os microsserviços principais:

- **Gateway Service:** Ponto de entrada para interagir com os diferentes serviços.
- **Config Service:** Gerencia configurações do sistema.
- **Discovery Service:** Fornece descoberta e registro de serviços.
- **Storage Service:** Responsável pelo armazenamento de imagens.
- **Format Service:** Realiza operações de formatação em imagens.
- **Compression Service:** Oferece serviços de compressão de imagens.
- **Color Service:** Manipulação de cores em imagens.

## Pré-requisitos

- Java 21

## Configuração e Uso

1. Clone o repositório.
2. Inicie os serviços usando Maven. Certifique-se de iniciar o `Config Service` e o `Discovery Service` antes dos demais serviços.
   ```bash
   ./mvnw spring-boot:run
