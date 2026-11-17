package modelos;

import java.time.LocalDateTime;

public class Inscripcion {
    //todos final porque nunca se va a cambiar una vez inscritos
    private final Cliente cliente;
    private final LocalDateTime fecha;
    //Se utiliza montoPagado en vez de costoInscripcion por si el costo del torneo cambia,
    //solo se realiza el pago del dia en el que fue inscrito, lo pagado ya no cambia
    private final double montoPagado;

    public Inscripcion(Cliente cliente, double montoPagado) {
        this.cliente = cliente;
        this.fecha = LocalDateTime.now();
        this.montoPagado = montoPagado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    @Override
    public String toString() {
        //getNombre() para no imprimir el cliente completo (documento, telefono, correo)
        return "[INSCRIPCION] | CLIENTE: " + cliente.getNombre() + " | Fecha: " + fecha + " | Pagado: " + montoPagado;
    }
}
