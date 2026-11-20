# GG Hub — Sistema de gestión

## PARTE 1 — Descripción del programa

### 1. Contexto

El negocio que se va a automatizar es GG HUB, un establecimiento mediano que pertenece al sector de entretenimiento digital y de deportes electrónicos(e-sports).Cuenta con una infraestructura física dividida en tres áreas principales que son: Una zona de 15 estaciones de PC Gaming, 10 cabinas de consolas de última generación y una barra de venta de snacks y bebidas. Además se organizan torneos presenciales cada semana.

### 2. Actores

- Administrador de sala: Se encarga de la configuración del negocio. Define los precios tanto base como extras, el aforo de los torneos y se encarga de abastecer el inventario de la barra de snacks y bebidas.
- Operador: Es el recepcionista que da la atención directa al cliente. Asigna las estaciones de juego, lleva el conteo del tiempo, toma los pedidos de la barra y se encarga también de cobrar.
- Cliente: Es el usuario final que alquila el tiempo de uso en las estaciones, consume productos del inventario y se inscribe como competidor en los torneos.
- Árbitro (Para los torneos): Es el mediador durante los eventos competitivos. Su rol es organizar los cruces entre jugadores, registrar los resultados de cada partida y determinar quién avanza a la siguiente fase según la modalidad del torneo (eliminación directa o grupos).

### 3. Proceso actual

Actualmente, la operación del centro depende de métodos totalmente manuales. El operador registra a los clientes, asigna los equipos y anota los consumos de cafetería usando un cuaderno y notas adhesivas. Por esta razón, al finalizar una sesión, el cobro exige calcular a mano o con calculadora el tiempo exacto de uso, agregar el costo de controles adicionales y sumar los productos consumidos. De la misma manera, la logística de los torneos no cuenta con ningún tipo de sistematización; las llaves de juego se actualizan a mano en un tablero acrílico y el recaudo de inscripciones se maneja exclusivamente en efectivo en una caja separada.

### 4. Dolor

Este modelo de trabajo manual está provocando pérdidas económicas y problemas operativos significativos. Durante las jornadas donde hay una cantidad de clientes alta, es frecuente que se pierda el registro exacto del tiempo de uso o se extravíen las notas de la barra, lo que se traduce en servicios y productos entregados pero no facturados. Asimismo, se suele omitir el recargo por el préstamo de controles adicionales, causando constantes descuadres entre el inventario físico y el cierre de caja al final del día. Por otra parte, la gestión logística de los eventos resulta muy ineficiente, ya que la organización manual de los torneos genera retrasos prolongados en los horarios de inicio, afectando negativamente la experiencia y generando molestias entre los clientes.

### 5. Impacto

Automatizar este proceso transformará el negocio en tres aspectos importantes:

- Impacto Económico: Se frenarán las pérdidas de dinero al eliminar los errores de cálculo humano y las comandas extraviadas. El sistema garantizará que se cobre cada minuto exacto de uso y aplicará automáticamente las reglas de negocio sobre tarifas.
- Impacto Técnico: se centralizará el manejo de la información. Los antiguos registros en papel serán sustituidos por archivos digitales que aseguren la persistencia de los datos. De esta manera, se logrará automatizar el control de inventario, descontando en tiempo real el stock con cada producto vendido. Igualmente, esta sistematización garantizará un monitoreo exacto y constante del estado de cada equipo, permitiendo saber al instante si una estación está disponible, en uso o en mantenimiento.
- Impacto Social: Se reducirá drásticamente el estrés del Operador de Turno, quien ya no tendrá que hacer matemáticas bajo presión. Para los Jugadores, la experiencia mejorará radicalmente, ya que la automatización de los torneos permitirá generar los cruces de forma instantánea y justa, eliminando los tiempos de espera y problemas logísticos.




## PARTE 2 — Requerimientos

| ID | Descripción | Actor | Prioridad | Criterio de aceptación |
| :--- | :--- | :--- | :--- | :--- |
| RF-01 | El sistema debe permitir al operador registrar y consultar clientes indicando nombre, documento de identidad, teléfono y correo electrónico. | Operador | Alta | El sistema valida que el campo de documento contenga únicamente números y rechaza el registro si encuentra un documento registrado previamente. |
| RF-02 | El sistema debe permitir al administrador registrar las estaciones de juego diferenciando si es PC o consola con: identificador numérico, modelo y tarifa base por hora. | Administrador | Alta | El sistema rechaza el registro si el identificador ya existe y valida que la tarifa base por hora sea un número estrictamente mayor a cero. |
| RF-03 | El sistema debe permitir al operador consultar la disponibilidad y el estado en tiempo real de todas las estaciones de juego, las 15 de PC y las 10 de consola. | Operador | Media | El sistema muestra una lista con las 25 estaciones especificando su tipo e indica su estado actual (Disponible, En uso, o En mantenimiento) |
| RF-04 | El sistema debe permitir al operador registrar el inicio de sesión de un juego a un cliente en específico en una estación determinada, especificando controles, periféricos adicionales entregados. | Operador | Alta | El sistema bloquea el inicio de sesión del juego si la estación se encuentra en estado "En uso" o "En mantenimiento". |
| RF-05 | El sistema debe permitir al operador finalizar la sesión de juego de una estación, calcular el costo total de la cuenta y liberar el equipo. | Operador | Alta | El sistema calcula el cobro total sumando: (tiempo transcurrido * tarifa base) + (tarifa de controles extra * cantidad) + consumos de alimentos; al procesar el pago libera la estación cambiando su estado a "Disponible" |
| RF-06 | El sistema debe permitir al administrador registrar y actualizar los productos consumibles disponibles indicando código único, nombre, categoría, precio de venta costo y stock disponible. | Administrador | Alta | El sistema rechaza el registro si el código del producto está duplicado y no permite ingresar valores negativos en el precio de venta ni en la cantidad de stock. |
| RF-07 | El sistema debe permitir al operador registrar la venta de productos de la barra, cargándolos a la cuenta de cobro de la sesión activa del cliente o procesándolos como venta directa de contado. | Operador | Media | El sistema impide la venta si la cantidad requerida supera la cantidad en stock disponible en el inventario y descuenta automáticamente las unidades vendidas en tiempo real tras la confirmación de la venta. |
| RF-08 | El sistema debe permitir al administrador configurar torneos presenciales indicando nombre del evento, videojuego,fecha, costo de inscripción, aforo máximo y modalidad (eliminación directa o fase de grupos). | Administrador | Alta | El sistema no permite programar un torneo con una fecha o hora anterior a la del sistema, ni registrar un aforo máximo inferior a 2 participantes |
| RF-09 | El sistema debe permitir al operador registrar la inscripción de un cliente a un torneo activo registrando el cobro respectivo. | Operador | Alta | El sistema bloquea la inscripción si el cliente ya está registrado en ese mismo torneo o si la cantidad de participantes ya alcanzo el limite máximo |
| RF-10 | El sistema debe permitir al árbitro generar los emparejamientos y llaves de la competencia del torneo una vez que se cerrada la etapa de inscripción. | Arbitro | Media | El sistema genera los cruces emparejando únicamente a los clientes que se encuentran inscritos y aplicando las reglas de la modalidad seleccionada (eliminación directa o grupos). |
| RF-11 | El sistema debe permitir al árbitro registrar los resultados y marcadores de cada partida en las llaves del torneo. | Arbitro | Media | El sistema avanza automáticamente al ganador a la siguiente ronda de la llave y actualiza la tabla de posiciones si la competencia sigue en la instancia de fase de grupos. |
| RF-12 | El sistema debe permitir al operador realizar el cierre de caja de su turno, generando un informe consolidado de recaudos y de eventos en el dia. | Operador | Media | El sistema calcula y divide el dinero total recaudado apartado por tiempo de juego, recargos de controles extras, ventas de barra e inscripciones a torneos. |
| RNF-01 | Todos los datos gestionados por la aplicación (clientes,estaciones de juego, catálogo de inventario, consumos, sesiones activas/cerradas y torneos con sus llaves) deben persistir en almacenamiento permanente | Todos | Alta | Toda la información registrada se conserva componiendose tras cerrar y reiniciar la aplicación, recuperando el estado de la sala, los saldos y el inventario sin perder los datos. |

## Próximos pasos

### 1. Conformación del equipo

  Tomas Gomez — Responsable de Producto (PO): Se encarga de que el código no se desvíe del problema original. Reviso que cada clase y método que hagamos realmente le sirva a la lógica del GG Hub y no sea código "de relleno".
   
Baioleth Rojas y Juan Diego Cuervo — Responsable de Diseño (AR): Lidera el diagrama de clases en UML y es el "policía" del polimorfismo y la herencia. Su trabajo es evitar que hagamos clases gigantes que hagan de todo ("God classes").

Daniel Bernate — Responsable de Calidad (QA): Define cómo vamos a probar el código. Se encarga de meter datos erróneos (como textos donde van números o aforos impares) para verificar que el sistema no se rompa.

Marco — Responsable de Integración (GI): Es el dueño del repositorio. Nos organiza las ramas de Git, vigila que nadie suba código que no compila y revisa que todos tengamos commits parejos.


### 2. Repositorio

Enlace: https://github.com/gmezosp7-dev/gghub-system.git

Ya creamos el repositorio, invitamos al docente como colaborador, y nos aseguramos de que haya al menos un commit inicial de cada uno de los 5 integrantes para validar que todos tenemos el entorno de desarrollo bien configurado.

### 3. Plan hacia la Entrega 2
Para la segunda entrega nos vamos a concentrar en la lógica "cruda" del negocio (sin interfaz gráfica todavía) y en lograr que los datos se guarden en archivos. Planteamos nuestra matriz de trazabilidad inicial abordando todos los frentes de trabajo definidos en nuestro catálogo:

| Requerimiento(s) | Descripción y Clases previstas | Temas del curso | Estado |
| :--- | :--- | :--- | :--- |
| **RF-01** (Gestión de Clientes) | Para manejar el registro de los usuarios y validar que no existan documentos duplicados, implementaremos la clase `Cliente` y su controlador `GestorClientes`. | Clases, objetos y encapsulamiento estricto. | Terminado |
| **RF-02, RF-03, RF-04, RF-05** (Puestos, Sesiones y Cobros) | Para resolver la configuración de máquinas, inicio de sesión con controles extra y cálculo de tarifas, usaremos la clase abstracta `PuestoJuego` y sus hijas `EstacionPC` y `EstacionConsola`, conectadas a `SesionJuego` y al enum `EstadoPuesto`. | Herencia, clases abstractas, polimorfismo por sobreescritura (evitando condicionales por tipo), enums, comunicación entre objetos. | Terminado |
| **RF-06, RF-07** (Inventario y Consumos) | Para sumar productos a una cuenta activa o vender de contado descontando stock, diseñaremos las clases `Producto`, `ItemConsumo` e `InventarioBarra`. | Comunicación entre objetos, colecciones (`ArrayList` para catálogo y pedidos). | Terminado |
| **RF-08, RF-09, RF-10, RF-11** (Gestión de Torneos) | Para organizar competencias, registrar participantes y definir ganadores, crearemos la clase `Torneo` vinculada a la clase abstracta `ModalidadTorneo` y sus subclases `EliminacionDirecta` y `FaseGrupos`. | Herencia, polimorfismo puro (para generar cruces y avanzar rondas sin usar *switch*), composición y colecciones. | Terminado |
| **RF-12** (Cierre de Caja Diario) | Para consolidar los ingresos del día discriminando tiempo de juego, cafetería e inscripciones, diseñaremos la clase `CierreCaja` que sumarizará las sesiones cerradas. | Clases, objetos, métodos de cálculo y agregación de datos. | Terminado |
| **RNF-01** (Persistencia) | Para garantizar que el estado de máquinas, inventario y torneos sobreviva al cierre, se implementa la interfaz genérica `IRepository<T>`, la clase `FileManage` (lectura y escritura de archivos `.txt` en `src/files/`) y un repositorio por entidad (`ClienteRepository`, `PuestoRepository`, `ProductoRepository`, `SesionRepository`, `VentaRepository`, `TorneoRepository`). | Archivos planos desacoplados de la lógica (operaciones de guardado y carga), interfaces y genéricos. | Terminado |

### 4. Registro de Decisiones de Diseño POO (GG Hub)

**Decisión 1: Estructura de los puestos de juego**

Decisión: Armamos la clase PuestoJuego como abstracta para que de ahí se cuelguen las computadoras y las consolas, dejando un método obligatorio para calcular lo que debe pagar cada cliente.

Alternativas consideradas: Hacer una sola clase general con un atributo de texto o usando condicionales if/else y switch para diferenciar los equipos.

Por qué: Las consolas cobran el tiempo más un recargo por cada control adicional, mientras que las computadoras cobran solo por el tiempo de uso. Si resolviéramos eso con puros condicionales, romperíamos el Principio Abierto/Cerrado y nos llenaríamos de validaciones feas que la universidad nos va a castigar.

Consecuencia: Nos ahorra dolores de cabeza a futuro al permitirnos añadir nuevos equipos fácilmente sin dañar la base del programa.

**Decisión 2: Estructura de las modalidades de torneo**

Decisión: Creamos una clase abstracta llamada ModalidadTorneo para que de ella salgan las opciones de eliminación directa y fase de grupos, encargándose cada una de armar los cruces y pasar a los ganadores.

Alternativas consideradas: Tirar toda la lógica de los campeonatos dentro de la misma clase Torneo usando variables booleanas para prender o apagar funciones.

Por qué: Una fase de grupos necesita sacar cuentas de puntos, empates y sets, mientras que la eliminación directa bota al que pierde de una vez. Juntar todo eso en una sola clase crearía un monstruo gigante ("God Class") imposible de mantener y lleno de errores.

Consecuencia: Mantiene el código limpio y ordenado, haciendo que cualquier cambio o adición posterior sea mucho más rápido y sencillo.

**Decisión 3: Selección y delimitación del dominio del proyecto**

Decisión: Nos decidimos de una vez por todas a desarrollar el sistema para el centro de entretenimiento GG Hub, descartando por completo las ideas de hacer un gimnasio o un conjunto residencial.

Alternativas consideradas: Programar un software de administración para un gimnasio o un sistema de control de pagos para un conjunto residencial.

Por qué: El GG Hub nos pone un reto muchísimo más chévere y real, porque exige controlar las computadoras al tiempo, manejar el inventario de la barra en vivo y calcular las llaves de los torneos, lo que justifica perfectamente el uso de herencia, abstracción y estructuras de datos más avanzadas.

Consecuencia: Facilita el desarrollo general del proyecto y nos ayuda a cumplir sin líos con los requisitos de la materia.

### 5. Bitácora de uso de IA

**B (Bitácora):**
Solicitamos a Gemini una lluvia de ideas inicial para explorar alternativas de proyectos, planteando opciones como un sistema para gimnasio o para un conjunto residencial, y posteriormente pedimos orientación para delimitar las entidades y clases principales de nuestro centro de entretenimiento. Como respuesta, obtuvimos un listado general de propuestas comerciales y un esquema preliminar compuesto por clases básicas como cliente, empleado, estación, torneo y producto.

**C (Crítica):**
Evaluamos las alternativas iniciales y decidimos descartar el gimnasio y el conjunto residencial porque presentaban una lógica y una complejidad técnica muy reducidas. Al revisar el esquema de clases propuesto por Gemini, rechazamos un enfoque procedimental basado en múltiples condicionales o atributos de texto plano, reconociendo la necesidad de implementar una arquitectura orientada a objetos más robusta.

## ENTREGA #2 — Decisiones de diseño (Diagrama UML)

### 1. Clases
   El dominio se descompuso en entidades independientes como Cliente, PuestoJuego, SesionJuego, Torneo, Producto y CierreCaja. Se aplicó el principio de responsabilidad única para asegurar que cada clase administre exclusivamente sus propios datos y funciones. Esto evita la creación de clases gigantes que intenten hacer todo en el sistema, permitiendo que el código sea limpio, fácil de entender y mantenible a medida que el proyecto crece.

### 2. Atributos y métodos estáticos
   Se implementaron en la clase ValidadorDatos para métodos de comprobación de formato (documentoValido, precioValido), y en clases de negocio para definir constantes de estado inmutables (como DISPONIBLE, EN_USO y EN_MANTENIMIENTO en PuestoJuego, y CONTADO o CARGO_SESION en Venta). Los miembros estáticos pertenecen a la clase en general y no a un objeto particular. Su utilidad radica en proveer herramientas de validación global y constantes estandarizadas sin necesidad de instanciar objetos repetidamente, lo cual optimiza el uso de memoria y evita duplicar código en las validaciones.


### 3. Clases anidadas
   Se incorporó la clase anidada estática DetalleCargo dentro de la clase contenedora SesionJuego. Esta estructura se utilizó para agrupar datos auxiliares (concepto, cantidad, precio unitario) que solo tienen sentido dentro del contexto de una sesión de juego. Al anidarla, se logra un mejor encapsulamiento, manteniendo el código ordenado y evitando crear clases sueltas en el paquete que solo sirven como apoyo a una entidad principal.

### 4. Interfaces
   Se definieron las interfaces CalculableCobro (para el cálculo de tarifas) y RepositorioArchivo (para estandarizar las operaciones de lectura y escritura en los archivos de texto). Las interfaces actúan como contratos obligatorios que indican a las clases qué métodos deben implementar sin dictar la forma en que deben hacerlo. Esto desacopla el diseño: permite que las clases de negocio trabajen con reglas generales en lugar de depender directamente de implementaciones concretas, facilitando la adición de nuevas funciones sin alterar el resto de la aplicación.


### 5. Polimorfismo
   Se aplicó en tres áreas clave:
   1. En las estaciones de juego: El método calcularCostoSesion() responde de manera distinta en EstacionPC (aplica tarifa plana por tiempo) y en EstacionConsola (calcula el tiempo sumando automáticamente el recargo por controles adicionales).
   2. En la gestión de torneos: A través de la clase abstracta ModalidadTorneo, las subclases EliminacionDirecta y FaseGrupos sobrescriben generarCruces() y procesarResultado() con sus propias reglas de juego.
   3. En la persistencia: Las seis clases de almacenamiento (ArchivoClientes, ArchivoPuestos, ArchivoProductos, ArchivoTorneos, ArchivoSesiones y ArchivoVentas) implementan de forma polimórfica las operaciones básicas de almacenamiento y lectura de datos (guardar, listar, buscar y eliminar) definidas en la interfaz RepositorioArchivo.
  
   El polimorfismo evita el uso de condicionales múltiples (if-else o switch) para determinar con qué tipo de objeto se está trabajando. Esto garantiza que el sistema sea abierto a la extensión y cerrado a la modificación, permitiendo incluir nuevos tipos de estaciones o modalidades de torneos en el futuro sin modificar la lógica existente.

## ESTADO DE LA IMPLEMENTACIÓN

Los 12 requerimientos funcionales y el RNF-01 están implementados. El diagrama de clases actualizado, sacado del código final, está en **[docs/UML.md](docs/UML.md)**.

### Cómo ejecutar
Requiere **Java 21**.
- **IntelliJ IDEA:** abrir la carpeta `GGHUB/` como proyecto, marcar `src` como *Sources Root* (si no lo hace solo) y ejecutar `Main`.
- **Consola**, desde la carpeta `GGHUB/`:
  ```
  javac -d out src/Main.java src/enums/*.java src/interfaces/*.java src/modelos/*.java src/servicios/*.java src/persistencia/*.java src/repositorios/*.java src/ui/*.java
  java -cp out Main
  ```
  Hay que ejecutarlo **desde `GGHUB/`**: los datos se guardan en `src/files/`, una ruta relativa a la carpeta desde donde se corre el programa.

### Cómo se usa
`Main` abre el **menú principal** de consola:

| Opción | Menú | Requerimientos |
| :--- | :--- | :--- |
| 1 | Clientes: registrar, buscar por documento, listar | RF-01 |
| 2 | Estaciones: registrar PC o consola, ver el estado de las 25, buscar, poner y quitar mantenimiento | RF-02, RF-03 |
| 3 | Sesiones: iniciar (con controles extra), ver la sesión activa de una estación, finalizar y cobrar | RF-04, RF-05 |
| 4 | Barra: registrar productos, actualizar precio, reabastecer, venta de contado, venta cargada a una sesión | RF-06, RF-07 |
| 5 | Torneos: crear, inscribir (con pago), cerrar inscripciones, generar cruces, registrar resultados, ver posiciones | RF-08 a RF-11 |
| 6 | Cierre de caja del día | RF-12 |
| 7 | Demo: ejecuta todas las pruebas automáticas (casos válidos e inválidos de cada requerimiento) sin tocar los datos guardados | Todos |

Cada pago (sesión, venta de contado, inscripción) se hace con **efectivo** (calcula el cambio), **tarjeta de crédito** (últimos 4 dígitos y cuotas) o **pago digital** (Nequi, Daviplata...).

### Persistencia (RNF-01)
- Al arrancar, `MenuPrincipal` **carga** los archivos de `src/files/` y llena los gestores. La primera vez, si no hay estaciones guardadas, crea la sala: PCs del 1 al 15 y consolas del 16 al 25.
- Cada vez que se vuelve de un submenú, **guarda** todo, así no se pierde nada si se cierra el programa.
- Un archivo por entidad, con los campos separados por `|`: `clientes.txt`, `puestos.txt`, `productos.txt`, `sesiones.txt` (con sus consumos), `ventas.txt` (con sus productos), `torneos.txt`, `inscripciones.txt` y `partidas.txt`.
- Los objetos que contienen a otros se guardan por su identificador (el documento del cliente, el id de la estación, el código del producto) y al cargar se buscan en el gestor correspondiente.
- Las clases que al crearse ponen valores "de ahora" (la hora de inicio de la sesión, el estado `DISPONIBLE` de una estación, el precio actual del producto...) tienen un **segundo constructor** para recuperar los valores guardados (sobrecarga).
- Los **cierres de caja** se calculan y se muestran, pero no se guardan en archivo.

### Estructura de paquetes
| Paquete | Contenido |
| :--- | :--- |
| `modelos` | Entidades: `Cliente`, `PuestoJuego` (abstracta), `EstacionPC`, `EstacionConsola`, `SesionJuego`, `Producto`, `ItemConsumo`, `Venta`, `Torneo`, `Inscripcion`, `ModalidadTorneo` (abstracta), `EliminacionDirecta`, `FaseGrupos`, `Partida`, `PosicionGrupo`, `CierreCaja`, y los pagos `Efectivo`, `TarjetaCredito`, `PagoDigital` |
| `servicios` | Gestores con las reglas de negocio: `GestorClientes`, `GestorPuestos`, `GestorSesiones`, `InventarioBarra`, `GestorVentas`, `GestorTorneos`, `GestorCaja`, y `ValidadorDatos` (métodos estáticos) |
| `interfaces` | `CalculableCobro`, `MetodoPago`, `IRepository<T>` |
| `enums` | `EstadoPuesto` (DISPONIBLE, EN_USO, EN_MANTENIMIENTO), `TipoVenta` (CONTADO, CARGO_SESION) |
| `persistencia` | `FileManage` (lectura y escritura de archivos `.txt`) |
| `repositorios` | `ClienteRepository`, `PuestoRepository`, `ProductoRepository`, `SesionRepository`, `VentaRepository`, `TorneoRepository` (este último también guarda inscripciones y partidas) |
| `ui` | `MenuPrincipal`, `MenuClientes`, `MenuPuestos`, `MenuSesiones`, `MenuBarra`, `MenuTorneos`, `MenuCaja` y `Demo` |

### Conceptos de POO en el código
| Concepto | Dónde |
| :--- | :--- |
| Clases abstractas y herencia | `PuestoJuego` → `EstacionPC`, `EstacionConsola`; `ModalidadTorneo` → `EliminacionDirecta`, `FaseGrupos` |
| Interfaces | `CalculableCobro` (la implementa `PuestoJuego`), `MetodoPago` (`Efectivo`, `TarjetaCredito`, `PagoDigital`), `IRepository<T>` (los 6 repositorios) |
| Polimorfismo | `calcularCostoSesion()` cobra distinto en PC y consola; `validar()` y `aLinea()` se sobreescriben por tipo de estación; `generarCruces()`, `procesarResultado()` y `obtenerPosiciones()` cambian por modalidad; `pagar()` cambia por método de pago; `toString()` en todas las entidades |
| Encapsulamiento | atributos `private`; identificadores y hechos ya ocurridos con `final` y sin setter; `listar()` devuelve copias; el estado solo cambia por métodos con reglas (`cambiarEstado`, `cerrarInscripciones`, `descontarStock`...) |
| Miembros estáticos | `ValidadorDatos` (`PATRON_DOCUMENTO`, `documentoValido`, `precioValido`, `textoValido`, `validadorNegativo`); `PosicionGrupo.PUNTOS_POR_VICTORIA` |
| Enums | `EstadoPuesto`, `TipoVenta` |
| Sobrecarga | `GestorVentas.confirmar(venta)` y `confirmar(venta, metodo)`; segundos constructores para cargar desde archivo |
| Composición y asociación | `SesionJuego` tiene un `Cliente`, un `PuestoJuego` y sus consumos; `Venta` tiene sus `ItemConsumo`; `Torneo` tiene sus inscripciones y partidas; los gestores reciben otros gestores por constructor |
| Colecciones y genéricos | `ArrayList<T>` en todos los gestores; `IRepository<T>` |
| Excepciones | las reglas de negocio lanzan `IllegalArgumentException` con un mensaje claro, y los menús las atrapan |

### Cambios respecto al diseño de la Entrega 2
El código final se aparta del diagrama de la Entrega 2 en estos puntos (el diagrama actualizado está en [docs/UML.md](docs/UML.md)):
- **Colecciones y enums:** el profesor autorizó `ArrayList`, genéricos y `enum`. Los estados de las estaciones y los tipos de venta son **enums** (`EstadoPuesto`, `TipoVenta`), no constantes `String`; las listas son `ArrayList`, no arreglos.
- **Gestores:** se agregaron `GestorClientes`, `GestorPuestos`, `GestorSesiones`, `GestorVentas`, `GestorTorneos` y `GestorCaja` para que cada entidad tenga quien guarde su lista y aplique sus reglas (responsabilidad única).
- **Pagos:** se agregó la interfaz `MetodoPago` (`pagar`, `obtenerComprobante`) con `Efectivo`, `TarjetaCredito` y `PagoDigital`. La usan la venta de contado, el cierre de la sesión y la inscripción a torneos. Se cobra **antes** de cambiar cualquier dato: si el pago falla, no se descuenta stock, no se libera la estación ni se inscribe al cliente.
- **Cargos de la sesión:** en lugar de la clase anidada `DetalleCargo`, los consumos de la barra cargados a una sesión se guardan como `ItemConsumo`, que copia el precio del momento de la venta.
- **Persistencia:** en lugar de `RepositorioArchivo` y las seis clases `ArchivoX`, se usa la interfaz genérica `IRepository<T>`, `FileManage` y un repositorio por entidad (`XRepository`), como en el proyecto de referencia del curso.
- **Usuarios:** `UsuarioSistema`, `Operador`, `AdministradorSala` y `Arbitro` no se implementaron como clases; el cierre de caja registra el nombre del operador como texto.
- **Torneos:** el torneo se identifica por su nombre; `Partida` guarda a los dos jugadores (si el número de inscritos es impar, uno pasa directo); la fase de grupos es un solo grupo de todos contra todos, con 3 puntos por victoria y sin empates; la tabla de posiciones usa `PosicionGrupo` en lugar de los arreglos `participantes[]` y `puntos[]`.
- **Cierre de caja:** separa el dinero en tiempo de juego, controles extra, barra (ventas de contado más consumos cargados a sesiones, sin contarlos dos veces) e inscripciones a torneos.
- **Menús:** se agregó la capa `ui` con un menú de consola por área y una `Demo` con las pruebas.

### Bitácora de uso de IA (implementación)

**B (Bitácora):**
Durante la implementación usamos Claude Code (Anthropic) como tutor y revisor. Para cada requerimiento le pedimos que describiera en español qué debía hacer cada clase y método (qué recibe, qué devuelve y sus pasos), y nosotros lo transcribimos a Java. Después le pedimos que leyera el código, lo compilara y lo probara con casos válidos e inválidos en una carpeta aparte, y que explicara cada error según los pilares de POO. También lo usamos para unir las ramas del equipo, generar el diagrama de clases final (`docs/UML.md`) a partir del código real, actualizar este README, preparar la presentación y el guion de la sustentación, y probar la carga de archivos `.txt` con datos mal escritos.

**C (Crítica):**
No aceptamos todo lo que propuso: no implementamos la clase anidada que sugirió para `PosicionGrupo`. Varias de sus primeras respuestas generaban clases completas; le pedimos trabajar paso a paso para entender cada decisión antes de escribirla. Todo lo que produjo lo verificamos compilando y ejecutando el programa, y cuando el UML original no coincidía con el código dejamos que mandara el código.
