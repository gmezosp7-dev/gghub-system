package servicios;

import modelos.Cliente;
import modelos.SesionJuego;
import enums.EstadoPuesto;
import interfaces.MetodoPago;
import modelos.PuestoJuego;
import java.util.ArrayList;

public class GestorSesiones {
    private ArrayList<SesionJuego> sesiones = new ArrayList<>();
    private final GestorClientes gestorClientes;
    private final GestorPuestos gestorPuestos;

    public GestorSesiones(GestorClientes gestorClientes, GestorPuestos gestorPuestos){
        this.gestorClientes = gestorClientes;
        this.gestorPuestos = gestorPuestos;

    }
    public SesionJuego iniciarSesion(String documento,int id, int controlesExtra){
        Cliente cliente  =gestorClientes.buscarPorDocumento(documento) ;
        if(cliente == null){
            throw new IllegalArgumentException("No existe un cliente con el documento: " + documento );
        }
        PuestoJuego puestoJuego = gestorPuestos.buscarPorId(id);
        if(puestoJuego == null) {
            throw new IllegalArgumentException("No existe un puesto de juego con el id: " + id);
        }
        if(!puestoJuego.estaDisponible()){
            throw new IllegalArgumentException("El puesto de juego "+id +" se encuentra en: " + puestoJuego.getEstado());
        }
        // segundo control (programacion defensiva): el estado es solo un "letrero";
        // las sesiones son la realidad. Si ya hay una abierta en esa estacion, no se abre otra.
        if (buscarSesionActiva(id) != null){
            throw new IllegalArgumentException("El puesto de juego " + id + " ya tiene una sesion abierta");
        }
        if(ValidadorDatos.validadorNegativo(controlesExtra)){
            throw new IllegalArgumentException("la cantidad de controles extra no puede ser negativa");
        }
        SesionJuego sesionJuego = new SesionJuego(cliente,puestoJuego,controlesExtra);
        puestoJuego.cambiarEstado(EstadoPuesto.EN_USO);
        sesiones.add(sesionJuego);
        return sesionJuego;





    }
    public SesionJuego buscarSesionActiva(int id){
        // recorre todas las sesiones buscando la que sigue abierta en esa estacion
        for (SesionJuego e : sesiones){
            if (e.estaActiva() && e.getPuesto().getId() == id){
                return e;
            }
        }
        // no hay ninguna sesion abierta en esa estacion
        return null;
    }
    public SesionJuego finalizarSesion(int id, MetodoPago metodo){
        // se busca UNA sola vez y se guarda en una variable local
        SesionJuego sesion = buscarSesionActiva(id);
        if (sesion == null){
            throw new IllegalArgumentException("No hay una sesion activa en el puesto " + id);
        }
        // la sesion calcula su tiempo, le pide al puesto su costo, cobra (polimorfismo)
        // y, solo si el pago sale bien, libera su estacion. Si el pago falla, lanza aqui y nada cambia.
        sesion.finalizar(metodo);
        return sesion;
    }

    // copia de todas las sesiones (abiertas y cerradas); la usa el cierre de caja (RF-12)
    public ArrayList<SesionJuego> listar(){
        return new ArrayList<>(sesiones);
    }
}
