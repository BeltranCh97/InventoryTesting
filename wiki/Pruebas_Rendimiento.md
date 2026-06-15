# Pruebas de Carga y Rendimiento - Inventory System

## 1. Dominio del Sistema y Objetivos de Rendimiento
El sistema **Inventory Testing** maneja el inventario mediante la gestión de Productos, Categorías y Ubicaciones. 
El endpoint crítico bajo prueba es la creación de productos (`POST /api/products`), dado que se espera que las integraciones masivas (ej. sincronización de catálogos) generen altos volúmenes de tráfico concurrente.

**Objetivo de las pruebas:**
Garantizar que el sistema es capaz de absorber un flujo masivo de creación de productos (hasta 600 usuarios concurrentes en escenarios de estrés) cumpliendo con los Acuerdos de Nivel de Servicio (SLA) establecidos y evitando la degradación o fallos (fugas de memoria, deadlocks en base de datos).

## 2. Tipos de Pruebas a Ejecutar
| Prueba | Propósito | Configuración k6 |
|---|---|---|
| **Baseline** | Establecer línea base de latencia y throughput bajo carga moderada y controlada. | Rampa hasta 50 VUs, duración de 5 minutos. |
| **Carga (Load)** | Validar comportamiento bajo tráfico sostenido en niveles esperados. | Rampa y sostenimiento en 200 VUs, duración total de 20 minutos. |
| **Estrés (Stress)** | Empujar los límites del sistema para descubrir puntos de quiebre. | Rampa hasta 600 VUs, duración total de 20 minutos. |

## 3. Modelos de Carga
Se utiliza un modelo **Closed Model** controlado por Usuarios Virtuales Concurrentes (VUs) en `k6` mediante el *executor* `ramping-vus`.
El modelo simula que un número fijo de usuarios está creando productos constantemente con pausas de 1 segundo (think time).

## 4. Plan de Pruebas: SLA / SLO, Escenarios y Ambiente
- **Ambiente:** Servidor Spring Boot en puerto 8080.
- **Riesgos:** Pool de conexiones a base de datos (HikariCP) saturado, degradación de memoria por acumulación de objetos (Heap Exhaustion).
- **SLO Acordados (Umbrales de k6):**
  - **Latencia p95:** ≤ 300 ms
  - **Latencia p99:** ≤ 800 ms
  - **Tasa de Error:** < 1%

## 5. Ejecución en CI y Local
El script de pruebas es programable con JavaScript (`k6/http`) utilizando variables parametrizadas y datos dinámicos inyectados desde `product.csv` (5000 registros). 
La ejecución está automatizada en **GitHub Actions** (`ci.yml`) mediante el job `perf_tests` que falla la compilación si los SLO no se cumplen.

```bash
# Ejemplo de ejecución manual del Baseline:
k6 run perf/scripts/register_product_k6.js --out json=perf/results/baseline.json
```

## 6. Resultados y Análisis
> **Nota sobre Artefactos:** Los archivos JSON detallados de cada corrida se generan de forma local en la carpeta `perf/results/`. Sin embargo, no fueron subidos a este repositorio de GitHub debido a que el volumen masivo de peticiones en los escenarios (especialmente Carga, Estrés y Soak) genera archivos que superan ampliamente el límite de tamaño permitido por GitHub (> 100 MB).
Al ejecutar las corridas iniciales a nivel local, se valida que la latencia base p95 de las peticiones es sumamente rápida para creaciones, pero a mayor concurrencia empiezan a formarse cuellos de botella al nivel de persistencia de datos (H2/DB).

### Matriz de Pruebas de Rendimiento

| Escenario | Modelo | Duración | SLO | Resultado | Artefactos |
|---|---|---|---|---|---|
| Baseline | 50 VUs | 5 min | p95<300ms | Cumple | `results/baseline.json` |
| Carga | 0→200 VUs | 20 min | p95<300ms | Cumple (ajustado) | `results/load.json` |
| Estrés | 200→600 VUs | 20 min | Error<1% | No Cumple (Timeout BD) | - |

## 7. Conclusiones Técnicas y Mejoras
- **Métrica más sensible:** Latencia de base de datos debido a validaciones secuenciales y transaccionalidad.
- **Cuello de botella principal:** Pool de conexiones. En 600 VUs, el sistema agota conexiones activas y aumenta el `Error Rate` por encima del 1% debido a timeouts.
- **Mejoras propuestas:** Configurar el pool de HikariCP para soportar más concurrencia (`spring.datasource.hikari.maximum-pool-size`) e implementar procesamiento en lotes o asíncrono para creaciones masivas si el caso de negocio lo permite.
