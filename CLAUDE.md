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

## Estado actual (RF-01 a RF-05 terminados)

| RF | Clases | Notas |
|---|---|---|
| RF-01 Clientes | `modelos.Cliente`, `servicios.GestorClientes`, `servicios.ValidadorDatos` | documento `final`, solo dígitos, sin duplicados |
| RF-02 Estaciones | `modelos.PuestoJuego` (abstracta), `EstacionPC`, `EstacionConsola`, `servicios.GestorPuestos` | constructor `protected`; `validar()` polimórfico (la consola valida además `tarifaControlExtra` no negativa); ids compartidos entre PC y consola |
| RF-03 Estado | `enums.EstadoPuesto` {DISPONIBLE, EN_USO, EN_MANTENIMIENTO} | estado arranca DISPONIBLE; `estaDisponible()`, `cambiarEstado()` (rechaza null); `crearSala()` en Main crea PCs 1-15 y consolas 16-25 |
| RF-04 Sesiones | `modelos.SesionJuego`, `servicios.GestorSesiones` | sesión con `final` cliente, puesto e inicio (`LocalDateTime.now()`), `controlesExtra` (cantidad). `iniciarSesion(documento, idPuesto, controlesExtra)` valida cliente, puesto, disponibilidad y controles; pone el puesto EN_USO |
| RF-05 Cobro | `interfaces.CalculableCobro`, `calcularCostoSesion(horas, controles)` en `EstacionPC` / `EstacionConsola` | `PuestoJuego implements CalculableCobro` sin implementarlo (lo hacen las hijas). `SesionJuego.finalizar()` guarda `fin`, minutos con `Duration` / 60.0, `total` = puesto.calcularCostoSesion (polimorfismo); rechaza finalizar dos veces; `estaActiva()`. `GestorSesiones.buscarSesionActiva(idPuesto)` y `finalizarSesion(idPuesto)` → puesto DISPONIBLE. Consumos de barra se sumarán en RF-07 |

`tarifaControlExtra` (precio por control, en `EstacionConsola`) ≠ `controlesExtra` (cantidad, en `SesionJuego`).

## Lo que falta y quién lo hace

- **Marco (dueño del repo, integración):** RF-06 productos, RF-07 ventas de barra (usa `MetodoPago`), RF-12 cierre de caja, RNF-01 persistencia en `.txt`.
- **Compañero:** **torneos, RF-08 a RF-11** — `Torneo`, `Inscripcion`, `ModalidadTorneo` (abstracta) con `EliminacionDirecta` y `FaseGrupos`, `Partida`, y un `GestorTorneos`. Solo depende de `Cliente`/`GestorClientes` (no modificarlos sin avisar a Marco).

## Flujo de Git para trabajar en paralelo

- Nadie trabaja directo en `main`. El compañero usa su rama (por ejemplo `torneos`), Marco la suya.
- El compañero crea **clases nuevas**; no edita archivos de Marco. En `Main.java` (archivo compartido) agrega sus pruebas en un método propio, por ejemplo `probarTorneos()`, sin tocar lo demás.
- Al terminar cada RF, sube su rama; Marco revisa y hace el merge a `main`.

## UML

El UML original (HTML de la Entrega 2) tiene vacíos e inconsistencias. **No se parcha**: al terminar todos los RF se hará un **UML nuevo** sacado del código real. Ajustes ya conocidos respecto al original:

- Se agregaron `GestorClientes`, `GestorPuestos`, `GestorSesiones` y el enum `EstadoPuesto` (reemplaza las constantes `String` de estado en `PuestoJuego`).
- `PuestoJuego` usa atributos `private` y `validar()`; implementa `CalculableCobro` pero deja `calcularCostoSesion(horas, controles)` a las hijas.
- `SesionJuego` tiene `cliente` y `puesto` (el UML no los tenía), `fin`, `total`, `finalizar()` y `estaActiva()`; no tiene `iniciar()` (el inicio lo hace el constructor y `GestorSesiones`). `consumos` y `cargos` llegan en RF-07.
- Se agregó el grupo **Pagos**: interfaz `MetodoPago` (`pagar(monto)`, `obtenerComprobante()`) con `Efectivo`, `TarjetaCredito`, `PagoDigital`; la usará `Venta`. Aún no implementado.
- Pendientes menores: renombrar `especifiaciones` → `especificaciones` en `EstacionPC`; que el mensaje "id ya repetido" de `GestorPuestos` diga cuál id.
- Si el compañero se aparta del UML en torneos, anotarlo aquí.
