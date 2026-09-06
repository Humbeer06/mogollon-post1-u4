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

### Necesidad 3 — Notificaciones ante cambio de estado

**Patrón aplicado:** Observer

**Justificación:** El problema exige que, cada vez que una solicitud cambia de estado, se disparen automáticamente tres reacciones independientes (correo, dashboard de contabilidad, auditoría), sin que el código que cambia el estado conozca directamente esos tres módulos, y que agregar una cuarta reacción no requiera modificar ese mecanismo. Esto se resolvió con `NotificadorCambioEstado` como sujeto, que mantiene una lista de `SuscriptorEstado` y les avisa mediante `notificarCambio()`. `ServicioAprobacionCadena` y `EjecutorSolicitud` solo conocen a `NotificadorCambioEstado`, nunca a `SuscriptorCorreo`, `SuscriptorDashboard` ni `SuscriptorAuditoria` directamente. El test `agregarUnCuartoSuscriptorDePruebaNoRequiereModificarElMecanismo` demuestra que un cuarto suscriptor se agrega con `suscribir()` sin tocar `NotificadorCambioEstado`.

Se descartó State para este problema porque aquí el comportamiento que cambia no es el de la propia `Solicitud` (qué operaciones se le permite realizar según su estado), sino el de módulos completamente externos y ajenos a ella que deben reaccionar una vez que el estado ya cambió. La solicitud no necesita saber que existen un correo, un dashboard o una auditoría; son esos módulos quienes se enteran del cambio, exactamente lo contrario de lo que resuelve la Necesidad 4.

### Necesidad 4 — Reglas de transición según el estado

**Patrón aplicado:** State

**Justificación:** El problema exige reemplazar los if/else dispersos sobre `getEstado()` en varios métodos por un diseño donde agregar un estado nuevo (por ejemplo, `EN_ESPERA_PROVEEDOR`) no obligue a revisar múltiples lugares del código. Esto se resolvió con `EstadoSolicitud` como interfaz de estado, implementada por `EstadoPendiente`, `EstadoAprobada`, `EstadoEjecutada`, `EstadoRechazada` y `EstadoCancelada`, cada una decidiendo qué operaciones son válidas y a qué estado transiciona. `ContextoSolicitud` delega cada operación (`aprobar()`, `rechazar()`, `ejecutar()`, `cancelar()`) al estado actual, y una operación inválida simplemente devuelve el mismo estado sin cambiarlo, como confirma el test `ejecutarUnaSolicitudPendienteSeRechazaSinCambiarElEstado`.

Se descartó Strategy, visto en el pre-contenido de esta unidad, a pesar del parecido estructural (ambos tienen una interfaz con varias implementaciones intercambiables). La diferencia está en quién decide y en si hay transición. En Strategy, un cliente externo elige e inyecta explícitamente el comportamiento activo (como un carrito que activa la estrategia de descuento que desea usar). Aquí no hay ningún cliente externo seleccionando un comportamiento: es la propia `Solicitud`, a través de `ContextoSolicitud`, quien determina qué operación es válida según en qué estado se encuentra en ese instante de su historia, y además transiciona de un estado a otro como parte de resolver la operación, algo que un conjunto de estrategias independientes entre sí no hace por su cuenta.

### Reflexión — otros tres patrones (opcional)

1. Un reporte que recorre secuencialmente todas las solicitudes de un centro de costo sin exponer si están almacenadas en una lista, un mapa u otra estructura encajaría con **Iterator**, que permite recorrer una colección sin que el código cliente conozca su representación interna.
2. Los tres tipos de comprobante que comparten el mismo esqueleto de impresión (encabezado, cuerpo, pie) pero difieren solo en cómo llenan el cuerpo encajarían con **Template Method**, visto en el pre-contenido de esta unidad, definiendo el esqueleto fijo en una clase base y dejando que cada subclase implemente únicamente el paso variable.
3. Guardar y restaurar instantáneas completas del estado de una solicitud sin que el código que las guarda conozca los detalles internos de `Solicitud` encajaría con **Memento**. Se diferencia de lo construido en la Necesidad 2 en que Command encapsula una operación con su propia lógica de deshacer, mientras que Memento simplemente captura y devuelve un estado completo, sin ninguna operación asociada más que guardar y restaurar.



## Herramientas utilizadas
- Java 17, Spring Boot 3.2, Apache Maven, JUnit 5
- VS Code, Git, GitHub

## Conclusiones

Este laboratorio exigió, en cada una de las cuatro necesidades, distinguir un patrón de un vecino estructuralmente parecido antes de escribir cualquier código. La comparación más difícil fue la de la Necesidad 4: Strategy y State comparten la misma forma (una interfaz con varias implementaciones), y solo el análisis de quién decide el comportamiento y si existe una transición real permitió descartar Strategy con un argumento técnico y no solo intuitivo. La Necesidad 3 aportó una lección distinta: Observer y State pueden confundirse si no se distingue con precisión si el objeto que cambia es el mismo cuyo comportamiento varía (State) o si son terceros externos reaccionando a un cambio ya ocurrido (Observer). En conjunto, las cuatro necesidades reforzaron que la elección correcta de un patrón de comportamiento depende del problema exacto que resuelve, no de su parecido superficial con otro ya conocido.