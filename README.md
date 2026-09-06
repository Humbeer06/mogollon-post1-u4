# Post-contenido — Unidad 4: Patrones de Comportamiento en ComprasUDES

## Descripción
Repositorio del post-contenido de la Unidad 4 de Patrones de Diseño
de Software. Un único proyecto Spring Boot (compras-comportamiento)
que resuelve cuatro necesidades reales del backend de ComprasUDES,
el sistema interno de solicitudes de compra corporativas: aprobación
por niveles jerárquicos, ejecución reversible de solicitudes
aprobadas, notificaciones ante cambios de estado y reglas de
transición según el estado actual de la solicitud.

## Cómo ejecutar

mvn clean package
mvn spring-boot:run
mvn test


## Decisiones de diseño

### Necesidad 1 — Aprobación por niveles jerárquicos

**Patrón aplicado:** Chain of Responsibility

**Justificación:** El problema exige que una solicitud recorra una secuencia de decisores independientes (`SupervisorArea`, `GerenteArea`, `DirectorFinanciero`), donde cada uno evalúa si el monto está dentro de su autoridad y, si no, delega automáticamente al siguiente, sin que `ControladorSolicitudes` conozca cuántos niveles existen ni en qué orden se consultan. Esto se resolvió con `NivelAprobacion` como clase base de la cadena, donde cada nivel implementa `puedeResolver()` y `resolver()`, y se enlaza al siguiente mediante `enlazarCon()`. Agregar el nivel `RevisorCumplimientoNormativo` para solicitudes INTERNACIONAL no requirió modificar `ControladorSolicitudes` ni ningún nivel existente, solo insertarlo al inicio de la cadena en `ServicioAprobacionCadena`.

Se descartó Command porque este problema no tiene ninguna operación que deba encapsularse para poder revertirse después; el requisito central es el enrutamiento condicional de una petición hasta que alguien la resuelve, no la reversibilidad de una acción ya ejecutada. Un Command aquí habría exigido inventar artificialmente una noción de "deshacer una aprobación", que el enunciado nunca pide, y no habría resuelto el problema real de que la secuencia de niveles pueda crecer sin tocar código existente.

### Necesidad 2 — Ejecución reversible de solicitudes

**Patrón aplicado:** Command

**Justificación:** El problema exige que reservar presupuesto y generar orden de compra sean operaciones ejecutables y deshacibles de forma independiente, con un historial que conserve todas las operaciones realizadas sobre una solicitud, no solo la última. Esto se resolvió encapsulando cada operación como un objeto (`ReservarPresupuestoCommand`, `GenerarOrdenCompraCommand`) que implementa `OperacionEjecucion`, con métodos `ejecutar()` y `deshacer()` propios. `HistorialOperaciones` conserva la lista completa de comandos ejecutados sobre una solicitud, permitiendo deshacer cualquiera de ellos sin afectar a los demás, sin modificar `PresupuestoService` ni `OrdenCompraService`.

Se descartó Chain of Responsibility porque aquí no existe ningún decisor evaluando condiciones para decidir si delega o resuelve una petición entrante; hay dos operaciones discretas que un mismo actor, el equipo de Compras, decide ejecutar y, eventualmente, revertir. Una cadena de responsabilidad no tiene ningún mecanismo natural para conservar un historial de acciones reversibles, porque su propósito es el recorrido condicional de una solicitud, no la persistencia de operaciones ya ejecutadas.


