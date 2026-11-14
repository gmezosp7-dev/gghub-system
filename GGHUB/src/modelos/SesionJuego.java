package modelos;

import java.time.LocalDateTime;

public class SesionJuego {
    // final: cliente, puesto e inicio registran un hecho que ya ocurrio
// (quien juega, donde y desde cuando). Si cambiaran, se cobraria a la persona,
// la estacion o el tiempo equivocados.
    private final Cliente cliente;
    private final PuestoJuego puesto;
    private final LocalDateTime inicio;
    // sin final: el cliente puede pedir mas controles durante la sesion.
    private int controlesExtra;

    public SesionJuego (Cliente cliente, PuestoJuego puesto,  int controlesExtra){
        this.inicio = LocalDateTime.now();
        this.cliente = cliente;
        this.puesto= puesto;
        this.controlesExtra=controlesExtra;
    }
    public LocalDateTime getInicio(){
        return inicio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public int getControlesExtra() {
        return controlesExtra;
    }

    public PuestoJuego getPuesto() {
        return puesto;
    }
    @Override
    public String toString(){
        return "[SESION]"+ " | CLIENTE: "+cliente.getNombre() + " | Puesto: " + getPuesto().getId() + " | Inicio: " + getInicio() + " | ControlesXtra: " + getControlesExtra();


    }
}
