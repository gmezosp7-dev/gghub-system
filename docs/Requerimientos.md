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






ID	                Descripción	                                    Actor	            Prioridad	                      Criterio de aceptación
  
RF-01	  El sistema debe permitir al administrador registrar       Administrador           Alta      	No se permite registrar dos clientes con el mismo documento.
        clientes con nombre, documento, teléfono y correo.
      
RF-02	  El sistema debe permitir al administrador registrar       Administrador           Alta        Cada consola debe tener un identificador único y un estado inicial.
        las consolas disponibles indicando tipo, modelo, estado
        y precio de alquiler por hora.			
        
RF-03	  El sistema debe permitir al administrador consultar las    Administrador          Alta        La consulta solo muestra las consolas que se encuentran disponibles 
        consolas disponibles para alquiler.			                                                      para un nuevo alquiler.
        
RF-04	  El sistema debe permitir al empleado registrar el alquiler  Empleado              Alta        El sistema debe impedir el alquiler de una consola que ya esté ocupada.
        de una consola a un cliente, indicando la consola, fecha, 
        hora de inicio y duración.			
        
RF-05	  El sistema debe permitir al empleado registrar la           Empleado              Alta        Al registrar la devolución, la consola cambia a estado disponible 
        devolución de una consola.			                                                              y se calcula el valor total del alquiler.
        
RF-06	  El sistema debe calcular automáticamente el valor del        Sistema              Alta        El valor calculado corresponde a la tarifa de la consola multiplicada 
        alquiler según la consola y el tiempo utilizado.			                                        por el tiempo de alquiler.
        
RF-07	  El sistema debe permitir al empleado consultar los          Empleado              Media       La consulta debe diferenciar entre alquileres activos y finalizados.
        alquileres activos y los alquileres realizados   
        por un cliente.