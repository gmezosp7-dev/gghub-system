package modelos;

import enums.EstadoPuesto;
import interfaces.MetodoPago;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
    private ArrayList<ItemConsumo> consumos = new ArrayList<>();
    // como se pago la cuenta; null mientras la sesion siga abierta
    private MetodoPago metodoPago;
    private double costoTiempo;
    private double costoControles;

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
    public double getCostoTiempo(){
        return costoTiempo;
    }

    public double getCostoControles() {
        return costoControles;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    @Override
    public String toString(){
        return "[SESION]"+ " | CLIENTE: "+cliente.getNombre() + " | Puesto: " + getPuesto().getId() + " | Inicio: " + getInicio() + " | ControlesXtra: " + getControlesExtra();


    }
    //sesion de juego aun activa?
    public boolean estaActiva(){
        return fin == null;
    }
    //finalizacion de sesion


    // Primero se calcula y se cobra; solo si el pago sale bien se cierra la sesion
    // Si el pago falla, la sesion sigue abierta: el cliente no se va sin pagar
    public double finalizar(MetodoPago metodo){
        if(fin != null){
            throw new IllegalArgumentException("la sesión ya fue finalizada");
        }
        if(metodo == null){
            throw new IllegalArgumentException("Para finalizar la sesion se necesita un metodo de pago");
        }
        // se calcula con variables locales: todavia no se cambia nada de la sesion
        LocalDateTime ahora = LocalDateTime.now();
        long minutos = Duration.between(inicio, ahora).toMinutes();
        double horas = minutos / 60.0;
        double tiempoCalculado = puesto.calcularCostoSesion(horas,0);
        double controlesCalculado = puesto.calcularCostoSesion(horas, controlesExtra) - tiempoCalculado;
        double totalCalculado = tiempoCalculado+ totalConsumos()+ controlesCalculado;
        // se cobra  Si falla, lanza y nada cambio
        metodo.pagar(totalCalculado);
        // el pago salio bien: ahora si se cierra la sesion
        fin = ahora;
        total = totalCalculado;
        costoTiempo = tiempoCalculado;
        costoControles = controlesCalculado;
        metodoPago = metodo;
        // RF-05: al procesar el pago se libera la estacion. Lo hace la propia sesion (no el gestor)
        // para que la estacion quede libre por CUALQUIER camino que se finalice; si no, podria quedar
        // EN_USO para siempre sin ninguna sesion activa que la libere.
        puesto.cambiarEstado(EstadoPuesto.DISPONIBLE);
        return total;
    }
        //agregar consumibles a cuenta de sesion final
    //solo si la sesion sigue activa
    public void agregarConsumo(ItemConsumo itemConsumo){
        if (!estaActiva()){
            throw new IllegalArgumentException(("no se pueden cargar consumos a una sesión finalizada"));
        }
        consumos.add((itemConsumo));
    }
    //pago total de consumos al final de la sesion
    public double totalConsumos(){
        double suma = 0.0;
        for(ItemConsumo e : consumos){
            suma = e.subtotal() + suma;

        }
        return suma;
    }
    public ArrayList<ItemConsumo>listar(){
        return new ArrayList<>(consumos);
    }

}
