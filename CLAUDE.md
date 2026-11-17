# GG Hub — contexto del proyecto para Claude Code

Proyecto de la materia **Programación Orientada a Objetos** (Universidad de La Sabana). Sistema en **Java 21** para el centro de e-sports **GG Hub**: 15 PCs, 10 consolas, barra de snacks y torneos. Los requerimientos (RF-01 a RF-12 y RNF-01) están en `README.md`.

- Código: proyecto de IntelliJ en `GGHUB/`, fuentes en `GGHUB/src`.
- Compilar y correr desde `GGHUB/src`: `javac -d <carpeta_salida> interfaces/*.java enums/*.java modelos/*.java servicios/*.java Main.java` y luego `java -cp <carpeta_salida> Main`.
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
- Validaciones reutilizables en `servicios.ValidadorDatos` (todo `static`): `PATRON_DOCUMENTO`, `documentoValido(String)`, `precioValido(double)` (> 0), `validadorNegativo(double)` (true si el valor es < 0), `textoValido(String)` (no null y no en blanco).
- Estados de estación: **nadie llama `cambiarEstado()` directo** desde el `Main` o la `ui`. `EN_USO` lo pone `GestorSesiones.iniciarSesion`; `DISPONIBLE` lo pone la propia `SesionJuego.finalizar(metodo)` después de cobrar (así la estación se libera por cualquier camino); el mantenimiento entra solo por `GestorPuestos.ponerEnMantenimiento(id)` / `quitarMantenimiento(id)`. `iniciarSesion` además rechaza si ya hay una sesión activa en esa estación.
- **Ventas solo por `GestorVentas.confirmar(...)`**, nunca `venta.confirmar(...)` directo: el gestor guarda la venta en su lista, y el cierre de caja (RF-12) sale de esa lista.
- **Cada `MetodoPago` se usa una sola vez**: un segundo `pagar()` sobre el mismo objeto lanza error (si no, se borraría el comprobante del primer pago).
- Un gestor que necesita otro **lo recibe por constructor** (el mismo objeto del `Main`), nunca lo crea con `new`.
- `toString()` con `@Override` en cada modelo; las hijas usan `super.toString()`.
- Strings se comparan con `.equals()`; `int` y enums con `==`.
- Nada de `if` por tipo de clase (`instanceof`): usar polimorfismo (ej. `validar()` sobreescrito).

## Estado actual (RF-01 a RF-12 terminados en `main`, con pagos; falta RNF-01, menú `ui`, README y UML)

| RF | Clases | Notas |
|---|---|---|
| RF-01 Clientes | `modelos.Cliente`, `servicios.GestorClientes`, `servicios.ValidadorDatos` | documento `final`, solo dígitos, sin duplicados |
| RF-02 Estaciones | `modelos.PuestoJuego` (abstracta), `EstacionPC`, `EstacionConsola`, `servicios.GestorPuestos` | constructor `protected`; `validar()` polimórfico (la consola valida además `tarifaControlExtra` no negativa); ids compartidos entre PC y consola |
| RF-03 Estado | `enums.EstadoPuesto` {DISPONIBLE, EN_USO, EN_MANTENIMIENTO} | estado arranca DISPONIBLE; `estaDisponible()`, `cambiarEstado()` (rechaza null); `crearSala()` en Main crea PCs 1-15 y consolas 16-25 |
| RF-04 Sesiones | `modelos.SesionJuego`, `servicios.GestorSesiones` | sesión con `final` cliente, puesto e inicio (`LocalDateTime.now()`), `controlesExtra` (cantidad). `iniciarSesion(documento, idPuesto, controlesExtra)` valida cliente, puesto, disponibilidad y controles; pone el puesto EN_USO |
| RF-05 Cobro | `interfaces.CalculableCobro`, `calcularCostoSesion(horas, controles)` en `EstacionPC` / `EstacionConsola` | `PuestoJuego implements CalculableCobro` sin implementarlo (lo hacen las hijas). `SesionJuego.finalizar()` guarda `fin`, minutos con `Duration` / 60.0, `total` = puesto.calcularCostoSesion (polimorfismo); rechaza finalizar dos veces; `estaActiva()`. `GestorSesiones.buscarSesionActiva(idPuesto)` y `finalizarSesion(idPuesto)` → puesto DISPONIBLE. Consumos de barra se sumarán en RF-07 |
| RF-06 Productos | `modelos.Producto`, `servicios.InventarioBarra` | `codigo` `final` (String); `validar()` exige código y precio, costo y stock no negativos; `setPrecioVenta()` valida; `agregarStock()` (> 0, suma); `hayStock(cant)`. Inventario: `registrarProducto`, `buscarProducto(codigo)`, `actualizarPrecio(codigo, precio)`, `reabastecer(codigo, cant)`, `listar()` copia, `cantidad()`. Categoría es `String` (no enum) porque el admin define nuevas |
| RF-07 Ventas | `enums.TipoVenta` {CONTADO, CARGO_SESION}, `modelos.ItemConsumo`, `modelos.Venta`, `servicios.GestorVentas` | `ItemConsumo` copia el precio del momento. `Producto.descontarStock()`. `Venta.confirmar(metodo)` es atómica: valida todo, cobra (si es contado) y solo después descuenta stock y carga consumos a la sesión. `SesionJuego` tiene `consumos`, `agregarConsumo` (solo activa) y `totalConsumos`; su total = puesto + consumos. `GestorVentas`: `nuevaVentaContado()`, `nuevaVentaCargoSesion(id)`, `agregarProducto(venta, codigo, cant)`, `confirmar(venta, metodo)` (contado) y `confirmar(venta)` (cargo, sobrecarga) |
| Pagos | `interfaces.MetodoPago` { `void pagar(monto)`, `String obtenerComprobante()` }, `modelos.Efectivo` (recibido, cambio), `TarjetaCredito` (4 dígitos, 1-36 cuotas), `PagoDigital` (plataforma, celular de 10 dígitos) | `pagar` lanza excepción si falla (no `boolean`). Se usa en la venta de contado y en `SesionJuego.finalizar(metodo)` / `GestorSesiones.finalizarSesion(id, metodo)`: se cobra antes de cerrar; si el pago falla, la sesión sigue abierta y la estación no se libera |
| RF-08 Torneos | `modelos.ModalidadTorneo` (abstracta, `getNombre()` abstracto), `EliminacionDirecta`, `FaseGrupos`, `modelos.Torneo`, `servicios.GestorTorneos` | `nombre` `final` es el identificador; `modalidad` tipada como la clase padre; `inscripcionesAbiertas` arranca `true`. `Torneo.validar()`: nombre no null/vacío, modalidad no null, fecha no null ni anterior a `LocalDateTime.now()` (`isBefore`), cupo ≥ 2, `precioValido(costo)`. `GestorTorneos.buscarPorNombre()` usa `equalsIgnoreCase`; `registrar()` llama `validar()` y rechaza nombres repetidos |
| RF-09 Inscripciones | `modelos.Inscripcion`, cambios en `Torneo` y `GestorTorneos` | `Inscripcion`: `final` cliente, fecha (`now()`) y `montoPagado` (`double`, copia del costo al inscribirse). `Torneo` tiene `ArrayList<Inscripcion>`, `estaInscrito(documento)`, `cantidadInscritos()`, `estaLleno()`, `inscribir(Cliente, MetodoPago)` (valida abiertas, repetido por documento, cupo y pago no null; **después** cobra con `metodoPago.pagar(costoInscripcion)` y al final crea, agrega y devuelve: nunca se cobra a quien no puede inscribirse, y si el cobro falla no queda inscrito) y `getInscripciones()` (copia). `Inscripcion` guarda además `final MetodoPago metodoPago` y su `toString()` muestra el comprobante; `getMontoPagado()` no cambió. `GestorTorneos` recibe `GestorClientes` por constructor (`final`); `inscribirCliente(nombreTorneo, documento, metodoPago)` busca ambos y delega en `torneo.inscribir()` |
| RF-10 Cruces | `modelos.Partida`, `generarCruces(ArrayList<Inscripcion>)` abstracto en `ModalidadTorneo`, cambios en `Torneo` y `GestorTorneos` | `Partida`: `final` ronda, jugador1, jugador2; `ganador` sin final; si `jugador2 == null` es pase directo y el ganador es jugador1; `esPaseDirecto()`, `estaJugada()`. `EliminacionDirecta`: parejas consecutivas (`i += 2`), el impar pasa directo. `FaseGrupos`: todos contra todos en un solo grupo (`j = i + 1`, n·(n−1)/2 partidas). `Torneo.cerrarInscripciones()` (exige ≥ 2 inscritos, solo cierra, no hay setter) y `generarCruces()` (exige inscripciones cerradas, no permite generar dos veces, `partidas = modalidad.generarCruces(...)` sin if por tipo). `GestorTorneos`: ayudante `private buscarTorneoExistente()` (lanza error si no existe), `cerrarInscripciones(nombre)`, `generarCruces(nombre)` |
| RF-11 Resultados | `modelos.PosicionGrupo`, `procesarResultado(ArrayList<Partida>)` y `obtenerPosiciones()` abstractos en `ModalidadTorneo`, cambios en `Partida`, `EliminacionDirecta`, `FaseGrupos`, `Torneo`, `GestorTorneos` | `Partida.registrarResultado(documentoGanador, marcador)`: rechaza pase directo, partida ya jugada, marcador vacío y ganador que no juega (compara con `equals`); sin empates; `marcador` es `String`. `EliminacionDirecta`: ayudante `private emparejar(jugadores, ronda)` (lo usan `generarCruces` y `procesarResultado`); la ronda actual es la de la última partida; cuando toda la ronda tiene ganador arma la siguiente, y si queda uno lo guarda en `campeon`. `FaseGrupos`: `tabla` de `PosicionGrupo` recalculada desde cero en cada `procesarResultado` (nunca suma dos veces), `obtenerPosiciones()` ordena por puntos con `sort`. `PosicionGrupo`: `final` cliente, `victorias`, `PUNTOS_POR_VICTORIA = 3`, `sumarVictorias()` sin setter, `getPuntos()` calculado. `Torneo.registrarResultado(numeroPartida, documento, marcador)` (número desde 1) y `partidas.addAll(modalidad.procesarResultado(partidas))`; `obtenerPosiciones()` delega. `GestorTorneos.registrarResultado(nombre, ...)` y `obtenerPosiciones(nombre)` |

| RF-12 Cierre de caja | `modelos.CierreCaja`, `servicios.GestorCaja`; `SesionJuego.costoTiempo` / `costoControles`; `GestorSesiones.listar()` | `SesionJuego.finalizar` separa el cobro: tiempo = `calcularCostoSesion(horas, 0)`, controles = `calcularCostoSesion(horas, controles) - tiempo` (polimórfico, en PC da 0). `CierreCaja`: todo `final`, `fecha` (`LocalDate.now()`, no se recibe), `operador` (`String`, validado con `textoValido`), 4 totales y 3 cantidades, `calcularTotal()`, `generarResumen()` y `toString()`. `GestorCaja(gestorSesiones, gestorVentas, gestorTorneos).cerrarCaja(operador)`: suma las sesiones cobradas hoy (tiempo, controles y consumos → barra), solo ventas de **CONTADO** de hoy (las de cargo a sesión ya vienen en los consumos: no contar dos veces) y las inscripciones de hoy (`getMontoPagado()`); guarda y devuelve el cierre. `listar()` copia |

`tarifaControlExtra` (precio por control, en `EstacionConsola`) ≠ `controlesExtra` (cantidad, en `SesionJuego`).

## Lo que falta y quién lo hace

Torneos (RF-08 a RF-11) ya están terminados y unidos en `main`. Lo que queda se reparte en 3 fases:

**Fase 1 (en paralelo, ahora)**
- **Marco:** ~~RF-12 cierre de caja~~ (terminado). Sus arreglos medios: nombre vacío en `Cliente`, id ≤ 0 en estaciones, mensajes ("id ya repetido" sin el id, reabastecer 0 dice "negativo"), `especifiaciones` → `especificaciones`.
- **Compañero (rama `torneos`, primero `git merge origin/main`):**
  1. ✅ (hecho) Arreglos de torneos: `Torneo.validar()` rechaza `videojuego` vacío (`ValidadorDatos.textoValido`); `final` en `gestorClientes` de `GestorTorneos`; `toString()` de `Torneo` e `Inscripcion` con el estilo del proyecto (`[TORNEO] nombre | ...`).
  2. ✅ (hecho) Cobrar la inscripción con `MetodoPago` (RF-09 "registrando el cobro"): `inscribirCliente` recibe un `MetodoPago` y llama `metodo.pagar(costo)` **antes** de inscribir (si falla, no se inscribe). **No cambiar `Inscripcion.getMontoPagado()`**: lo usa el cierre de caja.
  3. ✅ (hecho) Base de la persistencia (RNF-01): `persistencia/FileManage.java` e `interfaces/IRepository.java`, como en el repo del profe. Separador `|`, carpeta `files/`, un `.txt` por entidad. Copiadas tal cual del profe, sin agregados. Los archivos van en `GGHUB/src/files/` (esa carpeta tiene que existir).

**Fase 2 (después de unir la fase 1)**
- **Marco:** repositorios de sus clases (clientes, puestos, sesiones, productos, ventas). Necesitan un segundo constructor para cargar desde archivo en `SesionJuego`, `PuestoJuego`, `Venta` e `ItemConsumo` (hoy ponen `now()`, `DISPONIBLE` o copian el precio actual).
- **Compañero:** repositorios de torneos e inscripciones, con un segundo constructor en `Torneo` para cargar torneos con fecha pasada.

**Fase 3 (juntos, al final)**
- Menú de consola en `ui` (`MenuPrincipal`, `MenuClientes`, `MenuPuestos`, ...): el compañero hace `MenuTorneos`, Marco el resto. Las pruebas actuales salen del `Main` a una clase de demostración (como `DemoAlmacenamiento` del profe) y el `Main` solo arranca el menú.
- README (matriz en "Terminado") y UML nuevo sacado del código.
- Ensayo de sustentación: cada uno debe poder explicar la parte del otro.

## Flujo de Git para trabajar en paralelo

- Nadie trabaja directo en `main`. El compañero usa su rama (por ejemplo `torneos`), Marco la suya.
- El compañero crea **clases nuevas**; no edita archivos de Marco.
- **El compañero no pone pruebas de torneos en `Main.java`** (el `Main` debe quedar limpio para la entrega). Claude prueba sus clases con un archivo aparte **fuera del repo** (carpeta temporal), compilando contra `GGHUB/src`, y le reporta los resultados en una tabla de casos.
- Al terminar cada RF, sube su rama; Marco revisa y hace el merge a `main`.

## UML

El UML original (HTML de la Entrega 2) tiene vacíos e inconsistencias. **No se parcha**: al terminar todos los RF se hará un **UML nuevo** sacado del código real. Ajustes ya conocidos respecto al original:

- Se agregaron `GestorClientes`, `GestorPuestos`, `GestorSesiones` y el enum `EstadoPuesto` (reemplaza las constantes `String` de estado en `PuestoJuego`).
- `PuestoJuego` usa atributos `private` y `validar()`; implementa `CalculableCobro` pero deja `calcularCostoSesion(horas, controles)` a las hijas.
- `SesionJuego` tiene `cliente` y `puesto` (el UML no los tenía), `fin`, `total`, `finalizar()` y `estaActiva()`; no tiene `iniciar()` (el inicio lo hace el constructor y `GestorSesiones`). `consumos` y `cargos` llegan en RF-07.
- Grupo **Pagos** implementado: `MetodoPago` con `pagar(monto)` **void** que lanza excepción (el UML decía `boolean`), `Efectivo`, `TarjetaCredito`, `PagoDigital`. Lo usan `Venta` (contado) y `SesionJuego` (al finalizar).
- `TipoVenta` es enum (el UML tenía constantes `String` `CONTADO`/`CARGO_SESION` en `Venta`). `Venta` e `InventarioBarra` usan `ArrayList`, no arreglos. `Producto` tiene `descontarStock`/`agregarStock` en vez de `actualizarStock`. `ItemConsumo` guarda `precioUnitario`.
- Pendientes menores: renombrar `especifiaciones` → `especificaciones` en `EstacionPC`; que el mensaje "id ya repetido" de `GestorPuestos` diga cuál id.
- **Torneos (RF-08):** se agregó `GestorTorneos`; `Torneo.nombre` es el identificador (el UML no le da id); `arbitro` no se implementa (ningún RF lo pide guardar); `ModalidadTorneo` tiene `getNombre()` abstracto (`generarCruces` y `procesarResultado` llegan en RF-10/RF-11).
- **Torneos (RF-09):** `Inscripcion` tiene `cliente` (el UML no lo tenía) y `montoPagado` en vez de `pagada` + `confirmarPago()` (el cobro se registra al inscribirse; `montoPagado` sirve al cierre de caja RF-12). `posicionFinal` se deja para RF-11. `Torneo` tiene la lista `inscripciones`. No usa `MetodoPago` todavía (es de Marco, aún no existe).
- **Torneos (RF-10):** `generarCruces` devuelve `ArrayList<Partida>` (no `Partida[]`) y recibe las inscripciones; `Partida` tiene `jugador1` y `jugador2` (el UML no los tenía) y representa el pase directo con `jugador2 == null`. Decisión de diseño: fase de grupos = un solo grupo todos contra todos (no varios grupos + eliminatoria). `marcador` / `registrarMarcador()` llegan en RF-11.
- **Torneos (RF-11):** `procesarResultado` recibe todas las partidas (no una sola `p`) y devuelve las partidas nuevas; se agregó `obtenerPosiciones()` abstracto. `FaseGrupos` usa `ArrayList<PosicionGrupo>` en vez de `participantes[]` + `puntos[]`, y `actualizarTabla()` es el recálculo dentro de `procesarResultado`. `Partida.registrarMarcador()` del UML es `registrarResultado(documento, marcador)`; `obtenerGanador()` es `getGanador()`. `Inscripcion.posicionFinal` no se implementó (la posición sale de `obtenerPosiciones()`). Decisiones: sin empates, 3 puntos por victoria, el árbitro identifica la partida por su número (desde 1).
- **Pendiente tras el merge:** usar `ValidadorDatos.textoValido()` en `Torneo.validar()` para el nombre y para el `videojuego` (todavía sin validar).
- **RF-12 (decisión):** el operador del cierre de caja es solo su nombre (`String`). No se implementan `UsuarioSistema`, `Operador`, `AdministradorSala` ni `Arbitro` → quitarlos del UML nuevo.
- Pendiente menor de torneos: validar que `videojuego` no sea null/vacío en `Torneo.validar()` (recomendado, no exigido por el criterio de aceptación del RF-08).
