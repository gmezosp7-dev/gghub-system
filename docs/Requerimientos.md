PARTE 1 Descripcion del programa

1. CONTEXTO

El negocio que se va a automatizar es Nexus Gaming Arena, un establecimientomediano que pertenece al sector de entretenimiento digital y de deportes electrónicos(e-sports).Cuenta con una infraestructura física dividida en tres áreas principales que son: Una zona de 15 estaciones de PC Gaming, 10 cabinas de consolas de última generación y una barra de venta de snacks y bebidas. Además se organizan torneos presenciales cada semana.

2. ACTORES

- Administrador de sala: Se encarga de la configuración del negocio. Define los precios tanto base como extras, el aforo de los torneos y se encarga de abastecer el inventario de la barra de snacks y bebidas.
- Operador: Es el recepcionista que da la atención directa al cliente. Asigna las estaciones de juego, lleva el conteo del tiempo, toma los pedidos de la barra y se encarga también de cobrar.
- Cliente: Es el usuario final que alquila el tiempo de uso en las estaciones, consume productos del inventario y se inscribe como competidor en los torneos.
- Árbitro (Para los torneos): Es el mediador durante los eventos competitivos. Su rol es organizar los cruces entre jugadores, registrar los resultados de cada partida y determinar quién avanza a la siguiente fase según la modalidad del torneo (eliminación directa o grupos).

3. PROCESO ACTUAL:

Actualmente, la operación del centro depende de métodos totalmente manuales. El operador registra a los clientes, asigna los equipos y anota los consumos de cafetería usando un cuaderno y notas adhesivas. Por esta razón, al finalizar una sesión, el cobro exige calcular a mano o con calculadora el tiempo exacto de uso, agregar el costo de controles adicionales y sumar los productos consumidos. De la misma manera, la logística de los torneos no cuenta con ningún tipo de sistematización; las llaves de juego se actualizan a mano en un tablero acrílico y el recaudo de inscripciones se maneja exclusivamente en efectivo en una caja separada.

4. DOLOR

Este modelo de trabajo manual está provocando pérdidas económicas y problemas operativos significativos. Durante las jornadas donde hay una cantidad de clientes alta, es frecuente que se pierda el registro exacto del tiempo de uso o se extravíen las notas de la barra, lo que se traduce en servicios y productos entregados pero no facturados. Asimismo, se suele omitir el recargo por el préstamo de controles adicionales, causando constantes descuadres entre el inventario físico y el cierre de caja al final del día. Por otra parte, la gestión logística de los eventos resulta muy ineficiente, ya que la organización manual de los torneos genera retrasos prolongados en los horarios de inicio, afectando negativamente la experiencia y generando molestias entre los clientes.

5. IMPACTO

Automatizar este proceso transformará el negocio en tres aspectos importantes:

- Impacto Económico: Se frenarán las pérdidas de dinero al eliminar los errores de cálculo humano y las comandas extraviadas. El sistema garantizará que se cobre cada minuto exacto de uso y aplicará automáticamente las reglas de negocio sobre tarifas.
- Impacto Técnico: se centralizará el manejo de la información. Los antiguos registros en papel serán sustituidos por archivos digitales que aseguren la persistencia de los datos. De esta manera, se logrará automatizar el control de inventario, descontando en tiempo real el stock con cada producto vendido. Igualmente, esta sistematización garantizará un monitoreo exacto y constante del estado de cada equipo, permitiendo saber al instante si una estación está disponible, en uso o en mantenimiento.
- Impacto Social: Se reducirá drásticamente el estrés del Operador de Turno, quien ya no tendrá que hacer matemáticas bajo presión. Para los Jugadores, la experiencia mejorará radicalmente, ya que la automatización de los torneos permitirá generar los cruces de forma instantánea y justa, eliminando los tiempos de espera y problemas logísticos.




PARTE 2 Requerimientos

ID	                Descripción	                        Actor	     Prioridad	                   Criterio de aceptación

RF-01    El sistema debe permitir al operador registrar y          Operador             Alta        El sistema valida que el campo de documento contenga 
         consultar clientes indicando nombre, documento de                                          únicamente números y rechaza el registro si encuentra un
         identidad, teléfono y correo electrónico.                                                  documento registrado previamente.

RF-02    El sistema debe permitir al administrador registrar       Administrador        Alta        El sistema rechaza el registro si el identificador ya existe
         las estaciones de juego diferenciando si es PC o                                           y valida que la tarifa base por hora sea un número
         consola con: identificador numérico, modelo y tarifa                                       estrictamente mayor a cero.
         base por hora. 
         
RF-03    El sistema debe permitir al operador consultar la         Operador             Media        El sistema muestra una lista con las 25 estaciones
         disponibilidad y el estado en tiempo real de todas                                         especificando su tipo e indica su estado actual (Disponible, 
         las estaciones de juego, las 15 de PC y las 10 de                                          En uso, o En mantenimiento) 
         consola.
         
RF-04    El sistema debe permitir al operador registrar el         Operador             Alta        El sistema bloquea el inicio de sesión del juego si la estación
         inicio de sesión de un juego a un cliente en específico                                    se encuentra en estado "En uso" o "En mantenimiento".
         en una estación determinada, especificando controles, 
         periféricos adicionales entregados. 

RF-05    El sistema debe permitir al operador finalizar la         Operador             Alta        El sistema calcula el cobro total sumando: 
         sesión de juego de una estación, calcular el costo                                         (tiempo transcurrido * tarifa base) + (tarifa de controles
         total de la cuenta y liberar el equipo.                                                    extra * cantidad) + consumos de alimentos; al procesar el pago
                                                                                                    libera la estación cambiando su estado a "Disponible"

RF-06    El sistema debe permitir al administrador registrar       Administrador        Alta        El sistema rechaza el registro si el código del producto 
         y actualizar los productos consumibles disponibles                                         está duplicado y no permite ingresar valores negativos en el  
         indicando código único, nombre, categoría, precio de venta                                 precio de venta ni en la cantidad de stock.
         costo y stock disponible. 

RF-07    El sistema debe permitir al operador registrar la venta   Operador             Media       El sistema impide la venta si la cantidad requerida supera la 
         de productos de la barra, cargándolos a la cuenta de                                       cantidad en stock disponible en el inventario y descuenta 
         cobro de la sesión activa del cliente o procesándolos                                      automáticamente las unidades vendidas en tiempo real tras la 
         como venta directa de contado.                                                             confirmación de la venta.

RF-08    El sistema debe permitir al administrador configurar      Administrador        Alta        El sistema no permite programar un torneo con una fecha o hora 
         torneos presenciales indicando nombre del evento,                                          anterior a la del sistema, ni registrar un aforo máximo inferior
         videojuego,fecha, costo de inscripción,                                                    a 2 participantes
         aforo máximo y modalidad (eliminación directa o
         fase de grupos).

RF-09    El sistema debe permitir al operador registrar la         Operador             Alta       El sistema bloquea la inscripción si el cliente ya está registrado en 
         inscripción de un cliente a un torneo activo                                              ese mismo torneo o si la cantidad de participantes ya alcanzo el limite máximo 
         registrando el cobro respectivo.                                                          

RF-10    El sistema debe permitir al árbitro generar los           Arbitro              Media       El sistema genera los cruces emparejando únicamente a los clientes que se encuentran
         emparejamientos y llaves de la competencia del torneo                                     inscritos  y aplicando las reglas de la modalidad seleccionada
         una vez que se cerrada la etapa de inscripción.                                           (eliminación directa o grupos).

RF-11    El sistema debe permitir al árbitro registrar los         Arbitro              Media       El sistema avanza automáticamente al ganador a la siguiente ronda de la llave y 
         resultados y marcadores de cada partida en las                                            actualiza la tabla de posiciones si la competencia sigue en la instancia de fase de grupos.
         llaves del torneo.
         
RF-12    El sistema debe permitir al operador realizar             Operador             Media      El sistema calcula y divide el dinero total recaudado apartado por tiempo de juego,
         el cierre de caja de su turno, generando un informe                                       recargos de controles extras, ventas de barra e inscripciones a torneos.
         consolidado de recaudos y de eventos en el dia.

RNF-01   Todos los datos gestionados por la aplicación 
         (clientes,estaciones de juego, catálogo de inventario,    Todos                Alta       Toda la información registrada se conserva componiendose tras cerrar y reiniciar la aplicación,  
         consumos, sesiones activas/cerradas                                                       recuperando el estado de la sala, los saldos y el inventario sin perder los datos. 
         y torneos con sus llaves) deben persistir                        
         en almacenamiento permanente.
       
 Próximos pasos
 
1. Conformación del equipo

  Tomas Gomez — Responsable de Producto (PO): Me encargo de que el código no se desvíe del problema original. Reviso que cada clase y método que hagamos realmente le sirva a la lógica del Nexus Gaming Arena y no sea código "de relleno".
   
Baioleth Rojas y Juan Diego Cuervo — Responsable de Diseño (AR): Lidera el diagrama de clases en UML y es el "policía" del polimorfismo y la herencia. Su trabajo es evitar que hagamos clases gigantes que hagan de todo ("God classes").

Daniel Bernate — Responsable de Calidad (QA): Define cómo vamos a probar el código. Se encarga de meter datos erróneos (como textos donde van números o aforos impares) para verificar que el sistema no se rompa.

Marco — Responsable de Integración (GI): Es el dueño del repositorio. Nos organiza las ramas de Git, vigila que nadie suba código que no compila y revisa que todos tengamos commits parejos.


2. Repositorio
   
Enlace: https://github.com/gmezosp7-dev/gghub-system.git

Ya creamos el repositorio, invitamos al docente como colaborador, y nos aseguramos de que haya al menos un commit inicial de cada uno de los 5 integrantes para validar que todos tenemos el entorno de desarrollo bien configurado.

3. Plan hacia la Entrega 2
   Para la segunda entrega nos vamos a concentrar en la lógica "cruda" del negocio (sin interfaz gráfica todavía) y en lograr que los datos se guarden en archivos. Planteamos nuestra matriz de trazabilidad inicial abordando todos los frentes de trabajo definidos en nuestro catálogo:
   
### 3. Plan hacia la Entrega 2
Para la segunda entrega nos vamos a concentrar en la lógica "cruda" del negocio (sin interfaz gráfica todavía) y en lograr que los datos se guarden en archivos. Planteamos nuestra matriz de trazabilidad inicial abordando todos los frentes de trabajo definidos en nuestro catálogo:

| Requerimiento(s) | Descripción y Clases previstas | Temas del curso | Estado |
| :--- | :--- | :--- | :--- |
| **RF-01** (Gestión de Clientes) | Para manejar el registro de los usuarios y validar que no existan documentos duplicados, implementaremos la clase `Cliente` y su controlador `GestorClientes`. | Clases, objetos y encapsulamiento estricto. | Por iniciar |
| **RF-02, RF-03, RF-04, RF-05** (Puestos, Sesiones y Cobros) | Para resolver la configuración de máquinas, inicio de sesión con controles extra y cálculo de tarifas, usaremos la clase abstracta `PuestoJuego` y sus hijas `EstacionPC` y `EstacionConsola`, conectadas a `SesionJuego` y al enum `EstadoPuesto`. | Herencia, clases abstractas, polimorfismo por sobreescritura (evitando condicionales por tipo), enums, comunicación entre objetos. | Por iniciar |
| **RF-06, RF-07** (Inventario y Consumos) | Para sumar productos a una cuenta activa o vender de contado descontando stock, diseñaremos las clases `Producto`, `ItemConsumo` e `InventarioBarra`. | Comunicación entre objetos, colecciones (`ArrayList` para catálogo y pedidos). | Por iniciar |
| **RF-08, RF-09, RF-10, RF-11** (Gestión de Torneos) | Para organizar competencias, registrar participantes y definir ganadores, crearemos la clase `Torneo` vinculada a la clase abstracta `ModalidadTorneo` y sus subclases `EliminacionDirecta` y `FaseGrupos`. | Herencia, polimorfismo puro (para generar cruces y avanzar rondas sin usar *switch*), composición y colecciones. | Por iniciar |
| **RF-12** (Cierre de Caja Diario) | Para consolidar los ingresos del día discriminando tiempo de juego, cafetería e inscripciones, diseñaremos la clase `CierreCaja` que sumarizará las sesiones cerradas. | Clases, objetos, métodos de cálculo y agregación de datos. | Por iniciar |
| **RNF-01** (Persistencia) | Para garantizar que el estado de máquinas, inventario y torneos sobreviva al cierre, desarrollaremos `ArchivoPuestos` y `ArchivoInventario`. | Archivos planos desacoplados de la lógica (operaciones de guardado y carga). | Por iniciar |

