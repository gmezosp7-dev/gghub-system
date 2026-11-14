package servicios;

import modelos.Cliente;
import modelos.EstacionConsola;
import modelos.SesionJuego;
import enums.EstadoPuesto;
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
        if(ValidadorDatos.validadorNegativo(controlesExtra)){
            throw new IllegalArgumentException("la cantidad de controles extra no puede ser negativa");
        }
        SesionJuego sesionJuego = new SesionJuego(cliente,puestoJuego,controlesExtra);
        puestoJuego.cambiarEstado(EstadoPuesto.EN_USO);
        sesiones.add(sesionJuego);
        return sesionJuego;





    }
}
