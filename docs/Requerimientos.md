




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