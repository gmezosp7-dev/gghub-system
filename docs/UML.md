# GG Hub — Diagrama de clases (versión final)

Diagrama sacado directamente del código final (`GGHUB/src`). Reemplaza al diagrama de la Entrega 2; los cambios respecto a ese diseño están explicados en el [README](../README.md#cambios-respecto-al-diseño-de-la-entrega-2).

Para que se pueda leer, está dividido por áreas. Cuando una clase de otra área aparece como una caja vacía, es solo una referencia: su detalle está en su propia sección.

**Notación**

| Símbolo | Significado |
| :--- | :--- |
| `+` / `-` / `#` | público / privado / protegido |
| `final` | se asigna una sola vez (identificadores y hechos ya ocurridos) |
| subrayado | miembro estático (`static`) |
| cursiva | método abstracto |
| `<<abstract>>` / `<<interface>>` / `<<enumeration>>` | clase abstracta / interfaz / enum |
| flecha con triángulo vacío, línea continua | herencia (`extends`) |
| flecha con triángulo vacío, línea punteada | implementa una interfaz (`implements`) |
| rombo relleno | composición: la parte no existe sin el todo |
| rombo vacío | agregación: el gestor guarda la lista |
| flecha simple | asociación: tiene una referencia a |
| flecha punteada | dependencia: lo usa |

---

## 1. Vista general por capas

```mermaid
flowchart TB
    Main["Main"] --> UI
    subgraph UI["ui — menús de consola"]
        MP["MenuPrincipal"]
        MS["MenuClientes · MenuPuestos · MenuSesiones · MenuBarra · MenuTorneos · MenuCaja · Demo"]
    end
    subgraph SERV["servicios — reglas de negocio"]
        GS["GestorClientes · GestorPuestos · GestorSesiones · InventarioBarra · GestorVentas · GestorTorneos · GestorCaja · ValidadorDatos"]
    end
    subgraph REPO["repositorios + persistencia — archivos .txt (RNF-01)"]
        RP["ClienteRepository · PuestoRepository · ProductoRepository · SesionRepository · VentaRepository · TorneoRepository"]
        FM["FileManage"]
    end
    subgraph MOD["modelos + enums + interfaces"]
        MD["Cliente · PuestoJuego · EstacionPC · EstacionConsola · SesionJuego · Producto · ItemConsumo · Venta · CierreCaja · Torneo · Inscripcion · Partida · PosicionGrupo · ModalidadTorneo · EliminacionDirecta · FaseGrupos · Efectivo · TarjetaCredito · PagoDigital"]
        EN["EstadoPuesto · TipoVenta"]
        IN["CalculableCobro · MetodoPago · IRepository~T~"]
    end
    UI --> SERV
    UI --> REPO
    SERV --> MOD
    REPO --> MOD
    RP --> FM
```

El menú principal crea los gestores **una sola vez**; los gestores que necesitan otros los reciben **por constructor** (el mismo objeto), y los repositorios cargan los datos al arrancar y los guardan al volver de cada submenú.

---

## 2. Clientes y validación (RF-01)

```mermaid
classDiagram
    class Cliente {
        -final String documento
        -String nombre
        -String telefono
        -String correo
        +Cliente(String documento, String nombre, String telefono, String correo)
        +getDocumento() String
        +getNombre() String
        +setNombre(String) void
        +getTelefono() String
        +setTelefono(String) void
        +getCorreo() String
        +setCorreo(String) void
        +toString() String
    }
    class GestorClientes {
        -ArrayList~Cliente~ clientes
        +registrar(Cliente) void
        +buscarPorDocumento(String) Cliente
        +listar() ArrayList~Cliente~
        +cantidad() int
    }
    class ValidadorDatos {
        +final String PATRON_DOCUMENTO$
        +documentoValido(String) boolean$
        +precioValido(double) boolean$
        +textoValido(String) boolean$
        +validadorNegativo(double) boolean$
    }
    GestorClientes "1" o-- "0..*" Cliente : clientes
    GestorClientes ..> ValidadorDatos : valida el documento
```

- El `documento` es la identidad del cliente: es `final` y no tiene setter.
- `registrar` rechaza documentos que no sean solo dígitos y documentos repetidos. `listar` devuelve una copia de la lista.
- `ValidadorDatos` solo tiene miembros estáticos: se usa desde todo el sistema sin crear objetos.

---

## 3. Estaciones, sesiones y cobro (RF-02 a RF-05)

```mermaid
classDiagram
    class CalculableCobro {
        <<interface>>
        +calcularCostoSesion(double horas, int controlesExtra) double*
    }
    class PuestoJuego {
        <<abstract>>
        -final int id
        -String modelo
        -double tarifaHora
        -EstadoPuesto estado
        #PuestoJuego(int id, String modelo, double tarifaHora)
        +getId() int
        +getModelo() String
        +getTarifaHora() double
        +getEstado() EstadoPuesto
        +estaDisponible() boolean
        +cambiarEstado(EstadoPuesto) void
        +validar() void
        +aLinea() String*
        +toString() String
    }
    class EstacionPC {
        -String especifiaciones
        +EstacionPC(int id, String modelo, double tarifaHora, String especifiaciones)
        +getEspecifiaciones() String
        +calcularCostoSesion(double, int) double
        +aLinea() String
        +toString() String
    }
    class EstacionConsola {
        -String tipoConsola
        -double tarifaControlExtra
        +EstacionConsola(int id, String modelo, double tarifaHora, String tipoConsola, double tarifaControlExtra)
        +getTipoConsola() String
        +getTarifaControlExtra() double
        +calcularCostoSesion(double, int) double
        +validar() void
        +aLinea() String
        +toString() String
    }
    class EstadoPuesto {
        <<enumeration>>
        DISPONIBLE
        EN_USO
        EN_MANTENIMIENTO
    }
    class SesionJuego {
        -final Cliente cliente
        -final PuestoJuego puesto
        -final LocalDateTime inicio
        -int controlesExtra
        -LocalDateTime fin
        -double total
        -double costoTiempo
        -double costoControles
        -ArrayList~ItemConsumo~ consumos
        -MetodoPago metodoPago
        +SesionJuego(Cliente, PuestoJuego, int controlesExtra)
        +SesionJuego(Cliente, PuestoJuego, int, LocalDateTime inicio, LocalDateTime fin, double total, double costoTiempo, double costoControles, ArrayList~ItemConsumo~)
        +getCliente() Cliente
        +getPuesto() PuestoJuego
        +getInicio() LocalDateTime
        +getFin() LocalDateTime
        +getControlesExtra() int
        +getTotal() double
        +getCostoTiempo() double
        +getCostoControles() double
        +getMetodoPago() MetodoPago
        +estaActiva() boolean
        +finalizar(MetodoPago) double
        +agregarConsumo(ItemConsumo) void
        +totalConsumos() double
        +listar() ArrayList~ItemConsumo~
        +toString() String
    }
    class GestorPuestos {
        -ArrayList~PuestoJuego~ puestos
        +registrar(PuestoJuego) void
        +buscarPorId(int) PuestoJuego
        +ponerEnMantenimiento(int id) void
        +quitarMantenimiento(int id) void
        +listar() ArrayList~PuestoJuego~
        +cantidad() int
    }
    class GestorSesiones {
        -ArrayList~SesionJuego~ sesiones
        -final GestorClientes gestorClientes
        -final GestorPuestos gestorPuestos
        +GestorSesiones(GestorClientes, GestorPuestos)
        +iniciarSesion(String documento, int idPuesto, int controlesExtra) SesionJuego
        +buscarSesionActiva(int idPuesto) SesionJuego
        +finalizarSesion(int idPuesto, MetodoPago) SesionJuego
        +cargar(SesionJuego) void
        +listar() ArrayList~SesionJuego~
    }

    CalculableCobro <|.. PuestoJuego
    PuestoJuego <|-- EstacionPC
    PuestoJuego <|-- EstacionConsola
    PuestoJuego --> "1" EstadoPuesto : estado
    GestorPuestos "1" o-- "0..*" PuestoJuego : puestos
    SesionJuego --> "1" Cliente : cliente
    SesionJuego --> "1" PuestoJuego : puesto
    SesionJuego "1" *-- "0..*" ItemConsumo : consumos
    SesionJuego --> "0..1" MetodoPago : pagada con
    GestorSesiones "1" o-- "0..*" SesionJuego : sesiones
    GestorSesiones --> GestorClientes
    GestorSesiones --> GestorPuestos
```

- **Herencia y abstracción:** no existe "un puesto" a secas; `PuestoJuego` es abstracta y su constructor es `protected` (solo lo llaman las hijas con `super`). Los ids son compartidos entre PC y consola (PCs 1–15, consolas 16–25).
- **Polimorfismo:** `PuestoJuego` implementa `CalculableCobro` pero deja `calcularCostoSesion` a las hijas: el PC cobra horas × tarifa y la consola suma los controles extra. `validar()` (la consola revisa además su tarifa por control) y `aLinea()` (la línea que se guarda en el archivo) también se sobreescriben.
- **Estado:** `EN_USO` lo pone `iniciarSesion`; `DISPONIBLE` lo pone la propia sesión al cobrarse en `finalizar(metodo)`; el mantenimiento entra solo por `GestorPuestos`.
- **Cobro:** `finalizar` calcula el tiempo (por minutos), separa `costoTiempo` y `costoControles`, suma los consumos, **cobra** con el método de pago y solo si el pago sale bien cierra la sesión. El segundo constructor se usa para cargar sesiones guardadas.

---

## 4. Barra y ventas (RF-06 y RF-07)

```mermaid
classDiagram
    class Producto {
        -final String codigo
        -String nombre
        -String categoria
        -double costo
        -double precioVenta
        -int stock
        +Producto(String codigo, String nombre, String categoria, double costo, double precioVenta, int stock)
        +getCodigo() String
        +getNombre() String
        +getCategoria() String
        +getCosto() double
        +getPrecioVenta() double
        +getStock() int
        +setPrecioVenta(double) void
        +agregarStock(int) void
        +descontarStock(int) void
        +hayStock(int) boolean
        +validar() void
        +toString() String
    }
    class ItemConsumo {
        -final Producto producto
        -final int cantidad
        -final double precioUnitario
        +ItemConsumo(Producto, int cantidad)
        +ItemConsumo(Producto, int cantidad, double precioUnitario)
        +getProducto() Producto
        +getCantidad() int
        +getPrecioUnitario() double
        +subtotal() double
        +toString() String
    }
    class Venta {
        -final LocalDateTime fechaHora
        -final TipoVenta tipo
        -final SesionJuego sesion
        -ArrayList~ItemConsumo~ items
        -boolean confirmada
        -MetodoPago metodoPago
        +Venta(TipoVenta, SesionJuego)
        +Venta(TipoVenta, SesionJuego, LocalDateTime fechaHora, ArrayList~ItemConsumo~)
        +getFechaHora() LocalDateTime
        +getTipo() TipoVenta
        +getSesion() SesionJuego
        +getItems() ArrayList~ItemConsumo~
        +isConfirmada() boolean
        +getMetodoPago() MetodoPago
        +agregarItem(Producto, int cantidad) void
        +calcularTotal() double
        +confirmar(MetodoPago) void
        +toString() String
    }
    class TipoVenta {
        <<enumeration>>
        CONTADO
        CARGO_SESION
    }
    class InventarioBarra {
        -ArrayList~Producto~ productos
        +registrarProducto(Producto) void
        +buscarProducto(String codigo) Producto
        +actualizarPrecio(String codigo, double) void
        +reabastecer(String codigo, int cantidad) void
        +listar() ArrayList~Producto~
        +cantidad() int
    }
    class GestorVentas {
        -ArrayList~Venta~ ventas
        -final InventarioBarra inventarioBarra
        -final GestorSesiones gestorSesiones
        +GestorVentas(InventarioBarra, GestorSesiones)
        +nuevaVentaContado() Venta
        +nuevaVentaCargoSesion(int idPuesto) Venta
        +agregarProducto(Venta, String codigo, int cantidad) void
        +confirmar(Venta, MetodoPago) void
        +confirmar(Venta) void
        +cargar(Venta) void
        +listar() ArrayList~Venta~
        +cantidad() int
    }

    InventarioBarra "1" o-- "0..*" Producto : productos
    ItemConsumo --> "1" Producto : producto
    Venta "1" *-- "1..*" ItemConsumo : items
    Venta --> "1" TipoVenta : tipo
    Venta --> "0..1" SesionJuego : cargada a
    Venta --> "0..1" MetodoPago : pagada con
    GestorVentas "1" o-- "0..*" Venta : ventas
    GestorVentas --> InventarioBarra
    GestorVentas --> GestorSesiones
```

- El `codigo` del producto es su identidad (`final`). El stock solo cambia con movimientos que validan: `agregarStock` (mayor a cero) y `descontarStock` (no deja vender más de lo que hay). No existe `setStock`.
- `ItemConsumo` **copia el precio** del producto en el momento de la venta: si el precio cambia después, las ventas pasadas no se alteran.
- `Venta.confirmar` es **atómica**: valida todo (productos, sesión abierta, stock), cobra si es de contado, y solo después descuenta el stock y, si es `CARGO_SESION`, agrega los items a los consumos de la sesión (se pagan al finalizarla).
- `GestorVentas.confirmar` tiene **sobrecarga**: con método de pago (contado) y sin él (cargo a sesión).

---

## 5. Pagos

```mermaid
classDiagram
    class MetodoPago {
        <<interface>>
        +pagar(double monto) void*
        +obtenerComprobante() String*
    }
    class Efectivo {
        -final double montoRecibido
        -double montoAPagar
        -double cambio
        -boolean usado
        +Efectivo(double montoRecibido)
        +getCambio() double
        +pagar(double) void
        +obtenerComprobante() String
    }
    class TarjetaCredito {
        -final String ultimosDigitos
        -final int cuotas
        -double montoPagado
        -boolean usado
        +TarjetaCredito(String ultimosDigitos, int cuotas)
        +pagar(double) void
        +obtenerComprobante() String
    }
    class PagoDigital {
        -final String plataforma
        -final String celular
        -double montoPagado
        -boolean usado
        +PagoDigital(String plataforma, String celular)
        +pagar(double) void
        +obtenerComprobante() String
    }
    MetodoPago <|.. Efectivo
    MetodoPago <|.. TarjetaCredito
    MetodoPago <|.. PagoDigital
    SesionJuego ..> MetodoPago : paga al finalizar
    Venta ..> MetodoPago : paga la venta de contado
    Torneo ..> MetodoPago : paga la inscripción
```

- **Polimorfismo:** la sesión, la venta y el torneo solo llaman `metodo.pagar(total)`, sin saber cuál es. El efectivo rechaza si no alcanza y calcula el cambio; la tarjeta exige 4 dígitos y de 1 a 36 cuotas; el pago digital exige plataforma y un celular de 10 dígitos.
- `pagar` lanza una excepción si el pago no se puede hacer (no devuelve `boolean`), así cada forma de pago explica su motivo.
- Cada objeto de pago se usa **una sola vez** (`usado`): un segundo `pagar` lanza error.

---

## 6. Torneos (RF-08 a RF-11)

```mermaid
classDiagram
    class Torneo {
        -final String nombre
        -String videojuego
        -LocalDateTime fecha
        -int cupoMaximo
        -double costoInscripcion
        -ModalidadTorneo modalidad
        -boolean inscripcionesAbiertas
        -ArrayList~Inscripcion~ inscripciones
        -ArrayList~Partida~ partidas
        +Torneo(String nombre, String videojuego, LocalDateTime fecha, int cupoMaximo, double costo, ModalidadTorneo)
        +Torneo(String, String, LocalDateTime, int, double, ModalidadTorneo, boolean inscripcionesAbiertas)
        +getNombre() String
        +getVideojuego() String
        +getFecha() LocalDateTime
        +getCupoMaximo() int
        +getCostoInscripcion() double
        +getModalidad() ModalidadTorneo
        +isInscripcionesAbiertas() boolean
        +validar() void
        +estaInscrito(String documento) boolean
        +cantidadInscritos() int
        +estaLleno() boolean
        +inscribir(Cliente, MetodoPago) Inscripcion
        +getInscripciones() ArrayList~Inscripcion~
        +cerrarInscripciones() void
        +generarCruces() ArrayList~Partida~
        +getPartidas() ArrayList~Partida~
        +registrarResultado(int numeroPartida, String documentoGanador, String marcador) void
        +obtenerPosiciones() String
        +cargarInscripcion(Inscripcion) void
        +cargarPartidas(ArrayList~Partida~) void
        +toString() String
    }
    class Inscripcion {
        -final Cliente cliente
        -final LocalDateTime fecha
        -final double montoPagado
        -final MetodoPago metodoPago
        +Inscripcion(Cliente, double montoPagado, MetodoPago)
        +Inscripcion(Cliente, double montoPagado, LocalDateTime fecha)
        +getCliente() Cliente
        +getFecha() LocalDateTime
        +getMontoPagado() double
        +getMetodoPago() MetodoPago
        +toString() String
    }
    class Partida {
        -final int ronda
        -final Cliente jugador1
        -final Cliente jugador2
        -Cliente ganador
        -String marcador
        +Partida(int ronda, Cliente jugador1, Cliente jugador2)
        +Partida(int, Cliente, Cliente, Cliente ganador, String marcador)
        +getRonda() int
        +getJugador1() Cliente
        +getJugador2() Cliente
        +getGanador() Cliente
        +getMarcador() String
        +esPaseDirecto() boolean
        +estaJugada() boolean
        +registrarResultado(String documentoGanador, String marcador) void
        +toString() String
    }
    class ModalidadTorneo {
        <<abstract>>
        +getNombre() String*
        +generarCruces(ArrayList~Inscripcion~) ArrayList~Partida~*
        +procesarResultado(ArrayList~Partida~) ArrayList~Partida~*
        +obtenerPosiciones() String*
    }
    class EliminacionDirecta {
        -Cliente campeon
        +getNombre() String
        +generarCruces(ArrayList~Inscripcion~) ArrayList~Partida~
        +procesarResultado(ArrayList~Partida~) ArrayList~Partida~
        +obtenerPosiciones() String
        -emparejar(ArrayList~Cliente~, int ronda) ArrayList~Partida~
    }
    class FaseGrupos {
        -ArrayList~PosicionGrupo~ tabla
        +getNombre() String
        +generarCruces(ArrayList~Inscripcion~) ArrayList~Partida~
        +procesarResultado(ArrayList~Partida~) ArrayList~Partida~
        +obtenerPosiciones() String
        -buscarPosicion(Cliente) PosicionGrupo
    }
    class PosicionGrupo {
        -final Cliente cliente
        -int victorias
        -final int PUNTOS_POR_VICTORIA$
        +PosicionGrupo(Cliente)
        +getCliente() Cliente
        +getVictorias() int
        +sumarVictorias() void
        +getPuntos() int
        +toString() String
    }
    class GestorTorneos {
        -ArrayList~Torneo~ torneos
        -final GestorClientes gestorClientes
        +GestorTorneos(GestorClientes)
        +registrar(Torneo) void
        +buscarPorNombre(String) Torneo
        +inscribirCliente(String nombreTorneo, String documento, MetodoPago) Inscripcion
        +cerrarInscripciones(String nombreTorneo) void
        +generarCruces(String nombreTorneo) ArrayList~Partida~
        +registrarResultado(String nombreTorneo, int numeroPartida, String documentoGanador, String marcador) void
        +obtenerPosiciones(String nombreTorneo) String
        +cargar(Torneo) void
        +listar() ArrayList~Torneo~
        +cantidad() int
        -buscarTorneoExistente(String) Torneo
    }

    Torneo "1" *-- "0..*" Inscripcion : inscripciones
    Torneo "1" *-- "0..*" Partida : partidas
    Torneo --> "1" ModalidadTorneo : modalidad
    ModalidadTorneo <|-- EliminacionDirecta
    ModalidadTorneo <|-- FaseGrupos
    FaseGrupos "1" *-- "0..*" PosicionGrupo : tabla
    EliminacionDirecta --> "0..1" Cliente : campeon
    Inscripcion --> "1" Cliente
    Inscripcion --> "0..1" MetodoPago
    Partida --> "1..2" Cliente : jugadores y ganador
    PosicionGrupo --> "1" Cliente
    GestorTorneos "1" o-- "0..*" Torneo : torneos
    GestorTorneos --> GestorClientes
```

- El torneo se identifica por su `nombre` (`final`). `validar` exige nombre, videojuego, modalidad, fecha no anterior a la actual, cupo de al menos 2 y costo mayor a cero.
- `inscribir` valida que estén abiertas, que no esté repetido y que haya cupo; **después cobra** con el método de pago y solo entonces crea la inscripción. `Inscripcion` guarda el `montoPagado` (lo usa el cierre de caja).
- **Polimorfismo:** el torneo le pide a su modalidad que genere los cruces, procese los resultados y muestre las posiciones, sin preguntar cuál es. Eliminación directa empareja de a dos (si son impares, uno pasa directo) y arma la siguiente ronda con los ganadores hasta que queda el campeón. Fase de grupos es todos contra todos y recalcula la tabla desde cero con cada resultado (3 puntos por victoria, sin empates).
- Los segundos constructores de `Torneo`, `Inscripcion` y `Partida`, y `cargarInscripcion` / `cargarPartidas`, se usan para recuperar los torneos guardados sin volver a validar fechas pasadas ni a cobrar.

---

## 7. Cierre de caja (RF-12)

```mermaid
classDiagram
    class CierreCaja {
        -final LocalDate fecha
        -final String operador
        -final double totalJuego
        -final double totalControles
        -final double totalBarra
        -final double totalTorneos
        -final int cantidadSesiones
        -final int cantidadVentas
        -final int cantidadInscripciones
        +CierreCaja(String operador, double totalJuego, double totalControles, double totalBarra, double totalTorneos, int cantidadSesiones, int cantidadVentas, int cantidadInscripciones)
        +getFecha() LocalDate
        +getOperador() String
        +getTotalJuego() double
        +getTotalControles() double
        +getTotalBarra() double
        +getTotalTorneos() double
        +getCantidadSesiones() int
        +getCantidadVentas() int
        +getCantidadInscripciones() int
        +calcularTotal() double
        +generarResumen() String
        +toString() String
    }
    class GestorCaja {
        -ArrayList~CierreCaja~ cierres
        -final GestorSesiones gestorSesiones
        -final GestorVentas gestorVentas
        -final GestorTorneos gestorTorneos
        +GestorCaja(GestorSesiones, GestorVentas, GestorTorneos)
        +cerrarCaja(String operador) CierreCaja
        +listar() ArrayList~CierreCaja~
    }
    GestorCaja "1" o-- "0..*" CierreCaja : cierres
    GestorCaja --> GestorSesiones : sesiones cobradas hoy
    GestorCaja --> GestorVentas : ventas de contado de hoy
    GestorCaja --> GestorTorneos : inscripciones de hoy
```

- `cerrarCaja` divide el dinero del día en los cuatro rubros del RF-12: **tiempo de juego** y **controles extra** (de las sesiones cobradas hoy), **barra** (ventas de contado más los consumos cargados a sesiones) e **inscripciones a torneos**.
- Las ventas con cargo a sesión no se suman aparte: ya vienen dentro de los consumos de su sesión, así nada se cuenta dos veces.
- `CierreCaja` es un registro de algo que ya pasó: todos sus atributos son `final`, la fecha la pone sola y el operador es su nombre (texto).

---

## 8. Persistencia (RNF-01)

```mermaid
classDiagram
    class IRepository~T~ {
        <<interface>>
        +getDataList() ArrayList~T~*
        +insertData(T) void*
        +updateData(T anterior, T nuevo) void*
        +deleteData(T) void*
        +getDataLine(T) String*
    }
    class FileManage {
        -String nameFile
        +FileManage(String nameFile)
        +getFile() File
        +getDataOfFile() ArrayList~String~
        +writeFile(String linea) void
        +updateFile(String anterior, String nueva) void
        +deleteFile(String linea) void
        +writeAllLinesInFile(ArrayList~String~) void
    }
    class ClienteRepository {
        -FileManage fileManage
        +guardarTodos(ArrayList~Cliente~) void
    }
    class PuestoRepository {
        -FileManage fileManage
        +guardarTodos(ArrayList~PuestoJuego~) void
    }
    class ProductoRepository {
        -FileManage fileManage
        +guardarTodos(ArrayList~Producto~) void
    }
    class SesionRepository {
        -FileManage fileManage
        -GestorClientes gestorClientes
        -GestorPuestos gestorPuestos
        -InventarioBarra inventarioBarra
        +SesionRepository(GestorClientes, GestorPuestos, InventarioBarra)
        +guardarTodos(ArrayList~SesionJuego~) void
        -escribirConsumos(ArrayList~ItemConsumo~) String
        -leerConsumos(String) ArrayList~ItemConsumo~
    }
    class VentaRepository {
        -FileManage fileManage
        -InventarioBarra inventarioBarra
        -GestorSesiones gestorSesiones
        +VentaRepository(InventarioBarra, GestorSesiones)
        +guardarTodos(ArrayList~Venta~) void
        -buscarSesion(int, LocalDateTime) SesionJuego
        -escribirItems(ArrayList~ItemConsumo~) String
        -leerItems(String) ArrayList~ItemConsumo~
    }
    class TorneoRepository {
        -FileManage archivoTorneos
        -FileManage archivoInscripciones
        -FileManage archivoPartidas
        -GestorClientes gestorClientes
        +TorneoRepository(GestorClientes)
        +guardarTodos(ArrayList~Torneo~) void
        -leerModalidad(String) ModalidadTorneo
        -buscarTorneo(ArrayList~Torneo~, String) Torneo
        -buscarCliente(String) Cliente
    }

    IRepository <|.. ClienteRepository
    IRepository <|.. PuestoRepository
    IRepository <|.. ProductoRepository
    IRepository <|.. SesionRepository
    IRepository <|.. VentaRepository
    IRepository <|.. TorneoRepository
    ClienteRepository --> FileManage : clientes.txt
    PuestoRepository --> FileManage : puestos.txt
    ProductoRepository --> FileManage : productos.txt
    SesionRepository --> FileManage : sesiones.txt
    VentaRepository --> FileManage : ventas.txt
    TorneoRepository --> "3" FileManage : torneos, inscripciones y partidas
    SesionRepository ..> GestorClientes
    SesionRepository ..> GestorPuestos
    SesionRepository ..> InventarioBarra
    VentaRepository ..> InventarioBarra
    VentaRepository ..> GestorSesiones
    TorneoRepository ..> GestorClientes
```

- Cada repositorio implementa los cinco métodos de `IRepository<T>` (`getDataList`, `insertData`, `updateData`, `deleteData`, `getDataLine`) para su entidad; en el diagrama solo se muestran los métodos propios de cada uno.
- `FileManage` escribe y lee los `.txt` de `src/files/` (crea la carpeta si no existe). Los campos van separados por `|`.
- Los objetos que contienen a otros se guardan por su identificador (documento, id de estación, código de producto) y al cargar se buscan en el gestor correspondiente; por eso algunos repositorios reciben gestores.
- `PuestoRepository` guarda cada estación con `aLinea()` (polimórfico: cada tipo arma su propia línea).

---

## 9. Interfaz de consola

```mermaid
classDiagram
    class Main {
        +main(String[] args) void$
    }
    class MenuPrincipal {
        -Scanner scanner
        -GestorClientes gestorClientes
        -GestorPuestos gestorPuestos
        -GestorSesiones gestorSesiones
        -InventarioBarra inventarioBarra
        -GestorVentas gestorVentas
        -GestorTorneos gestorTorneos
        +MenuPrincipal()
        +mostrar() void
        -cargarDatos() void
        -guardarDatos() void
        -crearSala() void
    }
    class MenuClientes {
        +mostrar() void
    }
    class MenuPuestos {
        +mostrar() void
    }
    class MenuSesiones {
        +mostrar() void
    }
    class MenuBarra {
        +mostrar() void
    }
    class MenuTorneos {
        -final DateTimeFormatter FORMATO_FECHA$
        +mostrar() void
    }
    class MenuCaja {
        +mostrar() void
    }
    class Demo {
        +ejecutarDemo() void$
    }
    Main ..> MenuPrincipal : arranca
    MenuPrincipal *-- MenuClientes
    MenuPrincipal *-- MenuPuestos
    MenuPrincipal *-- MenuSesiones
    MenuPrincipal *-- MenuBarra
    MenuPrincipal *-- MenuTorneos
    MenuPrincipal *-- MenuCaja
    MenuPrincipal ..> Demo : opción 7
    MenuPrincipal --> "6" IRepository : carga y guarda
    MenuClientes --> GestorClientes
    MenuPuestos --> GestorPuestos
    MenuSesiones --> GestorSesiones
    MenuBarra --> InventarioBarra
    MenuBarra --> GestorVentas
    MenuTorneos --> GestorTorneos
    MenuCaja --> GestorCaja
```

- Los menús **no tienen reglas de negocio**: piden los datos, llaman al gestor y muestran el mensaje de la excepción si algo se rechaza. Todos comparten el mismo `Scanner`.
- `MenuPrincipal` crea los gestores una sola vez, carga los archivos al arrancar y guarda todo cada vez que se vuelve de un submenú. Tiene además un atributo por cada repositorio (los seis de la sección 8).
- Los métodos privados de cada menú (una opción por método, más `leerEntero`, `leerDouble` y `seleccionarMetodoPago`) no se muestran.
- `Demo` ejecuta las pruebas de todos los requerimientos con sus propios gestores, sin tocar los datos guardados.
