# 1. Inicio

## a) Descripción del dominio y propósito del sistema
El proyecto consiste en un sistema de gestión de inventarios que permite la administración de **Productos**, **Categorías** y **Ubicaciones** (Locations). El propósito de la aplicación es proveer operaciones CRUD para estas entidades garantizando la integridad de los datos, reglas de negocio y un manejo adecuado del almacenamiento de la información de inventario en una base de datos relacional. Está desarrollado usando Spring Boot bajo una Arquitectura Hexagonal que separa las reglas de negocio de la infraestructura y el punto de entrada REST.

## b) Diagrama de arquitectura
El proyecto sigue un diseño de **Arquitectura Hexagonal (Puertos y Adaptadores)**.

```mermaid
graph TD
    Client[Cliente / Postman / Navegador] -->|HTTP REST| Delivery[Capa Delivery / Controllers]
    Delivery -->|Llama| Application[Capa Aplicación / Services]
    Application -->|Implementa| Domain[Capa Dominio / Models & Ports]
    Infrastructure[Capa Infraestructura / Adapters] -->|Implementa Puertos| Domain
    Infrastructure -->|JPA / Hibernate| Database[(Base de Datos Relacional)]

    style Client fill:#f9f,stroke:#333,stroke-width:2px
    style Delivery fill:#bbf,stroke:#333,stroke-width:2px
    style Application fill:#bfb,stroke:#333,stroke-width:2px
    style Domain fill:#fbb,stroke:#333,stroke-width:2px
    style Infrastructure fill:#fbf,stroke:#333,stroke-width:2px
    style Database fill:#ddf,stroke:#333,stroke-width:2px
```

## c) Integrantes del equipo
* Luis Fernando Beltran
* Roberto Jose Breuer
