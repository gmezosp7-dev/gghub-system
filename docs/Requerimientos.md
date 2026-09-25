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

ID	                Descripción	                                    Actor	            Prioridad	                      Criterio de aceptación

RF-01    El sistema debe permitir al operador registrar y          Operador             Alta        El sistema valida que el campo de documento contenga 
         consultar clientes indicando nombre, documento de                                          únicamente números y rechaza el registro si encuentra un
         identidad, teléfono y correo electrónico.                                                  documento registrado previamente.

RF-02    El sistema debe permitir al administrador registrar       Administrador        Alta        El sistema rechaza el registro si el identificador ya existe
         las estaciones de juego diferenciando si es PC o                                           y valida que la tarifa base por hora sea un número
         consola con: identificador numérico, modelo y tarifa                                       estrictamente mayor a cero.
         base por hora. 
         
RF-03    El sistema debe permitir al operador consultar la         Operador             Alta        El sistema muestra una lista con las 25 estaciones
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

RF-07    El sistema debe permitir al operador registrar la venta   Operador             Alta        El sistema impide la venta si la cantidad requerida supera la 
         de productos de la barra, cargándolos a la cuenta de                                       cantidad en stock disponible en el inventario y descuenta 
         cobro de la sesión activa del cliente o procesándolos                                      automáticamente las unidades vendidas en tiempo real tras la 
         como venta directa de contado.                                                             confirmación de la venta.
         
