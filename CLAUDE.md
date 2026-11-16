# GG Hub — contexto del proyecto para Claude Code

Proyecto de la materia **Programación Orientada a Objetos** (Universidad de La Sabana). Sistema en **Java 21** para el centro de e-sports **GG Hub**: 15 PCs, 10 consolas, barra de snacks y torneos. Los requerimientos (RF-01 a RF-12 y RNF-01) están en `README.md`.

- Código: proyecto de IntelliJ en `GGHUB/`, fuentes en `GGHUB/src`.
- Compilar y correr desde `GGHUB/src`: `javac -d <carpeta_salida> enums/*.java modelos/*.java servicios/*.java Main.java` y luego `java -cp <carpeta_salida> Main`.
- La estructura sigue el repo de ejemplo del profesor: https://github.com/wilmertri/SistemaGestionBiblioteca

## Cómo trabajar con quien te use (MUY IMPORTANTE)

- El estudiante **escribe el código él mismo, paso a paso**. No generes clases completas: describe **en español** qué debe hacer cada método (nombre, qué recibe, qué devuelve, pasos) y deja que lo transcriba a Java. Después **lee su archivo, compila y revisa**.
- En cada decisión explica **por qué** según los pilares de POO (encapsulamiento, abstracción, herencia, polimorfismo) y responsabilidad única.
- Cuando algo **no salga del UML o del README**, avisa antes de hacerlo.
- Al señalar errores, explica la causa (no solo la corrección) y pide que lo arregle él.
- Si algo ya se intentó varias veces sin éxito, está bien mostrar la línea exacta.
- Commits y push los hace el estudiante. No agregues líneas de co-autoría de Claude en los commits.

## Convenciones del código (respetarlas en todo lo nuevo)

- Paquetes: `modelos` (entidades), `servicios` (gestores y `ValidadorDatos`), `enums`, `interfaces`, `repositorios`, `persistencia`, `ui` (estos últimos aún vacíos).
- Atributos `private`. Identificadores y datos de hechos ya ocurridos con `final` y sin setter.
- El profesor **sí permite** `ArrayList`, genéricos y `enum` (el UML viejo dice lo contrario: está desactualizado).
- Un **gestor** por entidad en `servicios`: lista `private ArrayList<...>`, `registrar(...)` que valida antes de agregar, `buscarPor...()` que devuelve el objeto o `null`, `listar()` que devuelve **una copia** (`new ArrayList<>(lista)`), `cantidad()`.
- Errores de negocio: `throw new IllegalArgumentException("mensaje claro, con el dato que falló")`. En `Main`, métodos `private static intentarX(...)` con `try/catch` que imprimen `e.getMessage()`.
- Validaciones reutilizables en `servicios.ValidadorDatos` (todo `static`): `PATRON_DOCUMENTO`, `documentoValido(String)`, `precioValido(double)` (> 0), `validadorNegativo(double)` (true si el valor es < 0).
- Un gestor que necesita otro **lo recibe por constructor** (el mismo objeto del `Main`), nunca lo crea con `new`.
- `toString()` con `@Override` en cada modelo; las hijas usan `super.toString()`.
- Strings se comparan con `.equals()`; `int` y enums con `==`.
- Nada de `if` por tipo de clase (`instanceof`): usar polimorfismo (ej. `validar()` sobreescrito).

## Estado actual (RF-01 a RF-04 en `main`; RF-08 a RF-10 en la rama `torneos`, pendientes de merge)

| RF | Clases | Notas |
|---|---|---|
| RF-01 Clientes | `modelos.Cliente`, `servicios.GestorClientes`, `servicios.ValidadorDatos` | documento `final`, solo dígitos, sin duplicados |
| RF-02 Estaciones | `modelos.PuestoJuego` (abstracta), `EstacionPC`, `EstacionConsola`, `servicios.GestorPuestos` | constructor `protected`; `validar()` polimórfico (la consola valida además `tarifaControlExtra` no negativa); ids compartidos entre PC y consola |
| RF-03 Estado | `enums.EstadoPuesto` {DISPONIBLE, EN_USO, EN_MANTENIMIENTO} | estado arranca DISPONIBLE; `estaDisponible()`, `cambiarEstado()` (rechaza null); `crearSala()` en Main crea PCs 1-15 y consolas 16-25 |
| RF-04 Sesiones | `modelos.SesionJuego`, `servicios.GestorSesiones` | sesión con `final` cliente, puesto e inicio (`LocalDateTime.now()`), `controlesExtra` (cantidad). `iniciarSesion(documento, idPuesto, controlesExtra)` valida cliente, puesto, disponibilidad y controles; pone el puesto EN_USO |
| RF-08 Torneos (rama `torneos`) | `modelos.ModalidadTorneo` (abstracta, `getNombre()` abstracto), `EliminacionDirecta`, `FaseGrupos`, `modelos.Torneo`, `servicios.GestorTorneos` | `nombre` `final` es el identificador; `modalidad` tipada como la clase padre; `inscripcionesAbiertas` arranca `true`. `Torneo.validar()`: nombre no null/vacío, modalidad no null, fecha no null ni anterior a `LocalDateTime.now()` (`isBefore`), cupo ≥ 2, `precioValido(costo)`. `GestorTorneos.buscarPorNombre()` usa `equalsIgnoreCase`; `registrar()` llama `validar()` y rechaza nombres repetidos |
| RF-09 Inscripciones (rama `torneos`) | `modelos.Inscripcion`, cambios en `Torneo` y `GestorTorneos` | `Inscripcion`: `final` cliente, fecha (`now()`) y `montoPagado` (`double`, copia del costo al inscribirse). `Torneo` tiene `ArrayList<Inscripcion>`, `estaInscrito(documento)`, `cantidadInscritos()`, `estaLleno()`, `inscribir(Cliente)` (valida abiertas, repetido por documento y cupo; luego crea, agrega y devuelve) y `getInscripciones()` (copia). `GestorTorneos` recibe `GestorClientes` por constructor; `inscribirCliente(nombreTorneo, documento)` busca ambos y delega en `torneo.inscribir()` |
| RF-10 Cruces (rama `torneos`) | `modelos.Partida`, `generarCruces(ArrayList<Inscripcion>)` abstracto en `ModalidadTorneo`, cambios en `Torneo` y `GestorTorneos` | `Partida`: `final` ronda, jugador1, jugador2; `ganador` sin final; si `jugador2 == null` es pase directo y el ganador es jugador1; `esPaseDirecto()`, `estaJugada()`. `EliminacionDirecta`: parejas consecutivas (`i += 2`), el impar pasa directo. `FaseGrupos`: todos contra todos en un solo grupo (`j = i + 1`, n·(n−1)/2 partidas). `Torneo.cerrarInscripciones()` (exige ≥ 2 inscritos, solo cierra, no hay setter) y `generarCruces()` (exige inscripciones cerradas, no permite generar dos veces, `partidas = modalidad.generarCruces(...)` sin if por tipo). `GestorTorneos`: ayudante `private buscarTorneoExistente()` (lanza error si no existe), `cerrarInscripciones(nombre)`, `generarCruces(nombre)` |

`tarifaControlExtra` (precio por control, en `EstacionConsola`) ≠ `controlesExtra` (cantidad, en `SesionJuego`).

## Lo que falta y quién lo hace

- **Marco (dueño del repo, integración):** RF-05 finalizar y cobrar (interfaz `CalculableCobro`, cobro polimórfico PC/consola), RF-06 productos, RF-07 ventas de barra (usa `MetodoPago`), RF-12 cierre de caja, RNF-01 persistencia en `.txt`.
- **Compañero:** **torneos, RF-08 a RF-11** — `Torneo`, `Inscripcion`, `ModalidadTorneo` (abstracta) con `EliminacionDirecta` y `FaseGrupos`, `Partida`, y un `GestorTorneos`. Solo depende de `Cliente`/`GestorClientes` (no modificarlos sin avisar a Marco).

## Flujo de Git para trabajar en paralelo

- Nadie trabaja directo en `main`. El compañero usa su rama (por ejemplo `torneos`), Marco la suya.
- El compañero crea **clases nuevas**; no edita archivos de Marco.
- **El compañero no pone pruebas de torneos en `Main.java`** (el `Main` debe quedar limpio para la entrega). Claude prueba sus clases con un archivo aparte **fuera del repo** (carpeta temporal), compilando contra `GGHUB/src`, y le reporta los resultados en una tabla de casos.
- Al terminar cada RF, sube su rama; Marco revisa y hace el merge a `main`.

## UML

El UML original (HTML de la Entrega 2) tiene vacíos e inconsistencias. **No se parcha**: al terminar todos los RF se hará un **UML nuevo** sacado del código real. Ajustes ya conocidos respecto al original:

- Se agregaron `GestorClientes`, `GestorPuestos`, `GestorSesiones` y el enum `EstadoPuesto` (reemplaza las constantes `String` de estado en `PuestoJuego`).
- `PuestoJuego` usa atributos `private` y `validar()`; aún no implementa `CalculableCobro` (pendiente RF-05).
- `SesionJuego` tiene `cliente` y `puesto` (el UML no los tenía); `fin`, `total`, `consumos` y `cargos` llegan en RF-05/RF-07.
- Se agregó el grupo **Pagos**: interfaz `MetodoPago` (`pagar(monto)`, `obtenerComprobante()`) con `Efectivo`, `TarjetaCredito`, `PagoDigital`; la usará `Venta`. Aún no implementado.
- Pendientes menores: renombrar `especifiaciones` → `especificaciones` en `EstacionPC`; que el mensaje "id ya repetido" de `GestorPuestos` diga cuál id.
- **Torneos (RF-08):** se agregó `GestorTorneos`; `Torneo.nombre` es el identificador (el UML no le da id); `arbitro` no se implementa (ningún RF lo pide guardar); `ModalidadTorneo` tiene `getNombre()` abstracto (`generarCruces` y `procesarResultado` llegan en RF-10/RF-11).
- **Torneos (RF-09):** `Inscripcion` tiene `cliente` (el UML no lo tenía) y `montoPagado` en vez de `pagada` + `confirmarPago()` (el cobro se registra al inscribirse; `montoPagado` sirve al cierre de caja RF-12). `posicionFinal` se deja para RF-11. `Torneo` tiene la lista `inscripciones`. No usa `MetodoPago` todavía (es de Marco, aún no existe).
- **Torneos (RF-10):** `generarCruces` devuelve `ArrayList<Partida>` (no `Partida[]`) y recibe las inscripciones; `Partida` tiene `jugador1` y `jugador2` (el UML no los tenía) y representa el pase directo con `jugador2 == null`. Decisión de diseño: fase de grupos = un solo grupo todos contra todos (no varios grupos + eliminatoria). `marcador` / `registrarMarcador()` llegan en RF-11.
- **Torneos, pendiente para RF-11:** registrar resultados en `Partida`; avanzar ganadores de ronda en `EliminacionDirecta` (incluye pases directos); tabla de posiciones en `FaseGrupos` (el UML usa arreglos `participantes`/`puntos`: usar `ArrayList`).
- Pendiente menor de torneos: validar que `videojuego` no sea null/vacío en `Torneo.validar()` (recomendado, no exigido por el criterio de aceptación del RF-08).
