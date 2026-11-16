package modelos;

import java.time.Duration;
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
    private LocalDateTime fin;
    private double total;

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

    public LocalDateTime getFin() {
        return fin;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString(){
        return "[SESION]"+ " | CLIENTE: "+cliente.getNombre() + " | Puesto: " + getPuesto().getId() + " | Inicio: " + getInicio() + " | ControlesXtra: " + getControlesExtra();


    }
    public boolean estaActiva(){
        return fin == null;
    }

    public double finalizar(){
        if(fin != null){
            throw new IllegalArgumentException("la sesión ya fue finalizada");
        }
        fin = LocalDateTime.now();
        long minutos = Duration.between(inicio, fin).toMinutes();
        double horas = minutos / 60.0;
        total = puesto.calcularCostoSesion(horas,controlesExtra);
        return total;



        }

}
